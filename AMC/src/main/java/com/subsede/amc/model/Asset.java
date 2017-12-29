package com.subsede.amc.model;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.subsede.amc.catalog.model.BaseMasterEntity;
import com.subsede.amc.catalog.model.asset.Amenity;
import com.subsede.amc.catalog.model.asset.AssetType;
import com.subsede.user.model.Customer;


@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Apartment.class, name = "Apartment"),
    @JsonSubTypes.Type(value = Flat.class, name = "Flat")
})
@Document(collection = "Assets")
public class Asset extends BaseMasterEntity {

  @NotBlank(message = "Asset name should not be blank")
  private String name;
  @NotNull(message = "Asset Type should not be blank")
  protected AssetType type;
  @Reference
  @NotNull(message = "Address should not be empty")
  Location location;
  String parentId;
  @Reference
  List<Customer> assetOwner;
  Set<Amenity> amenities;
  List<String> imagePaths;
  String backgroundImage;
  
  public Asset() {
    
  }

  public Asset(String name, Location location, AssetType type) {
    this.name = name;
    this.location = location;
  }

  public String getId() {
    return id;
  }

  public Customer getOwner() {
    if(assetOwner != null)
      for(Customer owner : assetOwner)
        if (owner.isPrimary())
          return owner;
    return null;
  }

  public void addOwner(Customer owner) {
    if (assetOwner == null)
      assetOwner = new ArrayList<>();
    this.assetOwner.add(owner);
  }

  public void removeOwner(String owner) {
    if (assetOwner == null)
      return;
    this.assetOwner.removeIf(obj -> obj.equals(owner));
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Set<Amenity> getAmenities() {
    return amenities;
  }

  public void setAmenities(Set<Amenity> amenities) {
    this.amenities = amenities;
  }

  public AssetType getType() {
    return this.type;
  }

  public void setType(AssetType type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Customer> getAssetOwner() {
    return assetOwner;
  }

  public void setAssetOwner(List<Customer> assetOwner) {
    this.assetOwner = assetOwner;
  }

  public List<String> getImagePaths() {
    return imagePaths;
  }

  public void setImagePaths(List<String> imagePaths) {
    this.imagePaths = imagePaths;
  }

  public String getBackgroundImage() {
    return backgroundImage;
  }

  public void setBackgroundImage(String backgroundImage) {
    this.backgroundImage = backgroundImage;
  }

  public void setParent(String parent) {
    this.parentId = parent;
  }
  
  public String getParent() {
    return this.parentId;
  }
  
  public boolean isCommunityBased() {
    return Objects.isNull(this.parentId);
  }
  
}
