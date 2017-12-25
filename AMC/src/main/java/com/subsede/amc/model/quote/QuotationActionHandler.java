package com.subsede.amc.model.quote;

import static com.subsede.amc.model.quote.QuotationConstants.ASSET_KEY;
import static com.subsede.amc.model.quote.QuotationConstants.QUOTATION_OBJ_KEY;
import static com.subsede.amc.model.quote.QuotationConstants.USER_COMMENTS_KEY;
import static com.subsede.amc.model.quote.QuotationConstants.USER_DATA_KEY;
import static com.subsede.amc.model.quote.QuotationConstants.USER_KEY;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import com.subsede.amc.catalog.model.ISubscriptionPackage;
import com.subsede.amc.catalog.model.Product;
import com.subsede.amc.catalog.model.Tax;
import com.subsede.amc.catalog.repository.PackageRepository;
import com.subsede.amc.catalog.repository.TaxRepository;
import com.subsede.amc.model.Asset;
import com.subsede.amc.model.EventHistory;
import com.subsede.amc.model.UserData;
import com.subsede.amc.model.subscription.Subscription;
import com.subsede.amc.model.subscription.SubscriptionJobScheduler;
import com.subsede.amc.repository.AssetRepository;
import com.subsede.amc.repository.SubscriptionRepository;
import com.subsede.user.model.Customer;
import com.subsede.user.model.user.User;
import com.subsede.user.repository.user.UserRepository;
import com.subsede.util.SecurityHelper;

@Component
public class QuotationActionHandler implements QuotationStateChangeListener {

  private static final Logger logger = LoggerFactory.getLogger(QuotationActionHandler.class);

  private QuotationNotificationHandler notificationHandler;
  private SubscriptionJobScheduler subscriptionJobHandler;
  private QuotationRepository quoteRepository;
  private PackageRepository<ISubscriptionPackage> packageRepository;
  private UserRepository<Customer> userRepository;
  private AssetRepository assetRepository;
  private TaxRepository taxRepository;
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  public void setNotificationHandler(
      QuotationNotificationHandler notificationHandler,
      SubscriptionJobScheduler subscriptionJobHandler) {
    this.notificationHandler = notificationHandler;
    this.subscriptionJobHandler = subscriptionJobHandler;
  }

  @Autowired
  public void setQuotationRepository(
      QuotationRepository quoteRepo,
      PackageRepository<ISubscriptionPackage> packageRepo,
      UserRepository<Customer> userRepository,
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
    updateHistory(message, userQuote, "Payment Initated.");
    this.quoteRepository.save(userQuote);

  }

  private void updateHistory(Message<QEvents> message, Quotation userQuote, String comment) {
    User loggedInUser = secHelper.getLoggedInUser();
    //@formatter:off
    EventHistory eventObj = 
        new EventHistory(message.getPayload().toString(),
            loggedInUser.getUsername(),
            loggedInUser.getRoles().iterator().next().getName(),
            comment
            );
    //@formatter:on       
    userQuote.getHistory().add(eventObj);
  }

