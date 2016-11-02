package com.real.proj.forum.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.real.proj.forum.model.Message;

@EnableMongoRepositories
public interface MessageRepository extends MongoRepository<Message, String>{

  public List<Message> findByForumId(String forumId, Pageable pageable);
  
}
