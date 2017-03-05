package com.real.proj.amc.model;

import java.io.Serializable;
import java.util.Date;

public class FixedPricingScheme extends PricingStrategy implements Serializable {

  public final static String NAME = "Fixed";

  TimeLine<FixedPrice> price = new TimeLine<FixedPrice>();

  public FixedPricingScheme() {
  }

  public FixedPricingScheme(FixedPrice price) {
    this(price, new Date());
  }

  public FixedPricingScheme(FixedPrice price, Date validFrom) {
    this.price.addToHistory(price, validFrom);
  }

  @Override
  public void setName(String name) {
    // this.name = "Fixed Price";
  }

  @Override
  public String getName() {
    return NAME;
  }

  public void setPrice(TimeLine<FixedPrice> price) {
    this.price = price;
  }

  public TimeLine<FixedPrice> getPrice() {
    return this.price;
  }

  @Override
  public double getPrice(UserInput<String, Object> input) {
    return this.price.getCurrentValue().getPrice();
  }

  @Override
  public double getPrice(UserInput<String, Object> input, Date on) {
    return this.price.getValueForDate(on).getPrice();
  }

  @Override
  public void updatePrice(PriceData price, Date validFrom) {
    if (price == null)
      throw new IllegalArgumentException("Null price data is passed");
    if (!(price instanceof FixedPrice))
      throw new IllegalArgumentException("Invalid price data TYPE is passed");
    this.price.addToHistory((FixedPrice) price, validFrom);
  }

  @Override
  public String toString() {
    return "FixedPricingScheme [price=" + price + "]";
  }

  public static class FixedPrice extends PriceData implements Serializable {

    double price;

    public FixedPrice(double price) {
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
      return "FixedPrice [price=" + price + "]";
    }

  }
}
