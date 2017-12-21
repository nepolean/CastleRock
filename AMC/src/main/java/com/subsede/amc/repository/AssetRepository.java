package com.subsede.amc.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.subsede.amc.model.Asset;
import com.subsede.util.user.model.User;

@Repository
public interface AssetRepository extends MongoRepository<Asset, String> {

  public List<Asset> findByAssetOwner(User name);

  // public Asset findById(String assetId);

}
