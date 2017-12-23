package com.subsede.amc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.subsede.amc.model.Asset;

@Repository
public interface AssetRepository extends MongoRepository<Asset, String> {

  public Page<Asset> findByAssetOwner(String name, Pageable page);

  public Page<Asset> findByCreatedBy(String createdBy, Pageable page);

  public Asset findByIdAndCreatedBy(String id, String createdBy);

  public Asset findByIdAndAssetOwner(String id, String username);

  // public Asset findById(String assetId);

}
