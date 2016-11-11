package com.real.proj.forum.service;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.real.proj.forum.model.User;

public interface UserRepository extends MongoRepository<User, String> {

  User findByEmail(String userMail);

}
