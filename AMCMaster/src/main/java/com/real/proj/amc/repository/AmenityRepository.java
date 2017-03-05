package com.real.proj.amc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.real.proj.amc.model.Amenity;

public interface AmenityRepository
    extends MongoRepository<Amenity, String>, PagingAndSortingRepository<Amenity, String> {
  Page<Amenity> findByIsActiveTrue(Pageable pageable);

}