  private void handleRejectQuote(Message<QEvents> message, QStates targetState) {
    Map<String, Object> userData = this.getUserData(message);
    Quotation userQuote = this.getQuoteFromUserInput(userData);
    String comments = (String) userData.get(USER_COMMENTS_KEY);
    comments = Optional.of(comments).orElse("");
    userQuote.setState(targetState);
    this.updateHistory(message, userQuote, comments);
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
    this.subscriptionJobHandler.scheduleJob(subscription);
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
    this.updateHistory(message, userQuote, "Quotation Renewed");
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
    String[] selectedPackages = (String[]) userData.get(QuotationConstants.SELECTED_PACKAGES_KEY);
    String[] selectedServices = (String[]) userData.get(QuotationConstants.SELECTED_SERVICES_KEY);

    if (logger.isDebugEnabled())
      logger.debug("Package ids {}", Arrays.asList(selectedPackages));
    if (logger.isDebugEnabled())
      logger.debug("Service ids {}", Arrays.asList(selectedServices));

    if ((selectedPackages == null && selectedServices == null)
        || (selectedPackages.length == 0 && selectedServices.length == 0))
      throw new IllegalArgumentException("You must selecte at least 1 package/service.");

    /* validate user input */
    User customer = this.userRepository.findByUsername(userId);
    customer = Objects.requireNonNull(customer, "User with id " + userId + " not found.");
    Asset customerAsset = this.assetRepository.findOne(assetId);
    customerAsset = Objects.requireNonNull(customerAsset, "Asset with id " + assetId + " not found.");

    /* Convert ids to real packages/services */
    Set<ISubscriptionPackage> allSelectedProducts = Collections.emptySet();
    List<ISubscriptionPackage> packages = this.packageRepository.findValidProducts(selectedPackages);
    if (logger.isDebugEnabled())
      logger.debug("selected product list : {}", packages);
    rejectIfPackagesMismatch(selectedPackages, packages);
    allSelectedProducts.addAll(packages);
    //List<Service> services = this.serviceRepository.findValidServices(selectedServices);
    //if (logger.isDebugEnabled())
    //  logger.debug("Selected Services {}", services);
    //rejectIfPackagesMismatch(selectedServices, services);
    //allSelectedProducts.addAll(services);
    /* create a new quotation */
    Quotation newQuote = new Quotation(customerAsset, secHelper.getLoggedInUser());

    allSelectedProducts.forEach(item -> newQuote.addProduct(item));
    newQuote.setState(targetState);
    this.updateHistory(message, newQuote, "Quotation is created");
    this.quoteRepository.save(newQuote);
    logger.info("New quote created");
  }

  private void rejectIfPackagesMismatch(String[] selectedPackages, List<? extends Product> packageObjects) {
    if (packageObjects == null)
      throw new IllegalArgumentException("The selected pacakges are not valid.");
    if (packageObjects.size() != selectedPackages.length) {
      // find mismatches
      List<String> mismatchedPackages = new LinkedList<String>();
      for (String id : selectedPackages) {
        boolean matched = false;
        for (Product pkg : packageObjects) {
          if (id.equals(pkg.getId())) {
            matched = true;
            break;
          }
        }
        if (!matched) {
          logger.warn("Product with product id {} not found.", id);
          mismatchedPackages.add(id);
        }
      }
      if (mismatchedPackages.size() > 0) {
        throw new IllegalArgumentException("Following products are not found/valid " + mismatchedPackages.toString());
      }
    }
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
    this.updateHistory(message, userQuote, comments);
    userQuote.setState(targetState);
    this.quoteRepository.save(userQuote);
    this.notificationHandler.handleQuotationAcceptedByUser(userQuote);
  }

  private void handleApproveQuote(Message<QEvents> message, QStates targetState) {
    Map<String, Object> userData = this.getUserData(message);
    Quotation userQuote = this.getQuoteFromUserInput(userData);
    String comments = (String) userData.get(USER_COMMENTS_KEY);
    comments = Optional.of(comments).orElse("");
    this.updateHistory(message, userQuote, comments);
    userQuote.setState(targetState);
    this.quoteRepository.save(userQuote);
    this.notificationHandler.handleQuotationApprovedByAdmin(userQuote);
  }

  private void handleGenerateQuoteInternal(Message<QEvents> message, QEvents event, QStates nextState) {
    Map<String, Object> userData = getUserData(message);
    UserData data = (UserData) userData.get(USER_DATA_KEY);
    data = Objects.requireNonNull(data, "Service specific data cannot be null.");
    Quotation userQuote = getQuoteFromUserInput(userData);
    List<Tax> applicableTaxes = getApplicableTaxes();
    userQuote.setUserData(data);
    userQuote.setApplicableTaxes(applicableTaxes);
    userQuote.createQuote();
    this.updateHistory(message, userQuote, "Quotation is generated");
    userQuote.setState(nextState);
    if (event.equals(QEvents.REGENERATE))
      userQuote.approveQuote();
    this.quoteRepository.save(userQuote);
  }

  private List<Tax> getApplicableTaxes() {
    return this.taxRepository.findByType("ST");
  }

  private Quotation getQuoteFromUserInput(Map<String, Object> userData) {
    Quotation quote = (Quotation) userData.get(QUOTATION_OBJ_KEY);
    return quote;
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> getUserData(Message<QEvents> message) {
    Map<String, Object> userData = (Map<String, Object>) message.getHeaders().get("DATA_KEY");
    userData = Optional.of(userData).orElse(new HashMap<String, Object>(1));
    return userData;
  }
}
