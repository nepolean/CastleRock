package com.real.proj.amc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.real.proj.amc.model.AMCPackage;

public interface PackageRepository
    extends MongoRepository<AMCPackage, String>, PagingAndSortingRepository<AMCPackage, String> {

  public abstract Page<AMCPackage> findByServicesServiceIdIn(Pageable pageable, List<String> serviceID);

}
