package com.real.proj.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.real.proj.forum.model.User;

@Service
public class UserServiceImpl {
  
  @Autowired
  private UserRepository userRepository;
  
  public UserServiceImpl() {
    
  }
  
  
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  
  public User getUser(String userMail) {
    return userRepository.findByEmail(userMail);
  }

  public void save(User user) {  
    userRepository.save(user);
  }

}
