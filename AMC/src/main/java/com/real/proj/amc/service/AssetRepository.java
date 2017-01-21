package com.real.proj.amc.service;

import java.util.List;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.real.proj.amc.model.Asset;

@EnableMongoRepositories
@Repository
public interface AssetRepository extends PagingAndSortingRepository<Asset, String> {

  public List<Asset> findByOwner(String name);

}
