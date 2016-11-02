package com.real.proj.forum.controller;

import java.security.Principal;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.forum.model.Forum;
import com.real.proj.forum.service.ForumService;
import com.real.proj.forum.service.UserServiceImpl;

@RestController
public class ForumController {
  
  private static final Logger logger = LogManager.getLogger(ForumController.class);
  
  @Autowired
  private ForumService forumService;
  
  @RequestMapping(name="/forum/create", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createForum(@RequestBody String subject, Principal loggedInUser) {
    Forum forum;
    try {
      //TODO 
      String userName = "dummy@gmail.com";
      System.out.println("User " + userName);
      System.out.println("subject " + subject);
      System.out.println("forum service " + forumService);
      forum = forumService.createForum(subject, userName);
      return new ResponseEntity<Object>(forum, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace(System.out);
      logger.error("There is an error while creating a forum: " + e.getMessage());
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
  }
  
  public Forum subscribeMe(String forumId, String loggedInUser) {    
    try {
      return forumService.subscribeUser(forumId, loggedInUser);
    } catch (Exception e) {
      logger.error("There is an error while subscribing user to the forum: " + e.getMessage());
      return null;
    }    
  }
  

  public List<Forum> forumsBelongingTo(String loggedInUser) {
    
    try {
      return forumService.getSubscriptions(loggedInUser);
    } catch (Exception ex) {
      if (logger.isDebugEnabled())
        logger.debug("Error while fetching the subscriptions", ex);      
      return null;      
    }
  }  
  
  public List<Forum> recentMessagesForMe(String loggedInUser, int start, int count) {
    
    return forumService.getMessages(loggedInUser, start, count);
    
  }
  
  public void postTextMessage(Long forumId, String message, Principal loggedInUser) {
    
  }
  
  public void uploadFile(Long forumId, Principal loggedInUser) {
    
  }
  
  public void inviteOthersByEmail(List<String> emails, Principal loggedInUser) {
    
  }
}
