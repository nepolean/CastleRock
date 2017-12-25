package com.subsede.amc.catalog.model;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Products")
public interface INonSubscriptionPackage extends Product {
  
  public interface IPackageVariant {
    String getName();

    double getPrice();
    
    String getUOM();

  }
  abstract Optional<Price> getPrice(String variant);

  abstract Map<String, IPackageVariant> getVariants();

  abstract Service getService();


}
