package com.real.proj.amc.model;

import java.util.Date;

public abstract class PricingStrategy {

  public abstract String getName();

  public abstract void setName(String name);

  public abstract ServiceData getServiceLevelData(UserInput<String, Object> input);

  public abstract void updatePrice(PriceData price, Date validFrom);

  public abstract ServiceData getServiceLevelData(UserInput<String, Object> input, Date on);

  public abstract ServiceData getDefaultServiceLevelData();

}
