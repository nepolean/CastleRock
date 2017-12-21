package com.subsede.amc.catalog.model.deleted;

import java.io.Serializable;

public class SubscriptionBasedData implements Serializable {

  private static final long serialVersionUID = 1L;
  private double price;
  private int visits;

  public SubscriptionBasedData(double price, int visits) {
    this.price = price;
    this.visits = visits;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public int getVisits() {
    return visits;
  }

  public void setVisits(int visits) {
    this.visits = visits;
  }

  @Override
  public String toString() {
    return "SubscriptionBasedData [price=" + price + ", visits=" + visits + "]";
  }

}
