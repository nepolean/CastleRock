package com.real.proj.amc.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;

public class Package {

  @Id
  private String id;
  @NotBlank
  private String name;
  @NotNull
  private List<MaintenanceService> services;
  // @NotNull
  // private Map<Rating, Double> pricing;
  private boolean isActive;

  public Package(String name, List<MaintenanceService> services) {
    this.name = name;
    this.services = services;
    // this.pricing = pricing;
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

  /*
   * public Map<Rating, Double> getPricing() { return pricing; }
   */

  public double getPricing(Rating rating) {
    // Rating rating = asset.getRating();
    // this is sigma of all services packaged hereunder.
    // return this.pricing.get(rating);
    if (this.services == null)
      return 0;
    double price = 0.0;
    for (MaintenanceService service : this.services) {
      price += service.getPrice(rating);
    }
    return price;
  }

  /*
   * public void setPricing(Map<Rating, Double> amount) { this.pricing = amount;
   * }
   */

  /*
   * public void addPrice(MaintenanceService service, Rating rating, double
   * amount) {
   * 
   * }
   */

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

}
