package com.real.proj.amc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.real.proj.amc.model.BasicService;

public interface ServiceRepository
    extends MongoRepository<BasicService, String>, PagingAndSortingRepository<BasicService, String> {

}
