package com.real.proj.amc.model;

import java.util.Date;
import java.util.Map;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.real.proj.user.model.User;

@Document
public class Asset implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  String id;
  @NotBlank
  String type;
  Asset belongsTo;
  @NotBlank
  String ownedBy;
  @NotBlank
  Map<String, String> details;
  User createdBy;
  Date createdOn;

  public Asset() {
    this.createdOn = new Date();
  }

  public Asset(String type, String ownedBy, Map details, User createdBy) {
    this.type = type;
    this.ownedBy = ownedBy;
    this.details = details;
    this.createdBy = createdBy;

  }

  public void setCreatedBy(User authorizedUser) {
    this.createdBy = authorizedUser;
  }

}
