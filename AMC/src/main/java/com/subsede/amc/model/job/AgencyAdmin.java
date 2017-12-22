package com.subsede.amc.model.job;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.subsede.user.model.user.User;

@Document(collection = "Users")
public class AgencyAdmin extends User {
  @DBRef
  private Agency agency;

  public AgencyAdmin(
      String username,
      String password,
      String firstName,
      String lastName,
      String email,
      String mobileNo,
      Agency agency) {
    super(username, password);
    super.setUserType("AGENCY_ADMIN");
    super.setFirstName(firstName);
    super.setLastName(lastName);
    super.setEmail(email);
    super.setMobileNo(mobileNo);
    this.agency = agency;
  }

}
