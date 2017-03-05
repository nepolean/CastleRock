package com.real.proj.amc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.real.proj.amc.model.Category;

public interface CategoryRepository
    extends MongoRepository<Category, String>, PagingAndSortingRepository<Category, String> {
  Page<Category> findByIsActiveTrue(Pageable pageable);

}
