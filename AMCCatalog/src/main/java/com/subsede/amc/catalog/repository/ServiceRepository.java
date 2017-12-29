package com.subsede.amc.catalog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.subsede.amc.catalog.model.BaseService;
import com.subsede.amc.catalog.model.Service;

public interface ServiceRepository<T extends Service>
    extends MongoRepository<T, String>, 
            PagingAndSortingRepository<T, String> {

  @Query("{'isActive': true, 'isDeleted':false}}")
  public Page<T> findValidServices(Pageable page);

  @Query("{'isActive': true, 'isDeleted':false, '_id': {$in: ?0}}")
  public List<T> findValidServices(String[] selectedServices);
  
  public List<T> findAll();

}
