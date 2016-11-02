package com.real.proj.forum.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.real.proj.forum.model.Forum;

public interface ForumRepository extends MongoRepository<Forum, String> {

}
