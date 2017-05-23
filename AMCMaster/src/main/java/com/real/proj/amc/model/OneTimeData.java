package com.real.proj.amc.model;

import java.io.Serializable;

public class OneTimeData implements Serializable {

  private static final long serialVersionUID = 1L;
  private double price;

  public OneTimeData(double price) {
    this.price = price;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "OneTimeData [price=" + price + "]";
  }

}
