package com.real.proj.amc.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BaseMasterEntity implements PropertyChangeListener {

  Object id;
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
    this.createdBy = getLoggedInUser();
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
    this.lastModified = new Date();
  }

  public void markForDeletion() {
    this.markForDeletion = true;
  }

  public boolean isDeleted() {
    return this.markForDeletion;
  }

  public Object getId() {
    return this.id;
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    if (event.getPropertyName().matches("(modifiedBy|lastModified)"))
      return;
    this.modifiedBy = getLoggedInUser();
    this.lastModified = new Date();
  }

  private String getLoggedInUser() {
    return SecurityContextHolder.getContext().getAuthentication() == null ? "TEST_USER"
        : SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
