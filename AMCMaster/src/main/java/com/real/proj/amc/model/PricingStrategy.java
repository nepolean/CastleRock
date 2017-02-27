package com.real.proj.amc.model;

import java.util.Date;

public abstract class PricingStrategy {

  public abstract double getPrice(UserInput<String, Object> input);

  public abstract void updatePrice(PriceData price, Date validFrom);

  public abstract double getPrice(UserInput<String, Object> input, Date on);

}
