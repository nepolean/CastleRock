package com.real.proj.amc.model.job;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.real.proj.user.model.User;

public class Technician extends User {

  @DBRef
  private Agency agency;
  private List<String> skills;

  public Technician(
      String firstName,
      String lastName,
      String email,
      String mobileNo,
      Agency agency,
      List<String> skills) {
    super(firstName, lastName, email, mobileNo);
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

}
