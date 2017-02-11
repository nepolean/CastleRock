package com.real.proj.amc.service;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.real.proj.amc.model.Asset;
import com.real.proj.user.model.User;

@Repository
public interface AssetRepository extends MongoRepository<Asset, String> {

  public List<Asset> findByAssetOwner(User name);

  // public Asset findById(String assetId);

}
