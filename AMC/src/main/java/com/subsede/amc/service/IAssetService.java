package com.subsede.amc.service;

import com.subsede.amc.model.Asset;
import com.subsede.user.model.user.UserRegistrationDTO;

public interface IAssetService {

  Asset createAsset(Asset asset);

  Asset createSubAsset(Asset asset, String parentId);

  void addOwner(String assetId, UserRegistrationDTO owner);

  void deleteOwner(String assetId, String userName);

}