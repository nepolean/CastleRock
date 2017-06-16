package com.real.proj.amc.model.workflow;

import javax.validation.constraints.NotNull;

public class NonRegisteredUser {

  @NotNull
  private String firstName;
  @NotNull
  private String lastName;
  @NotNull
  private String mobileNo;
  @NotNull
  private String emailId;

  public NonRegisteredUser(String firstName, String lastName, String mobileNo, String emailId) {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
    this.mobileNo = mobileNo;
    this.emailId = emailId;
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

  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

}
