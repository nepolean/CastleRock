package com.real.proj.amc.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BaseMasterEntity {

  @JsonIgnore
  protected Date createdOn;
  @JsonIgnore
  protected Date lastModified;
  @JsonIgnore
  protected String createdBy;
  @JsonIgnore
  protected String modifiedBy;
  protected boolean markForDeletion;

  BaseMasterEntity() {
    this.createdOn = new Date();
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
  }

  public void markForDeletion() {
    this.markForDeletion = true;
  }

  public boolean isDeleted() {
    return this.markForDeletion;
  }
}
