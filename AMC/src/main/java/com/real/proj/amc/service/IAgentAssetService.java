package com.real.proj.amc.service;

import java.util.List;

import com.real.proj.amc.model.AMCPackage;
import com.real.proj.amc.model.Asset;
import com.real.proj.amc.model.PackageScheme;
import com.real.proj.amc.model.asset.Amenity;
import com.real.proj.amc.model.asset.AssetType;
import com.real.proj.amc.model.subscription.Subscription;
import com.real.proj.user.model.User;

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
