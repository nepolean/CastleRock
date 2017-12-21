package com.subsede.util.user.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.subsede.util.user.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

  public User findByEmail(String userMail);

  public User findByUserName(String userName);

}
