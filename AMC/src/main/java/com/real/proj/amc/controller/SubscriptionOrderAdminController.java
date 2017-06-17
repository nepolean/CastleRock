package com.real.proj.amc.controller;

import static com.real.proj.amc.model.quote.QuotationConstants.QUOTATION_OBJ_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.USER_COMMENTS_KEY;
import static com.real.proj.amc.model.quote.QuotationConstants.USER_DATA_KEY;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.amc.controller.pojo.UserDecision;
import com.real.proj.amc.model.UserData;
import com.real.proj.amc.model.quote.QEvents;
import com.real.proj.amc.model.quote.Quotation;

@RestController
@RequestMapping("/api/v1/admin")
@Secured("ROLE_ADMIN")
public class SubscriptionOrderAdminController extends SubscriptionOrderController {
  private static Logger logger = LoggerFactory.getLogger(SubscriptionOrderAdminController.class);

  @RequestMapping(path = "/quotation/{id}/regenerate", method = RequestMethod.POST, consumes = "application/json")
  public void reGenerateQuotation(@PathVariable String id, @RequestBody @Validated UserData userData) throws Exception {
    logger.info("Genereate quotation requested for {}", id);
    userData = Objects.requireNonNull(userData, "UserData cannot be null");
    Map<String, Object> data = new HashMap<String, Object>();
    Quotation userQuotation = this.quotationRepository.findOne(id);
    userQuotation = Objects.requireNonNull(userQuotation, "Quotation with id " + id + " not found.");
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
    Quotation userQuotation = this.quotationRepository.findOne(id);
    userQuotation = Objects.requireNonNull(userQuotation, "Quotation with id " + id + " not found.");
    data.put(QUOTATION_OBJ_KEY, userQuotation);
    data.put(USER_COMMENTS_KEY, decision.getComments());
    QEvents event = decision.getDecision().equalsIgnoreCase("yes") ? QEvents.APPROVE : QEvents.REJECT;
    fireEvent(userQuotation.getState(), event, data);
  }

}
