package com.subsede.amc.catalog.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.subsede.amc.catalog.model.ISubscriptionPackage;
import com.subsede.amc.catalog.model.Product;

@Repository
public interface PackageRepository<T extends Product>
    extends MongoRepository<T, String>, PagingAndSortingRepository<T, String> {

  // public abstract Page<AMCPackage> findByServicesServiceIdIn(Pageable
  // pageable, List<String> serviceID);

  @Query("{'isActive': true, 'isDeleted':false, '_id': {$in: ?0}}")
  public abstract List<T> findValidProducts(Set<String> productIDs);

  @Query("{'isActive': true, 'isDeleted':false}}")
  public abstract Page<T> findValidProducts(Pageable pageable);

  @Query("{'isActive': true, 'isDeleted':false, '_id': ?0}")
  public abstract T findValidProduct(String pkgId);

  @Query("{'isActive': true, 'isDeleted':false, '_id': {$in: ?0}}")
  public abstract List<ISubscriptionPackage> findValidProducts(String[] selectedPackages);

  //public abstract Page<?> findByClass(String name, Pageable page);

  public abstract Page<?> findByType(String name, Pageable page);

}
