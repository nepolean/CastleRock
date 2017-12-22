package com.subsede.amc.model;

import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

import com.subsede.amc.catalog.model.BaseMasterEntity;
import com.subsede.amc.catalog.model.asset.Amenity;
import com.subsede.amc.catalog.model.asset.AssetBasedService;
import com.subsede.amc.catalog.model.asset.AssetType;
import com.subsede.user.model.Customer;

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
  Customer assetOwner;
  Set<Amenity> amenities;

  // Subscription subscription;

  public Asset() {
    // this.location = new Location();
  }

  public Asset(Customer ownedBy) {
    this.assetOwner = ownedBy;
  }

  public String getId() {
    return id;
  }

  public Customer getOwnedBy() {
    return assetOwner;
  }

  public void setOwnedBy(Customer ownedBy) {
    this.assetOwner = ownedBy;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Customer getOwner() {
    return assetOwner;
  }

  public void setOwner(Customer owner) {
    this.assetOwner = owner;
  }

  public Set<Amenity> getAmenities() {
    return amenities;
  }

  public void setAmenities(Set<Amenity> amenities) {
    this.amenities = amenities;
  }

  public void setServices(List<AssetBasedService> services) {
    // TODO Auto-generated method stub

  }

  public AssetType getType() {
    return this.getType();
  }

  public void setType(AssetType type) {
    this.type = type;
  }

}
