package com.real.proj.amc.model;

public abstract class SubscriptionMetadata extends ServiceMetadata {

  public abstract SubscriptionData getSubscriptionData(UserInput<String, Object> input);

}
