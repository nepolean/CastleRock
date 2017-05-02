package com.real.proj.amc.model;

import java.util.List;

public abstract class ServiceMetadata {

  public abstract boolean isValid(DeliveryMethod deliveryMethod, List<String> errorHolder);

  public abstract ServiceData getServiceData(UserInput<String, Object> input);

}
