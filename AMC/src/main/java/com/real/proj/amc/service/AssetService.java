package com.real.proj.amc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.real.proj.amc.model.Apartment;
import com.real.proj.amc.model.Asset;
import com.real.proj.user.model.User;
import com.real.proj.user.service.UserService;

@Service
public class AssetService {

  private AssetRepository assetRepository;
  private UserService userService;

  @Autowired
  public void setAssetRepository(AssetRepository repository, UserService userService) {
    this.assetRepository = repository;
    this.userService = userService;
  }

  public Asset createAsset(Asset asset, String loggedInUser) {
    User authorizedUser = userService.getUser(loggedInUser);
    asset.setCreatedBy(authorizedUser);
    return assetRepository.save(asset);
  }

  public Asset createApartment(String loggedInUser) {
    User authorizedUser = userService.getUser(loggedInUser);
    Asset newAsset = new Apartment(new Apartment.Details(100, 1000.0));
    newAsset.setCreatedBy(authorizedUser);
    Asset saved = assetRepository.save(newAsset);
    return saved;
  }

  public List<Asset> getMyAssets(String name) {
    return this.assetRepository.findByOwner(name);
  }
}
