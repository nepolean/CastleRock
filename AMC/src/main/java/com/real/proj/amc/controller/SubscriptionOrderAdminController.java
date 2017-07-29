package com.real.proj.amc.controller;

import static com.real.proj.amc.model.quote.QuotationConstants.QUOTATION_OBJ_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.USER_COMMENTS_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.USER_DATA_KEY;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.amc.controller.pojo.UserDecision;
import com.real.proj.amc.model.AbstractJob;
import com.real.proj.amc.model.UserData;
import com.real.proj.amc.model.quote.QEvents;
import com.real.proj.amc.model.quote.QStates;
import com.real.proj.amc.model.quote.Quotation;
import com.real.proj.amc.model.subscription.Subscription;
import com.real.proj.amc.model.subscription.SubscriptionJobScheduler;
import com.real.proj.amc.repository.JobRepository;
import com.real.proj.amc.repository.SubscriptionRepository;

@RestController
@RequestMapping("/api/v1/admin")
@Secured("ROLE_ADMIN")
public class SubscriptionOrderAdminController extends SubscriptionOrderController {
  private static Logger logger = LoggerFactory.getLogger(SubscriptionOrderAdminController.class);
  private SubscriptionRepository subscriptionRepository;
  private JobRepository jobRepository;
  private SubscriptionJobScheduler jobScheduler;

  @Autowired
  public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
    this.subscriptionRepository = subscriptionRepository;
  }

  @Autowired
  public void setJobRepository(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
  }

  @Autowired
  public void setJobScheduler(SubscriptionJobScheduler jobScheduler) {
    this.jobScheduler = jobScheduler;
  }

  @RequestMapping(path = "/quotation/{id}/regenerate", method = RequestMethod.POST, consumes = "application/json")
  public void reGenerateQuotation(@PathVariable String id, @RequestBody @Validated UserData userData) throws Exception {
    logger.info("Genereate quotation requested for {}", id);
    userData = Objects.requireNonNull(userData, "UserData cannot be null");
    Map<String, Object> data = new HashMap<String, Object>();
    Quotation userQuotation = getQuotation(id);
    data.put(QUOTATION_OBJ_KEY, userQuotation);
    data.put(USER_DATA_KEY, userData);
    fireEvent(userQuotation.getState(), QEvents.REGENERATE, data);
  }

  @RequestMapping(path = "/quotation/{id}/choice", method = RequestMethod.POST, consumes = "application/json")
  public void approveOrRejectQuotation(
      @PathVariable String id,
      @RequestBody @Validated UserDecision decision) throws Exception {
    logger.info("User response provided for {}", id);
    decision = Objects.requireNonNull(decision, "Decision cannot be null");
    Map<String, Object> data = new HashMap<String, Object>();
    Quotation userQuotation = getQuotation(id);
    data.put(QUOTATION_OBJ_KEY, userQuotation);
    data.put(USER_COMMENTS_KEY, decision.getComments());
    QEvents event = decision.getDecision().equalsIgnoreCase("yes") ? QEvents.APPROVE : QEvents.REJECT;
    fireEvent(userQuotation.getState(), event, data);
  }

  //@formatter:off
  /****************************************************************************************************
   *                                                                                                  *
   *                              RECONCILE ACTIONS                                                   *
   *                                                                                                  *
   ****************************************************************************************************/
  //@formatter:on

  @RequestMapping(path = "quotation/{id}/subscription", method = RequestMethod.POST, consumes = "application/json")
  public ResponseEntity<String> generateSubscriptionFromQuotation(@PathVariable @Validated String id) throws Exception {
    logger.info("Create quotation manually from paid quotation {}", id);
    Subscription subscription = this.subscriptionRepository.findByQuotationId(id);
    if (logger.isDebugEnabled())
      logger.debug("Subscription -> {}", subscription);
    if (Objects.nonNull(subscription))
      return new ResponseEntity<String>("Subscription was already created.", HttpStatus.OK);
    // first ensure payment is made
    Quotation userQuotation = getQuotation(id);
    QStates currentState = userQuotation.getState();
    if (logger.isDebugEnabled())
      logger.debug("Current state of the quotation is {}", userQuotation.getState());
    if (!QStates.SUBSCRIBED.equals(currentState)) {
      // looks like the payment has not been made
      return new ResponseEntity<String>("The payment is pending on this quotation.", HttpStatus.PRECONDITION_FAILED);
    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put(QUOTATION_OBJ_KEY, userQuotation);
    this.fireEvent(currentState, QEvents.PAYMENT_SUCCESSFUL, data);
    return new ResponseEntity<String>("Subscription is successful.", HttpStatus.OK);
  }

  @RequestMapping(path = "quotation/{id}/paid", method = RequestMethod.POST, consumes = "application/json")
  public ResponseEntity<String> handlePaymentSusccess(@PathVariable @Validated String id) throws Exception {
    logger.info("Handle missing payment authorization for quotation {} ", id);
    Quotation userQuotation = this.getQuotation(id);
    Map<String, Object> data = new HashMap<String, Object>();
    data.put(QUOTATION_OBJ_KEY, userQuotation);
    this.fireEvent(userQuotation.getState(), QEvents.PAYMENT_SUCCESSFUL, data);
    return new ResponseEntity<String>("Subscription is successful.", HttpStatus.OK);
  }

  @RequestMapping(path = "subscription/{id}/paid", method = RequestMethod.POST, consumes = "application/json")
  public ResponseEntity<String> scheduleJobsFromSubscription(@PathVariable @Validated String id) {
    logger.info("Schedule missing jobs for subscription {}", id);
    Subscription userSubscription = getSubscription(id);

    // create tasks if not created already
    // The sources to create any job are many.
    // How should a job look like? A generic job covers the basic information.
    // How to model additional parameters such as asset details for asset based
    // job?
    List<AbstractJob> scheduledJobs = this.jobRepository
        .findBySourceTypeAndSourceId(userSubscription.getClass().getName(), userSubscription.getId());
    String response = "";
    if (scheduledJobs.isEmpty()) {
      logger.info("The jobs are created yet. Create one now");
      this.jobScheduler.scheduleJob(userSubscription);
      response = "Jobs have been created successfully.";
    } else {
      response = "Jobs were already created.";
    }
    return new ResponseEntity<String>(response, HttpStatus.OK);
  }

  private Subscription getSubscription(String subscriptionId) {
    Subscription userSubscription = this.subscriptionRepository.findOne(subscriptionId);
    userSubscription = Objects.requireNonNull(userSubscription,
        "Subscription with id " + subscriptionId + " not found.");
    return userSubscription;
  }

  private Quotation getQuotation(String id) {
    Quotation userQuotation = this.quotationRepository.findOne(id);
    userQuotation = Objects.requireNonNull(userQuotation, "Quotation with id " + id + " not found.");
    return userQuotation;
  }

}
