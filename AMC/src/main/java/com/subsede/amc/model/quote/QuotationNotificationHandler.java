package com.subsede.amc.model.quote;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class QuotationNotificationHandler {

  @Async
  public void handleQuotationApprovedByAdmin(Quotation userQuote) {
    // TODO Auto-generated method stub

  }

  @Async
  public void handleQuotationRejected(Quotation userQuote) {
    // TODO Auto-generated method stub

  }

  @Async
  public void handleSubscriptionCreated(com.subsede.amc.model.subscription.Subscription subscription) {
    // TODO Auto-generated method stub

  }

  @Async
  public void handleQuotationAcceptedByUser(Quotation userQuote) {
    // TODO Auto-generated method stub

  }

}
