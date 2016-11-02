package com.real.proj.forum.service;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.real.proj.forum.model.Forum;
import com.real.proj.forum.model.Message;
import com.real.proj.forum.model.User;

@Service
public class ForumService {
  
  private static final Logger logger = LogManager.getLogger(ForumService.class);
  
  @Autowired
  private ForumRepository forumRepository;
  
  @Autowired 
  private UserServiceImpl userService;
  
  
  
  public void setForumRepository(ForumRepository forumRepository) {
    this.forumRepository = forumRepository;
  }

  public Forum createForum(String subject, String userName) throws Exception {
    if (logger.isDebugEnabled())
      logger.debug("Create new forum with subject " + subject);
    User user = userService.getUser(userName);
    if (null == user) {
      logger.error("The user " + userName + " is not found");
      throw new Exception("User does not exist");
    }
    Forum f = new Forum(subject);
    f.setOwner(user);
    f.addSubscriber(user);
    try {
      f = forumRepository.save(f);
    } catch(Exception ex) {
      ex.printStackTrace(System.err);
      throw new Exception("Error while creating forum");
    }
    user.addSubscription(f);
    try {      
      userService.save(user);      
    } catch(Exception ex) {
      //TODO Determine how to deal with this failue?
    }
    return f;
  }

  public Forum addSubscriber(String forumId, User subscriber) throws Exception{
    
    try {
      Forum f = forumRepository.findOne(forumId);
      f.addSubscriber(subscriber);
      f = forumRepository.save(f);
      return f;
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
      throw new Exception("Error while adding subscriber");
    }
  }
  
  public Forum postMessage(String forumId, Message msg) throws Exception {
    
    try {
      Forum f = forumRepository.findOne(forumId);
      f.postMessage(msg);
      f = forumRepository.save(f);
      return f;
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
      throw new Exception("Error while posting the message");
    }
    
  }
  
  public  Forum getForum(String forumId) throws Exception {
    try {
      Forum f = forumRepository.findOne(forumId);
      return f;
    } catch (Exception ex) {
      if (logger.isDebugEnabled())
        logger.debug("Error getting forum", ex);
      throw new Exception("Error while getting forum details");
    }
  }

  public static void getForums(Long id) {
    
  }

  public  List<Forum> getMessages(String userName, int start, int count) {
    User user = userService.getUser(userName);
    List<Forum> subscriptions = user.getSubscriptions();
    if (null != subscriptions)
      for (Forum f : subscriptions) {
        List<Message> messages = MessageService.getMessages(f, start, count);
        f.setMessages(messages);
      }    
    return subscriptions;  
  }
  public  Forum getMessagesForForum(String forumId, int start, int count) throws Exception {  
    Forum f = forumRepository.findOne(forumId);
    List<Message> messages =  MessageService.getMessages(f, start, count);
    f.setMessages(messages);
    return f;
  }

  public  Forum subscribeUser(String forumId, String loggedInUser) throws Exception {
    if (logger.isDebugEnabled())
      logger.debug("subscribeUser ( " + forumId + " , " + loggedInUser + " )");
    Forum f = forumRepository.findOne(forumId);
    if (null == f) {
      logger.error("The forum with id " + forumId + " does not exst");
      throw new Exception("Forum does not exist");
    }
    User user = userService.getUser(loggedInUser);
    if (null == user) {
      logger.error("The user " + loggedInUser + " is not found");
      throw new Exception("User does not exist");
    }
    user.addSubscription(f);
    try {
      userService.save(user);
    } catch(Exception ex) {
      logger.error("Error while saving user subscription ");
      if (logger.isDebugEnabled())
        logger.debug("Error while saving the user", ex);
    }
    return f;
  }

  public List<Forum> getSubscriptions(String loggedInUser) {
    // TODO Auto-generated method stub
    return null;
  }
}
