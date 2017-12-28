package com.subsede.amc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.subsede.amc.model.Asset;

@Repository
public interface AssetRepository 
              extends MongoRepository<Asset, String>,
                      PagingAndSortingRepository<Asset, String> {

  public Page<Asset> findByAssetOwner(String name, Pageable page);

  public Page<Asset> findByCreatedBy(String createdBy, Pageable page);

  public Asset findByIdAndCreatedBy(String id, String createdBy);

  public Asset findByIdAndAssetOwner(String id, String username);

  public List<Asset> findByParentId(String id);

  // public Asset findById(String assetId);

}
