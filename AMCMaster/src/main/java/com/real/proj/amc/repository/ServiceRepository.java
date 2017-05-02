package com.real.proj.amc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.real.proj.amc.model.AssetBasedService;

public interface ServiceRepository
    extends MongoRepository<AssetBasedService, String>, PagingAndSortingRepository<AssetBasedService, String> {

}
