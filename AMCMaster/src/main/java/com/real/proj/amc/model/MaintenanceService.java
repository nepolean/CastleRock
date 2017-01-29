package com.real.proj.amc.model;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;

public class MaintenanceService {
  
  @Id
  private String id;
  @NotBlank
  private String name;
  @NotBlank
  private String description;
  
  private boolean isActive;
  
  
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
  
  
  
  
}
