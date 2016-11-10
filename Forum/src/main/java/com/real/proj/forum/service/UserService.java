package com.real.proj.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.real.proj.forum.model.User;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public UserService() {

  }

  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getUser(String userMail) {
    return userRepository.findByEmail(userMail);
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public User subscribe(String userName, String forumId) {
    User user = this.getUser(userName);
    user.addSubscription(forumId);
    user = userRepository.save(user);
    return user;
  }

}
