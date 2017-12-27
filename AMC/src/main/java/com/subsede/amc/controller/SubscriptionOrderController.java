package com.subsede.amc.controller;

import static com.subsede.amc.model.quote.QuotationConstants.ASSET_KEY;
import static com.subsede.amc.model.quote.QuotationConstants.QUOTATION_OBJ_KEY;
import static com.subsede.amc.model.quote.QuotationConstants.SELECTED_PACKAGES_KEY;
import static com.subsede.amc.model.quote.QuotationConstants.SELECTED_SERVICES_KEY;
import static com.subsede.amc.model.quote.QuotationConstants.USER_COMMENTS_KEY;
import static com.subsede.amc.model.quote.QuotationConstants.USER_DATA_KEY;
import static com.subsede.amc.model.quote.QuotationConstants.USER_KEY;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.subsede.amc.controller.dto.UserDecision;
import com.subsede.amc.model.UserData;
import com.subsede.amc.model.quote.NewQuoteDTO;
import com.subsede.amc.model.quote.QEvents;
import com.subsede.amc.model.quote.QStates;
import com.subsede.amc.model.quote.Quotation;
import com.subsede.amc.model.quote.QuotationRepository;
import com.subsede.amc.model.quote.QuotationStateHandler;
import com.subsede.util.SecurityHelper;

@RestController
@RequestMapping("/api/v1/quote")
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

  @PostMapping("")
  public ResponseEntity<Quotation> createNewSubscriptionOrder(
      @RequestBody @Validated NewQuoteDTO userInput) throws Exception {
    logger.info("New quotation requested for {}", userInput);
    Map<String, Object> data = new HashMap<String, Object>();
    data.put(USER_KEY, userInput.getUserId());
    data.put(ASSET_KEY, userInput.getAssetId());
    data.put(SELECTED_PACKAGES_KEY, userInput.getPackageID());
    data.put(SELECTED_SERVICES_KEY, userInput.getPackageID());
    Message<QEvents> response = fireEvent(QStates.INITIAL, QEvents.CREATE_QUOTE, data);
    Quotation newQuote = (Quotation) response.getHeaders().get("NEW_QUOTE");
    return ResponseEntity.ok(newQuote);
  }

  @PostMapping(path = "/{id}/generate")
  public void generateQuotation(
      @PathVariable String id,
      @RequestBody @Validated UserData userData) throws Exception {
    logger.info("Genereate quotation requested for quote {}", id);
    userData = Objects.requireNonNull(userData, "UserData cannot be null");
    Map<String, Object> data = new HashMap<String, Object>();
    Quotation userQuotation = this.quotationRepository.findOne(id);
    userQuotation = Objects.requireNonNull(userQuotation, "Quotation with id " + id + " not found.");
    data.put(QUOTATION_OBJ_KEY, userQuotation);
    data.put(USER_DATA_KEY, userData);
    fireEvent(userQuotation.getState(), QEvents.GENERATE_QUOTE, data);
  }

  @PostMapping(path = "/{id}/decision")
  public void acceptOrRejectQuotation(
      @PathVariable String id,
      @RequestBody @Validated UserDecision decision) throws Exception {
    logger.info("User response provided for quote {}", id);
    decision = Objects.requireNonNull(decision, "Decision cannot be null");
    Map<String, Object> data = new HashMap<String, Object>();
    Quotation userQuotation = this.quotationRepository.findOne(id);
    userQuotation = Objects.requireNonNull(userQuotation, "Quotation with id " + id + " not found.");
    data.put(QUOTATION_OBJ_KEY, userQuotation);
    data.put(USER_COMMENTS_KEY, decision.getComments());
    QEvents event = decision.getDecision().equalsIgnoreCase("yes") ? QEvents.ACCEPT : QEvents.REJECT;
    fireEvent(userQuotation.getState(), event, data);
  }

  // this should not be here.. move to payment section.. and payment has to be against the payment request
  @PostMapping(path = "/{id}/pay")
  public void pay(
      @PathVariable String id) throws Exception {
    logger.info("Payment requested for quote {}", id);
    Map<String, Object> data = new HashMap<String, Object>();
    Quotation userQuotation = this.quotationRepository.findOne(id);
    userQuotation = Objects.requireNonNull(userQuotation, "Quotation with id " + id + " not found.");
    data.put(QUOTATION_OBJ_KEY, userQuotation);
    fireEvent(userQuotation.getState(), QEvents.INITIATE_PAY, data);
  }

  @PostMapping(path = "/{id}/renew")
  public void renewQuotation(
      @PathVariable String id) throws Exception {
    logger.info("Renew quotation with id {}", id);
    Map<String, Object> data = new HashMap<String, Object>();
    Quotation userQuotation = this.quotationRepository.findOne(id);
    userQuotation = Objects.requireNonNull(userQuotation, "Quotation with id " + id + " not found.");
    data.put(QUOTATION_OBJ_KEY, userQuotation);
    fireEvent(userQuotation.getState(), QEvents.RENEW, data);
  }


  protected Message<QEvents> fireEvent(QStates source, QEvents event, Object data) throws Exception {
    Message<QEvents> msg = MessageBuilder.withPayload(event)
        .setHeader("DATA_KEY", data)
        .build();
    this.quotationHandler.handlEvent(msg, source);
    return msg;
  }

}
