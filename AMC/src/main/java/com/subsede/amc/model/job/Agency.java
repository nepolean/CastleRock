package com.subsede.amc.model.job;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import com.subsede.amc.model.Location;

@Document(collection = "Agencies")
public class Agency extends com.subsede.amc.catalog.model.BaseMasterEntity {

  @NotBlank(message = "Name should not be empty")
  private String name;
  @NotNull(message = "Address should not be empty")
  private Location address;
  private boolean isActive;
  private Set<AgencyAdmin> adminUsers;
  private Set<Technician> technicians;

  public Agency(String name, Location address) {
    this.name = name;
    this.address = address;
  }
  
  public String getId() {
    return id;
  }

  public Location getAddress() {
    return address;
  }

  public void setAddress(Location address) {
    this.address = address;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public boolean getActvie() {
    return isActive;
  }

  public void addAdmin(AgencyAdmin adminUser) {
    if (adminUsers == null)
      adminUsers = new HashSet<>();
    adminUsers.add(adminUser);
  }

  public void addTechnician(Technician technician) {
    if (technicians == null) {
      technicians = new HashSet<>();
    }
    technicians.add(technician);
  }

  public void removeTechnician(Technician technician) {
    if (technicians != null)
      technicians.remove(technician);
  }

}
