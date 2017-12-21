package com.subsede.amc.service;

import java.util.List;

import com.subsede.amc.catalog.model.AMCPackage;
import com.subsede.amc.catalog.model.PackageScheme;
import com.subsede.amc.catalog.model.asset.Amenity;
import com.subsede.amc.catalog.model.asset.AssetType;
import com.subsede.amc.model.Asset;
import com.subsede.amc.model.subscription.Subscription;
import com.subsede.util.user.model.User;

public interface IAgentAssetService {

  public Asset createAsset(Asset asset, Asset parent, User owner);

  // public void addAmenityDetailsToAsset(AmenityDetails details);

  public AMCPackage suggestAPackage(AssetType assetType, List<Amenity> amenities);

  public Subscription subscribe(
      String assetId,
      AMCPackage pkg,
      PackageScheme scheme);

  // public Order createOrder(String userId);

  public Asset[] getAssetsIOnBoarded();

  public Asset[] getAssetsIOnBoardedByStatus(String status);

}
