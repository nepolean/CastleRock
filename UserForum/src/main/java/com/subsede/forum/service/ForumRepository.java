package com.subsede.forum.service;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.subsede.forum.model.Forum;

@Repository
public interface ForumRepository extends MongoRepository<Forum, String> {

  List<Forum> findBySubscribers_Email(String email);

}
