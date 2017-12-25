package com.subsede.amc.catalog.model;

public class Price {
  
  private double amount;
  private double discount;
  private double tax;
  public Price(double amount, double discount, double tax) {
    super();
    this.amount = amount;
    this.discount = discount;
    this.tax = tax;
  }
  public double getAmount() {
    return amount;
  }
  public void setAmount(double amount) {
    this.amount = amount;
  }
  public double getDiscount() {
    return discount;
  }
  public void setDiscount(double discount) {
    this.discount = discount;
  }
  public double getTax() {
    return tax;
  }
  public void setTax(double tax) {
    this.tax = tax;
  }
}
