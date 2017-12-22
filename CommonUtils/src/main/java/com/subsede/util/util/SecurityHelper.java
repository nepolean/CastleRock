package com.subsede.util.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.subsede.user.model.user.User;
import com.subsede.user.services.user.UserService;
import com.subsede.util.controller.exception.InvalidSessionException;

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
    return this.userService.findByUsername(userName);
  }

  public boolean IsUserLoggedIn() {
    return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
  }
}
