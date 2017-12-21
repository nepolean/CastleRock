package com.subsede.amc.catalog.model;

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

  /*
   * Returns the ServiceType this service belongs to. The ServiceType
   * provides information about the skills needed to perform the job
   * 
   */
  public abstract ServiceType getServiceType();

}
