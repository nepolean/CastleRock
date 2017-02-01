package com.real.proj.amc.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.real.proj.user.model.User;

@Document
public class Asset implements java.io.Serializable {

  static final long serialVersionUID = 1L;

  @Id
  String id;
  protected AssetType type;
  Location location;
  // Asset belongsTo;
  User owner;
  User createdBy;
  Date createdOn;
  Set<AmenityMaster> amenities;
  List<MaintenanceService> services;
  Subscription subscription;
  Date lastModified;
  // private Set<Package> subscribedPackages;
  private Rating rating;

  public Asset() {
    this.createdOn = new Date();
    // this.location = new Location();
  }

  public Asset(User ownedBy, User createdBy) {
    this.owner = ownedBy;
    this.createdBy = createdBy;
    this.lastModified = new Date();

  }

  /*
   * public Asset getBelongsTo() { return belongsTo; }
   * 
   * public void setBelongsTo(Asset belongsTo) { this.belongsTo = belongsTo; }
   */
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
    if (this.subscription == null)
      return SubscriptionStatus.NONE;
    return this.subscription.getStatus();
  }

  /*********************** BUISINESS LOGIC *************************/
  public void subscribe() {

  }

  public void renew() {

  }

  public List<AMCPackage> getSubscribedPackages() {
    if (this.subscription == null)
      return null;
    return this.subscription.getsubscribedPackages();
  }

  public Rating getRating() {
    return rating;
  }

  public void setRating(Rating rating) {
    this.rating = rating;
  }

}
