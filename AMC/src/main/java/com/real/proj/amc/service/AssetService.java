package com.real.proj.amc.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.real.proj.amc.model.Asset;
import com.real.proj.user.model.User;
import com.real.proj.user.service.UserService;

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

}
