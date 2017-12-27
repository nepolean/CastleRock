package com.subsede.amc.model.job;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonView;
import com.subsede.user.model.user.User;

@Document(collection = "Users")
public class Technician extends User {
  
  @DBRef
  private Agency agency;
  
  private List<String> skills;
  
  private boolean isActive = true;
  
  public Technician(){
    
  }

  public Technician(
      String username,
      String password,
      String firstName,
      String lastName,
      String email,
      String mobileNo,
      Agency agency,
      List<String> skills) {
    super(username, password);
    super.setUserType("TECHNICIAN");
    super.setFirstName(firstName);
    super.setLastName(lastName);
    super.setEmail(email);
    super.setMobileNo(mobileNo);
    this.agency = agency;
    this.skills = skills;
  }

  public Agency getAgency() {
    return agency;
  }

  public void setAgency(Agency agency) {
    this.agency = agency;
  }

  public List<String> getSkills() {
    return skills;
  }

  public void setSkills(List<String> skills) {
    this.skills = skills;
  }
  
  @JsonView (AdminView.class)
  public boolean isActive() {
    return this.isActive;
  }
  
  public void block() {
    this.isActive = false;
  }
  
  public void unblock() {
    this.isActive = true;
  }

}
