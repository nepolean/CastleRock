package com.real.proj.amc.model;

import java.util.List;
import java.util.Set;

import com.real.proj.user.model.User;

public class Quotation {
  
  User customer;
  private Asset asset;
  double totalAmount;
  double taxAmount;
  double discount;
  double finalAmount;
  List<String> comments;
  
  public void raiseQuote (Asset asset, Set<SubscriptionPackage> packages, Coupon coupon, Set<Tax> taxes) {
    for (SubscriptionPackage pkg : packages) {
      totalAmount += pkg.getPrice();
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
