package com.real.proj.amc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.real.proj.amc.model.Tax;

public interface TaxRepository extends MongoRepository<Tax, String>, PagingAndSortingRepository<Tax, String> {

  Page<Tax> findByIsActiveTrue(Pageable pageable);

  List<Tax> findByType(String string);

}
