package com.real.proj.amc.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author murali
 *         This interface is designed to satisfy the mongoDB requirement to
 *         have all the services together under a single collection name.
 *
 */
@Document(collection = "Products")
public interface Service extends Product {

  public abstract void setSubscriptionData(ServiceMetadata subcriptionData);

  public abstract void setOneTimeData(ServiceMetadata oneTimeData);

  public abstract ServiceType getServiceType();

}
