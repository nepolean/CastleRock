package com.real.proj.amc.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.real.proj.user.model.User;

@Document
public class Asset implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  String id;
  protected AssetType type;
  Location location;
  @DBRef
  Asset belongsTo;
  @DBRef
  User owner;
  User createdBy;
  Date createdOn;
  Set<AmenityMaster> amenities;
  List<MaintenanceService> services;
  SubscriptionStatus status;
  Date lastModified;

  private Set<SubscriptionPackage> subscribedPackages;

  public Asset() {
    this.createdOn = new Date();
    this.location = new Location();
  }

  public Asset(User ownedBy, User createdBy) {
    this.owner = ownedBy;
    this.createdBy = createdBy;
    this.lastModified = new Date();
    status = SubscriptionStatus.CREATED;

  }

  public Asset getBelongsTo() {
    return belongsTo;
  }

  public void setBelongsTo(Asset belongsTo) {
    this.belongsTo = belongsTo;
  }

  public User getOwnedBy() {
    return owner;
  }

  public void setOwnedBy(User ownedBy) {
    this.owner = ownedBy;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User authorizedUser) {
    this.createdBy = authorizedUser;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public Set<AmenityMaster> getAmenities() {
    return amenities;
  }

  public void setAmenities(Set<AmenityMaster> amenities) {
    this.amenities = amenities;
  }

  public List<MaintenanceService> getServices() {
    return services;
  }

  public void setServices(List<MaintenanceService> services) {
    this.services = services;
  }

  public SubscriptionStatus getStatus() {
    return status;
  }

  public void setStatus(SubscriptionStatus status) {
    this.status = status;
  }
  
  /*********************** BUISINESS LOGIC *************************/
  public void subscribe() {
    
  }
  
  public void renew() {
    
  }

  public Set<SubscriptionPackage> getSubscribedPackages() {
    return this.subscribedPackages;
  }

}
