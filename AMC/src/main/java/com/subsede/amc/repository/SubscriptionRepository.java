package com.subsede.amc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.subsede.amc.model.subscription.Subscription;

@Repository
public interface SubscriptionRepository
    extends MongoRepository<Subscription, String>, PagingAndSortingRepository<Subscription, String> {

  public Subscription findByQuotationId(String id);

}
