package com.real.proj.amc.model.quote;

import static com.real.proj.amc.model.quote.QuotationConstants.ASSET_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.PRODUCTS_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.QUOTATION_OBJ_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.USER_COMMENTS_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.USER_DATA_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.USER_KEY;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import com.real.proj.amc.model.Subscription;
import com.real.proj.amc.model.Tax;
import com.real.proj.amc.model.UserData;
import com.real.proj.amc.repository.AssetRepository;
import com.real.proj.amc.repository.PackageRepository;
import com.real.proj.amc.repository.SubscriptionRepository;
import com.real.proj.amc.repository.TaxRepository;
import com.real.proj.user.model.User;
import com.real.proj.user.service.UserRepository;
import com.real.proj.util.SecurityHelper;

@Component
public class QuotationActionHandler implements QuotationStateChangeListener {

  private static final Logger logger = LoggerFactory.getLogger(QuotationActionHandler.class);

  private QuotationNotificationHandler notificationHandler;
  private QuotationRepository quoteRepository;
  private PackageRepository packageRepository;
  private UserRepository userRepository;
  private AssetRepository assetRepository;
  private TaxRepository taxRepository;
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  public void setNotificationHandler(QuotationNotificationHandler notificationHandler) {
    this.notificationHandler = notificationHandler;
  }

