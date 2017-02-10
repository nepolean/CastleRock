package com.real.proj.amc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.real.proj.amc.model.Apartment;
import com.real.proj.amc.model.Asset;
import com.real.proj.amc.model.UOM;
import com.real.proj.controller.exception.EntityNotFoundException;
import com.real.proj.user.model.User;
import com.real.proj.user.service.UserService;

@Service
public class AssetService implements IAgentAssetService {

  @Autowired
  private AssetRepository assetRepository;
  @Autowired
  private UserService userService;

  public Asset createAsset(Asset asset, String loggedInUser) {
    User authorizedUser = userService.getUser(loggedInUser);
    // asset.setCreatedBy(authorizedUser);
    return assetRepository.save(asset);
  }

  public Asset createApartment(String loggedInUser) {
    User authorizedUser = userService.getUser(loggedInUser);
    Asset newAsset = new Apartment(new Apartment.Details(100, 2, 1000.0, UOM.SFT));
    // newAsset.setCreatedBy(authorizedUser);
    Asset saved = assetRepository.save(newAsset);
    return saved;
  }

  public List<Asset> getMyAssets(String email) {
    User owner = userService.getUser(email);
    return this.assetRepository.findByOwner(owner);
  }

  @Override
  public Asset onBoardAsset(Asset asset) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Asset addPackage(Asset asset, Package service) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
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
}
