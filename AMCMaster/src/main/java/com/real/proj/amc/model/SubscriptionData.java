package com.real.proj.amc.model;

public class SubscriptionData {

  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1L;
  private int visitCount;
  private double subscriptionPrice;
  private double discountPct;

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

}
