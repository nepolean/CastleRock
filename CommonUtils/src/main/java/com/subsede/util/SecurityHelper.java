package com.subsede.util;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
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

  public String getLoggedInUserRole() {
    @SuppressWarnings("unchecked")
    Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>) SecurityContextHolder.getContext()
        .getAuthentication().getAuthorities();
    if (roles != null & roles.size() > 0)
      return roles.iterator().next().getAuthority();
    return null;
  }
  
  public String getLoggedInUsername() {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    if (userName == null)
      throw new InvalidSessionException();
    return userName;
  }
}
