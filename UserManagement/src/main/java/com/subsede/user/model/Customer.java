package com.subsede.user.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.subsede.user.model.user.User;

@Document(collection = "Users")
public class Customer extends User {

  private String address;
  private boolean isPrimary;
  private boolean publicProfile;

  public Customer() {
    super.setUserType("CUSTOMER");
  }

  public Customer(String username, String password, String address) {
    super(username, password);
    super.setUserType("CUSTOMER");
    this.address = address;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return String.format(
        "Customer[username='%s', password='%s', address='%s', email='%s']",
        this.getUsername(), this.getPassword(), this.getAddress(), this.getEmail());
  }

  public void setPrimary() {
    this.isPrimary = true;
  }
  
  public boolean isPrimary() {
    return isPrimary;
  }

}