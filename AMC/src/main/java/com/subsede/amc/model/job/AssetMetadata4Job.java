package com.subsede.amc.model.job;

import com.subsede.amc.model.Asset;

public class AssetMetadata4Job extends BaseMetadata implements ServiceMetadata {

  private Asset asset;
  public AssetMetadata4Job(Asset asset, String source, String uniqueId) {
    super(source, uniqueId);
    this.asset = asset;
  }

  public AssetMetadata4Job(Asset userAsset) {
    this.asset = userAsset;
  }

  public Asset getAsset() {
    return asset;
  }

  public void setAsset(Asset asset) {
    this.asset = asset;
  }

}
