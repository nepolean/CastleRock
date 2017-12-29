package com.subsede.amc.model.job;

import java.util.List;

import com.subsede.amc.catalog.model.ServiceType;
import com.subsede.amc.model.Asset;

public class AssetBasedJob extends AbstractJob {

  private Asset asset;

  public AssetBasedJob(
      String name,
      Asset asset,
      List<String> serviceType,
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
