package com.real.proj.amc.model;

import java.io.Serializable;

public class ServiceData implements Serializable {

  private static final long serialVersionUID = 1L;

  protected double price;

  public ServiceData(double price) {
    if (price <= 0.0)
      throw new IllegalArgumentException("Invalid price.");
    this.price = price;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public boolean validate() {
    return price != 0;
  }

  @Override
  public String toString() {
    return "ServiceLevelData [price=" + price + "]";
  }

}
