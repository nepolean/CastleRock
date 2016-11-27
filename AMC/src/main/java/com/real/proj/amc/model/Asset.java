package com.real.proj.amc.model;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.real.proj.user.model.User;

@Document
public class Asset implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  String id;
  Location location;
  Asset belongsTo;
  @NotBlank
  String owner;
  User createdBy;
  Date createdOn;

  public Asset() {
    this.createdOn = new Date();
  }

  public Asset(String ownedBy, User createdBy) {
    this.owner = ownedBy;
    this.createdBy = createdBy;

  }

  public Asset getBelongsTo() {
    return belongsTo;
  }

  public void setBelongsTo(Asset belongsTo) {
    this.belongsTo = belongsTo;
  }

  public String getOwnedBy() {
    return owner;
  }

  public void setOwnedBy(String ownedBy) {
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

}
