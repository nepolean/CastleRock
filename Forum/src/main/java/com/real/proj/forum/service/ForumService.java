package com.real.proj.forum.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.real.proj.forum.model.Forum;
import com.real.proj.forum.model.Message;
import com.real.proj.forum.model.User;

public class ForumService {
  
  @Autowired
  static ForumRepository repository;

  public static Forum createForum(String subject) throws Exception {
    Forum f = new Forum(subject);
    try {
      f = repository.save(f);
    } catch(Exception ex) {
      ex.printStackTrace(System.err);
      throw new Exception("Error while creating forum");
    }
    return f;
  }

  public static Forum addSubscriber(Long forumId, User subscriber) throws Exception{
    
    try {
      Forum f = repository.findOne(forumId);
      f.addSubscriber(subscriber);
      f = repository.save(f);
      return f;
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
      throw new Exception("Error while adding subscriber");
    }
  }
  
  public static Forum postMessage(Long forumId, Message msg) throws Exception {
    
    try {
      Forum f = repository.findOne(forumId);
      f.postMessage(msg);
      f = repository.save(f);
      return f;
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
      throw new Exception("Error while posting the message");
    }
    
  }
  
  public static Forum getForum(Long forumId) throws Exception {
    try {
      Forum f = repository.findOne(forumId);
      return f;
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
      throw new Exception("Error while getting forum details");
    }
  }

  public static void getForums(Long id) {
  }
}
