package com.subsede.amc.catalog.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public class OneTimeData extends OneTimeMetadata {

  @Range
  @Min(value = 1, message = "Price should not be less than 1.")
  private double price;
  
  @Range
  @Min(value = 0, message = "Discount should not be less than 0.")
  @Max(value = 100, message = "Discount should not be more than 100.")
  private double discountPct;
  @NotBlank (message = "The name cannot be empty")
  private String name;

  public OneTimeData() {

  }

  public OneTimeData(double price) {
    this.price = price;
  }

  public OneTimeData(double price, double discountPct) {
    this.price = price;
    this.discountPct = discountPct;
  }
  
  public OneTimeData(double price, double discountPct, String name) {
    this.price = price;
    this.discountPct = discountPct;
    this.name = name;
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
    return "OneTimeData [price=" + price + ", discountPct=" + discountPct + ", name=" + name + "]";
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

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

}
