package com.subsede.forum.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.subsede.user.model.user.User;

@Document
public class Forum implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  String id;
  User owner;
  String subject;
  Date createdDate;
  List<User> subscribers;
  List<SubscriptionRequest> subscriptionRequests;
  List<Message> messages;
  boolean isClosed;
  boolean isAutoSubscriptionEnabled;

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

  public void addSubscriptionRequest(SubscriptionRequest request) {
    getSubscriptionRequests().add(request);
  }

  public List<SubscriptionRequest> getSubscriptionRequests() {
    if (this.subscriptionRequests == null)
      this.subscriptionRequests = new ArrayList<SubscriptionRequest>();
    return this.subscriptionRequests;
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

  public boolean isAutoSubscriptionEnabled() {
    return isAutoSubscriptionEnabled;
  }

  public void setAutoSubscriptionEnabled(boolean isAutoSubscriptionEnabled) {
    this.isAutoSubscriptionEnabled = isAutoSubscriptionEnabled;
  }

}
