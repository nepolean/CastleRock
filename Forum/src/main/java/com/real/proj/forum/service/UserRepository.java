package com.real.proj.forum.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.Repository;

import com.real.proj.forum.model.User;
import java.lang.Long;

public interface UserRepository extends MongoRepository<User, String> {

  User findByEmail(String userMail);

}
