package com.real.proj.forum.service;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.real.proj.forum.model.Forum;

public interface ForumRepository extends MongoRepository<Forum, Long> {
}
