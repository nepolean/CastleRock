package com.real.proj.amc.model;

public abstract class Price {

  private String id;
  private String name;
  private PricingStrategy strategy;
  private Package pkg;

  public enum PricingStrategy {
    RATING_BASED;
  }

  public abstract double getAmount();

}
