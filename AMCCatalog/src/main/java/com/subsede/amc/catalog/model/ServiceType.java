package com.subsede.amc.catalog.model;

import com.fasterxml.jackson.annotation.JsonValue;

public interface ServiceType {

  @JsonValue
  public abstract String getName();

  @JsonValue
  public abstract String[] getSkills();
}
