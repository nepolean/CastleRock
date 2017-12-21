package com.subsede.amc.catalog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.subsede.amc.catalog.model.AMCPackage;

@Repository
public interface PackageRepository
    extends MongoRepository<AMCPackage, String>, PagingAndSortingRepository<AMCPackage, String> {

  // public abstract Page<AMCPackage> findByServicesServiceIdIn(Pageable
  // pageable, List<String> serviceID);

  @Query("{'isActive': true, 'isDeleted':false, '_id': {$in: ?0}}")
  public abstract List<AMCPackage> findValidProducts(String[] productIDs);

  @Query("{'isActive': true, 'isDeleted':false}}")
  public abstract Page<AMCPackage> findValidProducts(Pageable pageable);

}
