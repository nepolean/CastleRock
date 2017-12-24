package com.real.proj.user.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User implements Serializable {
  /**
   * default serial id
   */
  private static final long serialVersionUID = 1L;
  @Id
  String Id;
  String userName;
  String firstName;
  String lastName;
  String email;
  String mobileNo;
  // List<String> subscriptions;

  public User() {

  }

  public User(String firstName, String lastName, String email, String mobileNo) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.mobileNo = mobileNo;
  }

  public User(String userName, String firstName, String lastName, String email, String mobileNo) {
    this(firstName, lastName, email, mobileNo);
    this.userName = userName;
  }

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobileNo() {
    return this.mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public String getRole() {
    // TODO Need to implement this for fool proof.
    return "AGENT";
  }

  /*
   * public void addSubscription(String forumId) {
   * this.getSubscriptions().add(forumId); }
   * 
   * public List<String> getSubscriptions() { if (subscriptions == null)
   * subscriptions = new ArrayList<String>(); return subscriptions; }
   * 
   * public List<Forum> getSubscriptions(PageRequest pageRequest) { return null;
   * }
   */
}