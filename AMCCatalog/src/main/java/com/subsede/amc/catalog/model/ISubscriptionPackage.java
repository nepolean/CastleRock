package com.subsede.amc.catalog.model;

import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Products")
public interface ISubscriptionPackage extends Product {
  
  public Set<Service> getServices();
  
  public abstract SubscriptionData fetchSubscriptionData();
  
  public abstract SubscriptionData fetchSubscriptionData(UserInput<String, Object> input);

  public abstract TenureBasedDiscount getTenureBasedDisc();

  public void addService(Service service);
  
  public void removeService(Service service);

  public void setTenureBasedDisc(TenureBasedDiscount discount);

  public void setActive(boolean b);

  
}
