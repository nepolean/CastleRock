package com.subsede.amc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.subsede.amc.model.NonSubscriptionOrder;

public interface NonSubscriptionOrderRepository 
      extends MongoRepository<NonSubscriptionOrder, String>, 
      PagingAndSortingRepository<NonSubscriptionOrder, String> {

}
