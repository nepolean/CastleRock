package com.real.proj.amc.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.real.proj.amc.model.Subscription;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

}
