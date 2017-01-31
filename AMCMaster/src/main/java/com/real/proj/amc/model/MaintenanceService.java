package com.real.proj.amc.model;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;

public class MaintenanceService extends BaseMasterEntity {

  @Id
  private String id;
  @NotBlank
  private String name;
  @NotBlank
  private String description;

  private boolean isActive;

  Map<Rating, ServiceData> pricing;

  public MaintenanceService(String name, String description) {
    this.name = name;
    this.description = description;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public double getPrice(Rating rating) {
    return this.pricing == null ? 0 : this.pricing.get(rating).getPrice();
  }

  public int getVisitCount(Rating rating) {
    return this.pricing == null ? 0 : this.pricing.get(rating).getVisits();
  }

  public void addPricing(Rating rating, double price, int visits) {
    if (this.pricing == null)
      this.pricing = new HashMap<Rating, ServiceData>();
    ServiceData details = new ServiceData(visits, price);
    this.pricing.put(rating, details);
  }
}
