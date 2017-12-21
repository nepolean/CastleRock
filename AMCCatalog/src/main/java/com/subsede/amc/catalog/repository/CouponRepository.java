package com.subsede.amc.catalog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.subsede.amc.catalog.model.Coupon;

public interface CouponRepository extends MongoRepository<Coupon, String>, PagingAndSortingRepository<Coupon, String> {

  Page<Coupon> findByIsActiveTrue(Pageable pageable);

}
