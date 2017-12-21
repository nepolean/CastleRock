package com.subsede.amc.catalog.model;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BaseMasterEntity {
  @JsonIgnore
  String id;
  @JsonIgnore
  protected Date createdOn;
  @JsonIgnore
  protected Date lastModified;
  @JsonIgnore
  protected String createdBy;
  @JsonIgnore
  protected String modifiedBy;
  // protected boolean markForDeletion;
  protected boolean isActive;
  protected boolean isDeleted;

  protected BaseMasterEntity() {
    this.createdBy = getLoggedInUser();
    this.createdOn = new Date();
    this.isActive = false;
    isDeleted = false;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public Date getLastModified() {
    return lastModified;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {

    this.modifiedBy = modifiedBy;
    this.lastModified = new Date();
  }

  public void markForDeletion() {
    this.isDeleted = true;
  }

  public boolean isDeleted() {
    return this.isDeleted;
  }

  public abstract String getId();

  private String getLoggedInUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null)
      if ("TEST".equalsIgnoreCase(System.getProperty("ENVIRONMENT")))
        return "TEST_USER";
      else
        throw new IllegalStateException("Security error. Session may have expired!");
    return auth.getName();
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }
}
