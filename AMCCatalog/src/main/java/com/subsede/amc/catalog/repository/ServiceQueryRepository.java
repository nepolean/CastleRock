package com.subsede.amc.catalog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.subsede.amc.catalog.model.BaseService;

public interface ServiceQueryRepository extends MongoRepository<BaseService, String>,
                                                PagingAndSortingRepository<BaseService, String>{

}
