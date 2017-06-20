package com.real.proj.amc.model;

public interface Product {

  public abstract String getName();

  public abstract String getType();

  public abstract boolean canSubscribe();

  public abstract boolean canRequestOneTime();

  public abstract SubscriptionData fetchSubscriptionData();

  public abstract OneTimeData fetchOneTimeData();

  public abstract SubscriptionData fetchSubscriptionData(UserInput<String, Object> input);

  public abstract OneTimeData fetchOneTimeData(UserInput<String, Object> input);

  public abstract Object getCategory();

  public abstract String getId();

}
