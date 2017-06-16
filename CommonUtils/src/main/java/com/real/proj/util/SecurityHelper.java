package com.real.proj.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.real.proj.controller.exception.InvalidSessionException;
import com.real.proj.user.model.User;
import com.real.proj.user.service.UserService;

@Component
public class SecurityHelper {

  private UserService userService;

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public User getLoggedInUser() {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    if (userName == null)
      throw new InvalidSessionException();
    return this.userService.findByUserName(userName);
  }

  public boolean IsUserLoggedIn() {
    return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
  }

}
