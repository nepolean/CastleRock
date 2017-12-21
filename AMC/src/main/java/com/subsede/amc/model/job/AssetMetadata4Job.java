package com.subsede.amc.model.job;

import com.subsede.amc.model.Asset;

public class AssetMetadata4Job implements JobMetadata {

  private Asset asset;
  private String uniqueId;
  private String name;

  public AssetMetadata4Job(Asset asset, String uniqueId, String name) {
    super();
    this.asset = asset;
    this.uniqueId = uniqueId;
    this.name = name;
  }

  public Asset getAsset() {
    return asset;
  }

  public void setAsset(Asset asset) {
    this.asset = asset;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
