package com.real.proj.amc.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class Subscription {

  List<Package> packages;
  Price price;
  Date createdOn;
  Date subscribedOn;
  Quotation quotation;
  boolean isPaid;
  SubscriptionStatus status;

  public void raiseQuote(Asset asset, Coupon coupon, Set<Tax> taxes) {
    for (Package pkg : packages) {
      totalAmount += pkg.getPricing().getAmount();
    }
    try {
      discount = coupon.applyDiscount(totalAmount);
    } catch (InvalidCouponException e) {
      comments.add(e.getMessage());
    }
    double netAmount = totalAmount - discount;
    double taxAmount = 0.0;
    for (Tax tax : taxes) {
      taxAmount += tax.calcTax(netAmount);
    }
    finalAmount = netAmount + taxAmount;
  }

}
