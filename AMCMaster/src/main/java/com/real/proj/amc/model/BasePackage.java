package com.real.proj.amc.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Packages")
public class BasePackage extends BaseMasterEntity {

  @Id
  private String id;
  @NotBlank
  private String name;
  @NotNull
  private List<? extends MaintenanceService> services;
  private Map<String, PackageVariant> variants;

  private boolean isActive;

  public BasePackage(String name, List<? extends MaintenanceService> services) {
    this.name = name;
    this.services = services;
    this.variants = new HashMap<String, PackageVariant>();
    isActive = false;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<? extends MaintenanceService> getServices() {
    return services;
  }

  public void setServices(List<MaintenanceService> services) {
    this.services = services;
  }

  public double getPricing(UserInput input) {
    // this is sigma of all services packaged hereunder.
    // return this.pricing.get(rating);
    if (this.services == null)
      throw new IllegalArgumentException();
    double price = 0.0;
    for (MaintenanceService service : this.services) {
      price += service.getPrice(input);
    }
    return price;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  static class PackageVariant {
    private String name;
    private int visitCount;
  }

}
