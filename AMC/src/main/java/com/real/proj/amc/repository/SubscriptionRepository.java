package com.real.proj.amc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.real.proj.amc.model.Subscription;

@Repository
public interface SubscriptionRepository
    extends MongoRepository<Subscription, String>, PagingAndSortingRepository<Subscription, String> {

}
