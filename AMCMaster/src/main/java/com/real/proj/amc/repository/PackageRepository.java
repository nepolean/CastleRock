package com.real.proj.amc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.real.proj.amc.model.AMCPackage;

public interface PackageRepository
    extends MongoRepository<AMCPackage, String>, PagingAndSortingRepository<AMCPackage, String> {

  // public abstract Page<AMCPackage> findByServicesServiceIdIn(Pageable
  // pageable, List<String> serviceID);

  @Query("{'isActive': true, 'isDeleted':false, '_id': {$in: ?0}}")
  public abstract Iterable<AMCPackage> findValidProducts(String[] productIDs);

  @Query("{'isActive': true, 'isDeleted':false}}")
  public abstract Page<AMCPackage> findValidProducts(Pageable pageable);

}
