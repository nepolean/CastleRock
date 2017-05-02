package com.real.proj.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.real.proj.controller.exception.EntityNotFoundException;
import com.real.proj.user.model.User;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public UserService() {

  }

  /*
   * public void setUserRepository(UserRepository userRepository) {
   * this.userRepository = userRepository; }
   */

  public User getUser(String userMail) {
    return userRepository.findByEmail(userMail);
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public User subscribe(String userName, String forumId) {
    User user = this.getUser(userName);
    // user.addSubscription(forumId);
    user = userRepository.save(user);
    return user;
  }

  public User findByUserName(String userName) {
    User loggedInUser = this.userRepository.findByUserName(userName);
    if (loggedInUser == null)
      throw new EntityNotFoundException(userName, "", "User");
    return loggedInUser;
  }

}
