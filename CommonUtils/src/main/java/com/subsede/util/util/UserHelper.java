package com.subsede.util.util;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.subsede.user.model.user.User;
import com.subsede.user.services.user.UserService;
import com.subsede.util.controller.exception.InvalidSessionException;

@Resource
public class UserHelper {

  private UserService userService;

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public User getCurrentUser() {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    if (userName == null)
      throw new InvalidSessionException();
    return this.userService.findByUsername(userName);
  }
}
