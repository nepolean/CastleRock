package com.real.proj.amc.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Range;

public class OneTimeData extends OneTimeMetadata implements Serializable {

  private static final long serialVersionUID = 1L;
  @Range
  @Min(value = 1, message = "Price cannot be less than 1.")
  private double price;
  @Range
  @Min(value = 0, message = "Discount cannot be < 0.")
  @Max(value = 100, message = "Discount cannot be > 100.")
  private double discountPct;

  public OneTimeData() {

  }

  public OneTimeData(double price) {
    this.price = price;
  }

  public OneTimeData(double price, double discountPct) {
    this.price = price;
    this.discountPct = discountPct;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getDiscountPct() {
    return discountPct;
  }

  public void setDiscountPct(double discountPct) {
    this.discountPct = discountPct;
  }

  @Override
  public String toString() {
    return "OneTimeData [price=" + price + ", discountPct=" + discountPct + "]";
  }

  @Override
  public OneTimeData getOneTimeData(UserInput<String, Object> input) {
    return this;
  }

  @Override
  public boolean isValid(List<String> errorHolder) {
    errorHolder = Optional.of(errorHolder).orElse(new LinkedList<String>());
    int originalSize = errorHolder.size();
    if (this.price < 1)
      errorHolder.add("Price cannot be less than 1.");
    int currentSize = errorHolder.size();
    return currentSize > originalSize;
  }

}
