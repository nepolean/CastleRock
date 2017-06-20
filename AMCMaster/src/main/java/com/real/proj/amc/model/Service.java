package com.real.proj.amc.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Services")
public interface Service extends Product {

  public abstract void setSubscriptionData(ServiceMetadata subcriptionData);

  public abstract void setOneTimeData(ServiceMetadata oneTimeData);

}
