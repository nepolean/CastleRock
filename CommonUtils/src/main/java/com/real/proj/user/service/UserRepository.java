package com.real.proj.user.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.real.proj.user.model.User;

@Component
public interface UserRepository extends MongoRepository<User, String> {

  User findByEmail(String userMail);

}
