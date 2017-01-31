package com.real.proj.user.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.real.proj.user.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

  public User findByEmail(String userMail);

}
