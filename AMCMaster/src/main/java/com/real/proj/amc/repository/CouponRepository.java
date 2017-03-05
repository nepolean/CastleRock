package com.real.proj.amc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.real.proj.amc.model.Coupon;

public interface CouponRepository extends MongoRepository<Coupon, String>, PagingAndSortingRepository<Coupon, String> {

  Page<Coupon> findByIsActiveTrue(Pageable pageable);

}
