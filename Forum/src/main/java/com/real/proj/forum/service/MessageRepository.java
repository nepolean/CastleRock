package com.real.proj.forum.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.real.proj.forum.model.Message;

@EnableMongoRepositories
public interface MessageRepository extends MongoRepository<Message, String> {

}
