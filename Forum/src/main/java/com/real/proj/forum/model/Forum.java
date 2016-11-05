package com.real.proj.forum.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Forum {

  @Id
  String id;
  User owner;
  String subject;
  Date createdDate;
  List<User> subscribers;
  List<Message> messages;
  boolean isClosed;

  public Forum() {

  }

  public Forum(String subject) {
    this.subject = subject;
    createdDate = new Date();
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public void addSubscribers(List<User> subscribers) {
    getSubscribers().addAll(subscribers);
  }

  public void addSubscriber(User subscriber) {
    getSubscribers().add(subscriber);
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

  public String getId() {
    return id;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public boolean isClosed() {
    return isClosed;
  }

  public void setClosed(boolean isClosed) {
    this.isClosed = isClosed;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setSubscribers(List<User> subscribers) {
    this.subscribers = subscribers;
  }

  public void postMessage(Message post) {
    this.getMessages().add(post);
  }

}
