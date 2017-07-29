package com.real.proj.amc.controller;

import static com.real.proj.amc.model.quote.QuotationConstants.ASSET_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.QUOTATION_OBJ_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.SELECTED_PACKAGES_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.SELECTED_SERVICES_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.USER_COMMENTS_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.USER_DATA_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.USER_KEY;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.amc.controller.pojo.UserDecision;
import com.real.proj.amc.model.Category;
import com.real.proj.amc.model.UserData;
import com.real.proj.amc.model.quote.NewQuoteInput;
import com.real.proj.amc.model.quote.QEvents;
import com.real.proj.amc.model.quote.QStates;
import com.real.proj.amc.model.quote.Quotation;
import com.real.proj.amc.model.quote.QuotationRepository;
import com.real.proj.amc.model.quote.QuotationStateHandler;
import com.real.proj.util.SecurityHelper;

@RestController
@RequestMapping("/api/v1")
public class SubscriptionOrderController {

  private static Logger logger = LoggerFactory.getLogger(SubscriptionOrderController.class);

  SecurityHelper secHelper;
  QuotationStateHandler quotationHandler;
  QuotationRepository quotationRepository;

  @Autowired
  public void setSecurityHelper(SecurityHelper secHelper) {
    this.secHelper = secHelper;
  }

  @Autowired
  public void setQuotationStateHandler(QuotationStateHandler handler) {
    this.quotationHandler = handler;
  }

  @Autowired
  public void setQuotationRepository(QuotationRepository quotationRepository) {
    this.quotationRepository = quotationRepository;
  }

  @RequestMapping(path = "/quotation", method = RequestMethod.POST, consumes = "application/json")
  public void createNewSubscription(
      @RequestBody @Validated NewQuoteInput userInput) throws Exception {
    logger.info("New quotation requested for {}", userInput);
    Map<String, Object> data = new HashMap<String, Object>();
    data.put(USER_KEY, userInput.getUserId());
    data.put(ASSET_KEY, userInput.getAssetId());
    data.put(SELECTED_PACKAGES_KEY, userInput.getPackageID());
    data.put(SELECTED_SERVICES_KEY, userInput.getPackageID());
    fireEvent(QStates.INITIAL, QEvents.CREATE_QUOTE, data);
  }

  @RequestMapping(path = "/quotation/{id}/generate", method = RequestMethod.POST, consumes = "application/json")
  public void generateQuotation(
      @PathVariable String id,
      @RequestBody @Validated UserData userData) throws Exception {
    logger.info("Genereate quotation requested for {}", id);
    userData = Objects.requireNonNull(userData, "UserData cannot be null");
    Map<String, Object> data = new HashMap<String, Object>();
    Quotation userQuotation = this.quotationRepository.findOne(id);
    userQuotation = Objects.requireNonNull(userQuotation, "Quotation with id " + id + " not found.");
    data.put(QUOTATION_OBJ_KEY, userQuotation);
    data.put(USER_DATA_KEY, userData);
    fireEvent(userQuotation.getState(), QEvents.GENERATE_QUOTE, data);
  }

  @RequestMapping(path = "/quotation/{id}/decision", method = RequestMethod.POST, consumes = "application/json")
  public void acceptOrRejectQuotation(
      @PathVariable String id,
      @RequestBody @Validated UserDecision decision) throws Exception {
    logger.info("User response provided for {}", id);
    decision = Objects.requireNonNull(decision, "Decision cannot be null");
    Map<String, Object> data = new HashMap<String, Object>();
    Quotation userQuotation = this.quotationRepository.findOne(id);
    userQuotation = Objects.requireNonNull(userQuotation, "Quotation with id " + id + " not found.");
    data.put(QUOTATION_OBJ_KEY, userQuotation);
    data.put(USER_COMMENTS_KEY, decision.getComments());
    QEvents event = decision.getDecision().equalsIgnoreCase("yes") ? QEvents.ACCEPT : QEvents.REJECT;
    fireEvent(userQuotation.getState(), event, data);
  }

  @RequestMapping(path = "/quotation/{id}/pay", method = RequestMethod.POST, consumes = "application/json")
  public void pay(
      @PathVariable String id) throws Exception {
    logger.info("Payment proceeded for {}", id);
    Map<String, Object> data = new HashMap<String, Object>();
    Quotation userQuotation = this.quotationRepository.findOne(id);
    userQuotation = Objects.requireNonNull(userQuotation, "Quotation with id " + id + " not found.");
    data.put(QUOTATION_OBJ_KEY, userQuotation);
    fireEvent(userQuotation.getState(), QEvents.INITIATE_PAY, data);
  }

  @RequestMapping(path = "/quotation/{id}/renew", method = RequestMethod.POST, consumes = "application/json")
  public void renewQuotation(
      @PathVariable String id) throws Exception {
    logger.info("Renew quotation with id {}", id);
    Map<String, Object> data = new HashMap<String, Object>();
    Quotation userQuotation = this.quotationRepository.findOne(id);
    userQuotation = Objects.requireNonNull(userQuotation, "Quotation with id " + id + " not found.");
    data.put(QUOTATION_OBJ_KEY, userQuotation);
    fireEvent(userQuotation.getState(), QEvents.RENEW, data);
  }

  @RequestMapping(path = "/quotation/test", method = RequestMethod.POST, consumes = "application/json")
  public void someEnumInput(@RequestBody @Validated Category.AssetServiceType type) {
    logger.info("Type {}", type);
  }

  protected void fireEvent(QStates source, QEvents event, Object data) throws Exception {
    Message<QEvents> msg = MessageBuilder.withPayload(event)
        .setHeader("DATA_KEY", data)
        .build();
    this.quotationHandler.handlEvent(msg, source);
  }

}
