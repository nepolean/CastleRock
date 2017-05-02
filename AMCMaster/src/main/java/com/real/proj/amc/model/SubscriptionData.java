package com.real.proj.amc.model;

public class SubscriptionData extends ServiceData {

  private static final long serialVersionUID = 1L;
  private int visitCount;
  private double subscriptionPrice;

  public SubscriptionData(double subscriptionPrice, double unitPrice, int visitCount) {
    super(unitPrice);
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

  @Override
  public String toString() {
    return "SubscriptionData [visitCount=" + visitCount + ", subscriptionPrice=" + subscriptionPrice + ", unitPrice="
        + price + "]";
  }

}
