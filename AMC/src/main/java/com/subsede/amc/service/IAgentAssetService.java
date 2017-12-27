package com.subsede.amc.service;

import org.springframework.data.domain.Page;

import com.subsede.amc.model.Asset;

public interface IAgentAssetService {

  public Page<Asset> getAssetsIOnBoarded(String username);

}
