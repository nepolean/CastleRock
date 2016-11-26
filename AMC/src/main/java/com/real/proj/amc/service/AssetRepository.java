package com.real.proj.amc.service;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.real.proj.amc.model.Asset;

public interface AssetRepository extends MongoRepository<Asset, String> {

}
