package com.subsede.amc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.subsede.amc.catalog.model.AMCPackage;
import com.subsede.amc.catalog.model.PackageScheme;
import com.subsede.amc.catalog.model.asset.Amenity;
import com.subsede.amc.catalog.model.asset.AssetType;
import com.subsede.amc.model.Apartment;
import com.subsede.amc.model.Asset;
import com.subsede.amc.model.UOM;
import com.subsede.amc.model.subscription.Subscription;
import com.subsede.amc.repository.AssetRepository;
import com.subsede.user.model.user.User;
import com.subsede.user.services.user.UserService;
import com.subsede.util.controller.exception.EntityNotFoundException;

@Service
public class AssetService implements IAgentAssetService {

  @Autowired
  private AssetRepository assetRepository;
  @Autowired
  private UserService userService;

  public Asset createAsset(Asset asset, String loggedInUser) {
    User authorizedUser = userService.findByUsername(loggedInUser);
    // asset.setCreatedBy(authorizedUser);
    return assetRepository.save(asset);
  }

  public Asset createApartment(String loggedInUser) {
    User authorizedUser = userService.findByUsername(loggedInUser);
    Asset newAsset = new Apartment(new Apartment.Details(100, 2, 1000.0, UOM.SFT));
    // newAsset.setCreatedBy(authorizedUser);
    Asset saved = assetRepository.save(newAsset);
    return saved;
  }

  public List<Asset> getMyAssets(String email) {
    User owner = userService.findByUsername(email);
    return this.assetRepository.findByAssetOwner(owner);
  }

  public Asset onBoardAsset(Asset asset) {
    // TODO Auto-generated method stub
    return null;
  }

  public Asset addPackage(Asset asset, Package service) {
    // TODO Auto-generated method stub
    return null;
  }

  public Asset[] getAssetsIOnboarded() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Asset[] getAssetsIOnBoardedByStatus(String status) {
    // TODO Auto-generated method stub
    return null;
  }

  public Asset getAssetById(String assetId) {
    Asset asset = this.assetRepository.findOne(assetId);
    if (asset == null)
      throw new EntityNotFoundException();
    return asset;
  }

  @Override
  public Asset createAsset(Asset asset, Asset parent, User owner) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AMCPackage suggestAPackage(AssetType assetType, List<Amenity> amenities) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Subscription subscribe(
      String assetId,
      AMCPackage pkg,
      PackageScheme scheme) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Asset[] getAssetsIOnBoarded() {
    // TODO Auto-generated method stub
    return null;
  }
}
