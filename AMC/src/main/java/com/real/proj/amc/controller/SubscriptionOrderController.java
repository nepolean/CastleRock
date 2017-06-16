package com.real.proj.amc.controller;

import static com.real.proj.amc.model.quote.QuotationConstants.ASSET_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.USER_KEY;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.amc.model.Location;
import com.real.proj.amc.model.quote.NewQuoteInput;
import com.real.proj.amc.model.quote.QEvents;
import com.real.proj.amc.model.quote.QStates;
import com.real.proj.amc.model.quote.QuotationConstants;
import com.real.proj.amc.model.quote.QuotationStateHandler;
import com.real.proj.util.SecurityHelper;

@RestController
public class SubscriptionOrderController {

  private static Logger logger = LoggerFactory.getLogger(SubscriptionOrderController.class);

  SecurityHelper secHelper;
  QuotationStateHandler quotationHandler;

  @Autowired
  public void setSecurityHelper(SecurityHelper secHelper) {
    this.secHelper = secHelper;
  }

  @Autowired
  public void setQuotationStateHandler(QuotationStateHandler handler) {
    this.quotationHandler = handler;
  }

  @RequestMapping(path = "/quotation", method = RequestMethod.POST, consumes = "application/json")
  public void createNewSubscription(
      @RequestBody @Validated NewQuoteInput userInput) throws Exception {
    logger.info("New quotation requested for {}", userInput);
    Map<String, Object> data = new HashMap<String, Object>();
    data.put(USER_KEY, userInput.getMobileNo());
    data.put(ASSET_KEY, userInput.getAssetId());
    data.put(QuotationConstants.PRODUCTS_KEY, userInput.getProductID());
    fireEvent(QStates.INITIAL, QEvents.CREATE, data);
  }

  private void fireEvent(QStates source, QEvents event, Object data) throws Exception {
    Message<QEvents> msg = MessageBuilder.withPayload(event)
        .setHeader("DATA_KEY", data)
        .build();
    this.quotationHandler.handlEvent(msg, source);
  }

  public void createNewSubscription(@RequestBody @Valid Location location, Set<String> productIds) {
    logger.info("New quoatation requested for {}", this.secHelper.getLoggedInUser().getEmail());
  }

}