  @Autowired
  public void setQuotationRepository(
      QuotationRepository quoteRepo,
      PackageRepository packageRepo,
      UserRepository userRepository,
      AssetRepository assetRepository,
      TaxRepository taxRepository,
      SubscriptionRepository subscriptionRepository) {
    this.quoteRepository = quoteRepo;
    this.packageRepository = packageRepo;
    this.userRepository = userRepository;
    this.assetRepository = assetRepository;
    this.taxRepository = taxRepository;
    this.subscriptionRepository = subscriptionRepository;
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
      execute(event, message, transition.getTarget().getId(), stateMachine);
    } catch (Exception ex) {
      logger.error("Error performing action \n{}", ex);
      stateMachine.setStateMachineError(ex);
      throw ex;
    }
  }

  public void execute(
      QEvents event,
      Message<QEvents> message,
      QStates targetState,
      StateMachine<QStates, QEvents> stateMachine) {
    logger.info("Event name {}", event.toString());
    switch (event) {
    case CREATE_QUOTE:
      handeCreateQuote(message, targetState);
      break;
    case GENERATE_QUOTE:
      handleGenerateQuote(message, targetState);
      break;
    case ACCEPT:
      handleAcceptQuote(message, targetState, stateMachine);
      break;
    case REGENERATE:
      handleRegenerateQuote(message, targetState);
      break;
    case APPROVE:
      handleApproveQuote(message, targetState);
      break;
    case REJECT:
      handleRejectQuote(message, targetState);
      break;
    case RENEW:
      handleQuoteRenewal(message);
      break;
    case INITIATE_PAY:
      handleInitiatePayment(message, targetState, stateMachine);
    case PAYMENT_SUCCESSFUL:
      handlePaymentReceived(message, targetState);
      break;
    case PAYMENT_ERROR:
      handlePaymentError(message, targetState);
    default:
      logger.error("Invalid Event");
      break;
    }
  }

  private void handleInitiatePayment(
      Message<QEvents> message,
      QStates targetState,
      StateMachine<QStates, QEvents> stateMachine) {
    // TODO perhaps, send a message to payment service
    Map<String, Object> userData = this.getUserData(message);
    Quotation userQuote = this.getQuoteFromUserInput(userData);
    if (userQuote.hasExpired()) {
      this.handleExpiredQuote(userQuote, stateMachine);
      return;
    }
    userQuote.setState(targetState);
    User loggedInUser = secHelper.getLoggedInUser();
    //@formatter:off
    EventHistory eventObj = 
        new EventHistory(message.getPayload().toString(),
            loggedInUser.getUserName(),
            loggedInUser.getRole(),
            "Payment Initated.");
    //@formatter:on       
    userQuote.getHistory().add(eventObj);
    this.quoteRepository.save(userQuote);

  }

  private void handleRejectQuote(Message<QEvents> message, QStates targetState) {
    Map<String, Object> userData = this.getUserData(message);
    Quotation userQuote = this.getQuoteFromUserInput(userData);
    String comments = (String) userData.get(USER_COMMENTS_KEY);
    comments = Optional.of(comments).orElse("");
    userQuote.setState(targetState);
    User loggedInUser = secHelper.getLoggedInUser();
    //@formatter:off
    EventHistory eventObj = 
        new EventHistory(message.getPayload().toString(),
            loggedInUser.getUserName(),
            loggedInUser.getRole(),
            comments);
    //@formatter:on       
    userQuote.getHistory().add(eventObj);
    this.quoteRepository.save(userQuote);
    this.notificationHandler.handleQuotationRejected(userQuote);
  }

  private void handlePaymentError(Message<QEvents> message, QStates targetState) {
    Map<String, Object> userData = this.getUserData(message);
    Quotation userQuote = this.getQuoteFromUserInput(userData);
    userQuote.setState(targetState);
    //@formatter:off
    EventHistory eventObj = 
        new EventHistory(message.getPayload().toString(),
            "SYSTEM",
            "SYSTEM",
            "Failed payment.");
    //@formatter:on       
    userQuote.getHistory().add(eventObj);
    this.quoteRepository.save(userQuote);
  }

  private void handlePaymentReceived(Message<QEvents> message, QStates targetState) {
    // update quotation with state
    Map<String, Object> userData = this.getUserData(message);
    Quotation userQuote = this.getQuoteFromUserInput(userData);
    userQuote.setState(targetState);
    //@formatter:off
    EventHistory eventObj = 
        new EventHistory(message.getPayload().toString(),
            "SYSTEM",
            "SYSTEM",
            "Payment Successful.");
    //@formatter:on   
    userQuote.getHistory().add(eventObj);
    this.quoteRepository.save(userQuote);

    // create subscription
    Subscription subscription = new Subscription(userQuote);
    subscription = this.subscriptionRepository.save(subscription);
    this.notificationHandler.handleSubscriptionCreated(subscription);
  }

  private void handleQuoteRenewal(Message<QEvents> message) {
    Map<String, Object> userData = this.getUserData(message);
    Quotation userQuote = this.getQuoteFromUserInput(userData);
    userQuote.createQuote();
    QStates targetState;
    if (userQuote.hasBeenApproved())
      targetState = QStates.PAYMENT_PENDING;
    else
      targetState = QStates.QUOTATION_REGENERATED;
    userQuote.setState(targetState);
    User loggedInUser = secHelper.getLoggedInUser();
    //@formatter:off
    EventHistory eventObj = 
        new EventHistory(message.getPayload().toString(),
            loggedInUser.getUserName(),
            loggedInUser.getRole(),
            "Quote is renewed.");
    //@formatter:on   
    userQuote.getHistory().add(eventObj);
    this.quoteRepository.save(userQuote);
  }

  private void handleExpiredQuote(Quotation userQuote, StateMachine<QStates, QEvents> stateMachine) {
    logger.error("Quotation with id {} has expired.", userQuote.getId());
    userQuote.setState(QStates.QUOTATION_EXPIRED);
    //@formatter:off
    EventHistory eventObj = 
        new EventHistory(QEvents.EXPIRE.toString(),
            "SYSTEM",
            "SYSTEM",
            "Quote has expired.");
    //@formatter:on       
    userQuote.getHistory().add(eventObj);
    this.quoteRepository.save(userQuote);
    stateMachine.setStateMachineError(
        new IllegalArgumentException(
            "This quotation has expired. You may want to regenerate the quotation again."));
  }

  private void handeCreateQuote(Message<QEvents> message, QStates targetState) {
    logger.info("creating new quote");

    /* extract all user input */
    Map<String, Object> userData = getUserData(message);
    userData = Optional.of(userData).orElse(new HashMap<String, Object>());
    String userId = (String) userData.get(USER_KEY);
    userId = Objects.requireNonNull(userId, "User id cannot be null.");
    String assetId = (String) userData.get(ASSET_KEY);
    assetId = Objects.requireNonNull(assetId, "Asset id cannot be null.");
    String[] productIds = (String[]) userData.get(PRODUCTS_KEY);
    productIds = Objects.requireNonNull(productIds, "Asset id cannot be null.");

    /* validate user input */
    User customer = this.userRepository.findByUserName(userId);
    customer = Objects.requireNonNull(customer, "User with id " + userId + " not found.");
    Asset customerAsset = this.assetRepository.findOne(assetId);
    customerAsset = Objects.requireNonNull(customerAsset, "Asset with id " + assetId + " not found.");

    User loggedInUser = secHelper.getLoggedInUser();
    //@formatter:off
    EventHistory eventObj = 
        new EventHistory(message.getPayload().toString(),
            loggedInUser.getUserName(),
            loggedInUser.getRole(),
            "Quote is created.");
    //@formatter:on

    /* create new quotation */
    Quotation newQuote = new Quotation(customerAsset, loggedInUser);
    logger.info("selected product ids : {}", Arrays.asList(productIds));

    Iterable<AMCPackage> products = this.packageRepository.findValidProducts(productIds);
    logger.info("selected product list : {}", products);

    if (logger.isDebugEnabled())
      logger.debug("selected product list : {}", products);
    if (products == null || !products.iterator().hasNext())
      throw new IllegalArgumentException("The products you selected are not valid.");
    products.forEach(item -> newQuote.addProduct(item));
    newQuote.setState(targetState);
    newQuote.getHistory().add(eventObj);
    this.quoteRepository.save(newQuote);
    logger.info("New quote created");
  }

  private void handleGenerateQuote(Message<QEvents> message, QStates targetState) {
    logger.info("Generate a quote");
    if (logger.isDebugEnabled())
      logger.debug("handleGenerateQuote  {}", message.getHeaders());
    handleGenerateQuoteInternal(message, QEvents.GENERATE_QUOTE, targetState);
  }

  private void handleRegenerateQuote(Message<QEvents> message, QStates targetState) {
    logger.info("regenerate quote");
    handleGenerateQuoteInternal(message, QEvents.REGENERATE, targetState);
  }

  private void handleAcceptQuote(
      Message<QEvents> message,
      QStates targetState,
      StateMachine<QStates, QEvents> stateMachine) {
    logger.info("User accepted the quote");
    Map<String, Object> userData = this.getUserData(message);
    Quotation userQuote = this.getQuoteFromUserInput(userData);
    // handle if quote is expired
    if (userQuote.hasExpired()) {
      handleExpiredQuote(userQuote, stateMachine);
      return;
    }
    String comments = (String) userData.get(QuotationConstants.USER_COMMENTS_KEY);
    comments = Optional.of(comments).orElse("");
    userQuote.userAccepted();
    User loggedInUser = secHelper.getLoggedInUser();
    //@formatter:off
    EventHistory event = 
        new EventHistory(
            message.getPayload().toString(),
            loggedInUser.getUserName(),
            loggedInUser.getRole(),
            comments);
    //@formatter:off
    userQuote.getHistory().add(event);
    userQuote.setState(targetState);
    this.quoteRepository.save(userQuote);
    this.notificationHandler.handleQuotationAcceptedByUser(userQuote);
  }

  private void handleApproveQuote(Message<QEvents> message, QStates targetState) {
    Map<String, Object> userData = this.getUserData(message);
    Quotation userQuote = this.getQuoteFromUserInput(userData);
    String comments = (String) userData.get(USER_COMMENTS_KEY);
    comments = Optional.of(comments).orElse("");
    User loggedInUser = secHelper.getLoggedInUser();
    //@formatter:off
    EventHistory eventObj = 
        new EventHistory(
            message.getPayload().toString(),
            loggedInUser.getUserName(),
            loggedInUser.getRole(),
            comments);
    //@formatter:on

    userQuote.getHistory().add(eventObj);
    userQuote.setState(targetState);
    this.quoteRepository.save(userQuote);
    this.notificationHandler.handleQuotationApprovedByAdmin(userQuote);
  }

  private void handleGenerateQuoteInternal(Message<QEvents> message, QEvents event, QStates nextState) {
    Map<String, Object> userData = getUserData(message);
    UserData data = (UserData) userData.get(USER_DATA_KEY);
    data = Objects.requireNonNull(data, "Service specific data cannot be null.");
    Quotation quote = getQuoteFromUserInput(userData);
    List<Tax> applicableTaxes = getApplicableTaxes();
    quote.setUserData(data);
    quote.setApplicableTaxes(applicableTaxes);
    quote.createQuote();
    String loggedInUser = secHelper.getLoggedInUser().getUserName();
    String userRole = secHelper.getLoggedInUser().getRole();
    //@formatter:off
    EventHistory eventObj = 
        new EventHistory(event.toString(),
            loggedInUser,
            userRole,
            "Quote is generated.");
    //@formatter:on

    quote.getHistory().add(eventObj);
    quote.setState(nextState);
    if (event.equals(QEvents.REGENERATE))
      quote.approveQuote();
    this.quoteRepository.save(quote);
  }

  private List<Tax> getApplicableTaxes() {
    return this.taxRepository.findByType("ST");
  }

  private Quotation getQuoteFromUserInput(Map<String, Object> userData) {
    Quotation quote = (Quotation) userData.get(QUOTATION_OBJ_KEY);
    return quote;
  }

  private Map<String, Object> getUserData(Message<QEvents> message) {
    Map<String, Object> userData = (Map<String, Object>) message.getHeaders().get("DATA_KEY");
    userData = Optional.of(userData).orElse(new HashMap<String, Object>(1));
    return userData;
  }
}
