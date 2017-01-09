package com.real.proj.amc.service;

import com.real.proj.amc.model.Asset;

public interface IAgentAssetService {

  public Asset onBoardAsset(Asset asset);

  public Asset addPackage(Asset asset, Package service);

  public Asset[] getAssetsIOnboarded();

  public Asset[] getAssetsIOnBoardedByStatus(String status);

}
