package com.real.proj.amc.model;

import java.util.List;
import java.util.Set;

import com.real.proj.user.model.User;

public class Quotation {

  double totalAmount;
  double taxAmount;
  double discount;
  double finalAmount;
  List<String> comments;
  Asset belongsTo;
  User owner;
  User createdBy;

  public void raiseQuote(Asset asset, Coupon coupon, Set<Tax> taxes) {
    List<Package> packages = asset.getSubscribedPackages();
    for (Package pkg : packages) {
      totalAmount += pkg.getPricing(asset);
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
