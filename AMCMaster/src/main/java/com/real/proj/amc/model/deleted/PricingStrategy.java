package com.real.proj.amc.model.deleted;

import java.util.Date;

import com.real.proj.amc.model.UserInput;

public abstract class PricingStrategy {

  public abstract String getName();

  public abstract void setName(String name);

  public abstract ServiceData getServiceLevelData(UserInput<String, Object> input);

  public abstract void updatePrice(PriceData price, Date validFrom);

  public abstract ServiceData getServiceLevelData(UserInput<String, Object> input, Date on);

  public abstract ServiceData getDefaultServiceLevelData();

}
