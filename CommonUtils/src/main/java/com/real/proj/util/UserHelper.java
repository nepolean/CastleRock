package com.real.proj.util;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.real.proj.controller.exception.InvalidSessionException;
import com.real.proj.user.model.User;
import com.real.proj.user.service.UserService;

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
