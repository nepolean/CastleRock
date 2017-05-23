package com.real.proj.amc.model;

public interface Product {

  public abstract String getName();

  public abstract String getType();

  public abstract boolean canSubscribe();

  public abstract boolean canRequestOneTime();

  public abstract SubscriptionData getSubscriptionData();

  public abstract OneTimeData getOneTimeData();

  public abstract SubscriptionData getSubscriptionData(UserInput<String, Object> input);

  public abstract OneTimeData getOneTimeData(UserInput<String, Object> input);

}
