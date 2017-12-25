package com.subsede.amc.catalog.model;

import java.util.List;

public abstract class  ServiceMetadata {

  public abstract boolean isValid(List<String> errorHolder);

}
