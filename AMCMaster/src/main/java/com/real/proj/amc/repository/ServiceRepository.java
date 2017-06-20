package com.real.proj.amc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.real.proj.amc.model.Service;

public interface ServiceRepository
    extends MongoRepository<Service, String>, PagingAndSortingRepository<Service, String> {

}
