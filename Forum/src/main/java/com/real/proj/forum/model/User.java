package com.real.proj.forum.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
  
  @Id
  String Id;
  String firstName;
  String lastName;
  String email;
  String mobileNo;
  
  List<Forum> subscriptions;
  
  public User() {
    
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public void addSubscription(Forum f) {
    this.getSubscriptions().add(f);
  }

  public List<Forum> getSubscriptions() {
    return subscriptions == null ? new ArrayList<Forum>() : subscriptions;
  }

}
