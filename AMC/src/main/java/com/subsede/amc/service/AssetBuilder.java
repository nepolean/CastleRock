package com.subsede.amc.service;

import java.util.List;

import com.subsede.util.amc.model.Asset;
import com.subsede.util.amc.model.AssetBasedService;
import com.subsede.util.amc.model.Location;
import com.subsede.util.user.model.User;

public class AssetBuilder {

  private Asset asset;
  private AssetBuilder self;

  public AssetBuilder() {
    self = new AssetBuilder();
    this.asset = new Asset();
  }

  public AssetBuilder withBasicDetails(Location location, User owner, User agent) {
    // assertNotNull(asset);
    if (asset == null)
      throw new NullPointerException();
    asset.setOwner(owner);
    // asset.setCreatedBy(agent);
    self.asset = asset;
    return self;
  }

  public AssetBuilder withServices(List<AssetBasedService> services) {
    self.asset.setServices(services);
    return self;
  }

  public AssetBuilder withSubscription(List<Package> packages) {
    return null;
  }

}
