package com.real.proj.forum.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

public class Forum {
  
  @Id
  Long id;
  String subject;
  Date createdDate;
  List<User> subscribers;
  List<Message> messages;
  boolean isClosed;
  
  public Forum(String subject) {
    this.subject = subject;
    createdDate = new Date();
  }

  public void addSubscribers(List<User> subscribers) {
    getSubscribers().addAll(subscribers);
  }
  
  public void addSubscriber(User subscriber) {
    getSubscribers().add(subscriber);
  }
  
  public void postMessage(Message msg) {
    getMessages().add(msg);
  }

  public List<Message> getMessages() {
    if (messages == null)
      messages = new ArrayList<Message>();
    return messages;
  }
  
  public List<User> getSubscribers() {
    if (subscribers == null)
      subscribers = new ArrayList<User>();
    return subscribers;
  }

}
