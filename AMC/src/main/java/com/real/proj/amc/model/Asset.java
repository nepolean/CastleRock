package com.real.proj.amc.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

import com.real.proj.user.model.User;

@Document(collection = "Assets")
public class Asset extends BaseMasterEntity implements java.io.Serializable {

  static final long serialVersionUID = 1L;

  @Id
  String id;
  protected AssetType type;
  @Reference
  Location location;
  @Reference
  Asset belongsTo;
  User assetOwner;
  Set<AmenityMaster> amenities;
  List<MaintenanceService> services;
  // Subscription subscription;

  public Asset() {
    // this.location = new Location();
  }

  public Asset(User ownedBy, User createdBy) {
    this.assetOwner = ownedBy;
  }

  public User getOwnedBy() {
    return assetOwner;
  }

  public void setOwnedBy(User ownedBy) {
    this.assetOwner = ownedBy;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public User getOwner() {
    return assetOwner;
  }

  public void setOwner(User owner) {
    this.assetOwner = owner;
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

  /*********************** BUISINESS LOGIC *************************/
  public void subscribe() {

  }

  /*
   * public List<AMCPackage> getSubscribedPackages() { if (this.subscription ==
   * null) return null; return this.subscription.getsubscribedPackages(); }
   */

}
