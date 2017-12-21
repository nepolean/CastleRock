package com.subsede.util.util;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.subsede.util.controller.exception.InvalidSessionException;
import com.subsede.util.user.model.User;
import com.subsede.util.user.service.UserService;

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
    return this.userService.findByUserName(userName);
  }
}
