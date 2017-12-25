package com.subsede.amc.controller;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subsede.amc.catalog.model.INonSubscriptionPackage;

public class NonSubscriptionOrderDTO {
  @NotBlank(message = "Package Id should not be empty")
  String pkgId;
  @NotBlank(message = "Variant should not be empty")
  String variant;
  
  @JsonIgnore
  INonSubscriptionPackage pkg;
  @JsonIgnore
  INonSubscriptionPackage.IPackageVariant pkgVariant;
  
  public NonSubscriptionOrderDTO() {
    
  }

  public String getPkgId() {
    return pkgId;
  }

  public void setPkgId(String pkgId) {
    this.pkgId = pkgId;
  }

  public String getVariant() {
    return variant;
  }

  public void setVariant(String variant) {
    this.variant = variant;
  }
  
  @JsonIgnore
  public INonSubscriptionPackage getPkg() {
    return pkg;
  }

  public void setPkg(INonSubscriptionPackage pkg) {
    this.pkg = pkg;
  }

  @JsonIgnore
  public INonSubscriptionPackage.IPackageVariant getPkgVariant() {
    return pkgVariant;
  }

  public void setPkgVariant(INonSubscriptionPackage.IPackageVariant pkgVariant) {
    this.pkgVariant = pkgVariant;
  }
  
  public String toString() {
    return pkgId + ":" + variant;
  }
  
  
 }
