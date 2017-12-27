package com.subsede.amc.controller.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.subsede.amc.model.Asset;

public class AssetDTO {
  
  @NotBlank(message = "Owner should not be blank")
  private String owner;
  
  @NotNull(message = "Asset should not be null")
  private Asset asset;
  
  @NotBlank(message = "AssetType should not be empty")
  private String type;
  
  public AssetDTO() {
    
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public Asset getAsset() {
    return asset;
  }

  public void setAsset(Asset asset) {
    this.asset = asset;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
  
  
}
