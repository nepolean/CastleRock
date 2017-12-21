package com.subsede.amc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.subsede.amc.model.job.Agency;

public interface AgencyRepository extends MongoRepository<Agency, String>, PagingAndSortingRepository<Agency, String> {

}
