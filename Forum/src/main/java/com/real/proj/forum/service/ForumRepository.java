package com.real.proj.forum.service;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.real.proj.forum.model.Forum;

public interface ForumRepository extends MongoRepository<Forum, String> {

  List<Forum> findBySubscribers_Email(String email);

}
