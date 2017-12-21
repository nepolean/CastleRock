package com.subsede.amc.catalog.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Range;

public class SubscriptionData extends SubscriptionMetadata {

  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1L;
  @Min(value = 1, message = "The visit count must be at least 1.")
  private int visitCount;
  @Range
  @Min(value = 1, message = "Subscription price cannot be less than 1.")
  private double subscriptionPrice;
  @Range
  @Max(value = 100, message = "Discount percent cannot be > 100.")
  private double discountPct;

  public SubscriptionData() {

  }

  public SubscriptionData(double subscriptionPrice, int visitCount) {
    if (visitCount <= 0)
      throw new IllegalArgumentException("Invalid visit count");
    this.visitCount = visitCount;
    this.subscriptionPrice = subscriptionPrice;
  }

  public int getVisitCount() {
    return visitCount;
  }

  public void setVisitCount(int visitCount) {
    this.visitCount = visitCount;
  }

  public double getSubscriptionPrice() {
    return subscriptionPrice;
  }

  public void setSubscriptionPrice(double subscriptionPrice) {
    this.subscriptionPrice = subscriptionPrice;
  }

  public double getDiscount() {
    return this.subscriptionPrice * this.discountPct / 100;
  }

  public double getDiscountPct() {
    return discountPct;
  }

  public void setDiscountPct(double discountPct) {
    this.discountPct = discountPct;
  }

  @Override
  public String toString() {
    return "SubscriptionData [visitCount=" + visitCount + ", subscriptionPrice=" + subscriptionPrice + "]";
  }

  @Override
  public SubscriptionData getSubscriptionData(UserInput<String, Object> input) {
    return this;
  }

  @Override
  public boolean isValid(List<String> errorHolder) {
    errorHolder = Optional.of(errorHolder).orElse(new LinkedList<String>());
    int originalSize = errorHolder.size();
    if (this.visitCount < 1)
      errorHolder.add("Visit count cannot be less than 1.");
    if (this.subscriptionPrice < 1.0)
      errorHolder.add("Invalid subscription price provided.");
    if (this.discountPct > 100.0)
      errorHolder.add("Disoucnt percent cannot be > 100.");
    int currentSize = errorHolder.size();
    return currentSize > originalSize;
  }

}
