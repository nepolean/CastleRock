package com.real.proj.amc.model.quote;

import static com.real.proj.amc.model.quote.QuotationConstants.ASSET_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.PRODUCTS_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.QUOTE_ID_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.USER_DATA;
import static com.real.proj.amc.model.quote.QuotationConstants.USER_KEY;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import com.real.proj.amc.model.AMCPackage;
import com.real.proj.amc.model.Asset;
import com.real.proj.amc.model.EventHistory;
import com.real.proj.amc.model.UserData;
import com.real.proj.amc.repository.PackageRepository;
import com.real.proj.amc.service.AssetRepository;
import com.real.proj.controller.exception.EntityNotFoundException;
import com.real.proj.user.model.User;
import com.real.proj.user.service.UserRepository;
import com.real.proj.util.SecurityHelper;

@Component
public class QuotationActionHandler implements QuotationStateChangeListener {

  private static final Logger logger = LoggerFactory.getLogger(QuotationActionHandler.class);

  QuotationRepository quoteRepository;
  PackageRepository packageRepository;
  private UserRepository userRepository;
  private AssetRepository assetRepository;

  @Autowired
  public void setQuotationRepository(
      QuotationRepository quoteRepo,
      PackageRepository packageRepo,
      UserRepository userRepository,
      AssetRepository assetRepository) {
    this.quoteRepository = quoteRepo;
    this.packageRepository = packageRepo;
    this.userRepository = userRepository;
    this.assetRepository = assetRepository;
  }

  @Autowired
  SecurityHelper secHelper;

  @Override
  public void onStateChange(
      State<QStates, QEvents> state,
      Message<QEvents> message,
      Transition<QStates, QEvents> transition,
      StateMachine<QStates, QEvents> stateMachine) {

    QEvents event = message.getPayload();
    try {
      execute(event, message);
    } catch (Exception ex) {
      logger.error("Error performing action \n{}", ex);
      stateMachine.setStateMachineError(ex);
      throw ex;
    }
  }

  public void execute(QEvents event, Message<QEvents> message) {
    switch (event) {
    case CREATE:
      handeCreateQuote(message);
      break;
    case SUBMITUSERDATA:
      handleGenerateQuote(message);
      break;
    case ACCEPT:
      Map<String, Object> userInput = getUserData(message);
      Quotation quote = getQuoteFromUserInput(userInput);
      handleAcceptQuote(quote, userInput);
      break;
    case REGENERATE:
      handleRegenerateQuote(message);
      break;
    case APPROVE:
      userInput = getUserData(message);
      quote = getQuoteFromUserInput(userInput);
      handleApproveQuote(quote, userInput);
      break;
    case PAY:
      userInput = getUserData(message);
      quote = getQuoteFromUserInput(userInput);
      handlePaymentReceived(quote, userInput);
      break;
    default:
      logger.error("Invalid Event");
      break;
    }
  }

  private void handeCreateQuote(Message<QEvents> message) {
    logger.info("creating new quote");
    Map<String, Object> userData = getUserData(message);
    userData = Optional.of(userData).orElse(new HashMap<String, Object>());
    String userId = (String) userData.get(USER_KEY);
    userId = Objects.requireNonNull(userId, "User id cannot be null.");
    String assetId = (String) userData.get(ASSET_KEY);
    assetId = Objects.requireNonNull(assetId, "Asset id cannot be null.");
    String[] productIds = (String[]) userData.get(PRODUCTS_KEY);
    productIds = Objects.requireNonNull(productIds, "Asset id cannot be null.");

    User customer = this.userRepository.findByUserName(userId);
    customer = Objects.requireNonNull(customer, "User with id " + userId + " not found.");
    Asset customerAsset = this.assetRepository.findOne(assetId);
    customerAsset = Objects.requireNonNull(customerAsset, "Asset with id " + assetId + " not found.");

    User agent = secHelper.getLoggedInUser();

    Quotation newQuote = new Quotation(customerAsset, agent);

    Iterable<AMCPackage> products = this.packageRepository.findValidProducts(Arrays.asList(productIds));
    if (logger.isDebugEnabled())
      logger.debug("selected product list : {}", products);
    if (products == null || !products.iterator().hasNext())
      throw new IllegalArgumentException("The products you selected are not valid.");
    products.forEach(item -> newQuote.addProduct(item));
    newQuote.setState(QStates.NEW_QUOTATION_CREATED);
    this.quoteRepository.save(newQuote);
    logger.info("New quote created");
  }

  private void handleRegenerateQuote(Message<QEvents> message) {
    logger.info("regenerate quote");
    Map<String, Object> userData = getUserData(message);
    UserData data = (UserData) userData.get(USER_DATA);
    data = Objects.requireNonNull(data, "Service specific data cannot be null.");
    Quotation quote = getQuoteFromUserInput(userData);
    quote.setUserData(data);
    EventHistory event = new EventHistory(QEvents.REGENERATE.toString(), "ADMIN");
    quote.getHistory().add(event);
    this.quoteRepository.save(quote);
    notifyUser(quote);
  }

  private Quotation getQuoteFromUserInput(Map<String, Object> userData) {
    String quoteId = (String) userData.get(QUOTE_ID_KEY);
    quoteId = Objects.requireNonNull(quoteId, "Quotation id cannot be null.");
    Quotation quote = this.quoteRepository.findOne(quoteId);
    if (quote == null)
      throw new EntityNotFoundException(quoteId, "", "QUOTATION");
    return quote;
  }

  private Map<String, Object> getUserData(Message<QEvents> message) {
    Map<String, Object> userData = (Map<String, Object>) message.getHeaders().get("DATA_KEY");
    userData = Optional.of(userData).orElse(new HashMap<String, Object>(1));
    return userData;
  }

  private void handlePaymentReceived(Quotation quote, Map<String, Object> args) {
    EventHistory event = new EventHistory(QEvents.RECEIVE_PAYMENT.toString(), "SYSTEM");
    quote.getHistory().add(event);
    this.quoteRepository.save(quote);
    // TODO: Complete this
    // createSubscription();
  }

  private void handleApproveQuote(Quotation quote, Map<String, Object> args) {
    EventHistory event = new EventHistory(QEvents.APPROVE.toString(), "ADMIN");
    quote.getHistory().add(event);
    this.quoteRepository.save(quote);
  }

  private void handleAcceptQuote(Quotation quote, Map<String, Object> args) {
    EventHistory event = new EventHistory(QEvents.ACCEPT.toString(), "USER");
    quote.getHistory().add(event);
    this.quoteRepository.save(quote);
  }

  private void handleGenerateQuote(Message<QEvents> message) {
    logger.info("generate quote");
    if (logger.isDebugEnabled())
      logger.debug("handleGenerateQuote  {}", message.getHeaders());
    handleRegenerateQuote(message);
    // throw new RuntimeException("This should reset the state to original
    // value");
  }

  private void notifyUser(Quotation quote) {
    // TODO: Complete this action

  }

  private Object[] validate(Quotation quote, Object[] args, Class clazz) {
    args = Objects.requireNonNull(args, "The arguments cannot be null");
    if (!(clazz.isInstance(args[0]))) {
      logger.error("The argument is not of type UserData");
      throw new IllegalArgumentException("Invalid user input. Expected UserData");
    }
    return args;
  }

}
