package com.real.proj.amc.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class Package {

  @Id
  private String id;
  private String name;
  private List<MaintenanceService> services;
  // private List<Price> pricing;
  private boolean isActive;

  public Package(String name, List<MaintenanceService> services, List<Price> pricing) {
    this.name = name;
    this.services = services;
    this.pricing = pricing;
    isActive = true;
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

  public List<MaintenanceService> getServices() {
    return services;
  }

  public void setServices(List<MaintenanceService> services) {
    this.services = services;
  }

  public Price getPricing() {
    return pricing;
  }

  public void setPricing(Price pricing) {
    this.pricing = pricing;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

}
