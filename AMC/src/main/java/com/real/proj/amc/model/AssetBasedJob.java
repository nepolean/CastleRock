package com.real.proj.amc.model;

public class AssetBasedJob extends AbstractJob {

  private Asset asset;

  public AssetBasedJob(
      String name,
      Asset asset,
      ServiceType serviceType,
      String sourceType,
      String sourceId) {
    super(name, serviceType, sourceType, sourceId, asset.getOwner());
    this.asset = asset;
  }

  public Asset getAsset() {
    return asset;
  }

  public void setAsset(Asset asset) {
    this.asset = asset;
  }

}
