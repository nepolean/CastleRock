package com.subsede.amc.catalog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.subsede.amc.catalog.model.BaseService;

public interface ServiceQueryRepository extends MongoRepository<BaseService, String>,
                                                PagingAndSortingRepository<BaseService, String>{

  Page<BaseService> findByIsActive(boolean b, Pageable pageable);

  Page<BaseService> findByCategory(String cat, Pageable pageable);

  Page<BaseService> findByCategoryAndIsActive(String cat, boolean b, Pageable pageable);

  Page<BaseService> findByCategoryAndIsActiveAndCanSubscribe(String cat, boolean b, boolean c, Pageable pageable);

  Page<BaseService> findByCategoryAndIsActiveAndCanRequestOneTime(String cat, boolean b, boolean c, Pageable pageable);

}
