package com.real.proj.amc.model;

public abstract class OneTimeMetadata extends ServiceMetadata {

  public abstract OneTimeData getOneTimeData(UserInput<String, Object> input);

}
