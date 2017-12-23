package com.subsede.user.model.user;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.subsede.user.validators.ValidEmail;
import com.subsede.user.validators.ValidPatterns;

public class UserProfileDTO {

  @NotBlank(message = "Username should not be empty")
  @Size(min = 8, max = 15, message = "Username should be between 8 and 15 characters")
  @Pattern(regexp = ValidPatterns.PATTERN_WITH_DIGITS_AND_ALLOWED_SPECIAL_CHARACTERS, message = "Username does not match the required pattern")
  private String username;

  @NotBlank(message = "Email should not be empty")
  @ValidEmail(message = "Valid email address required")
  private String email;

  @NotBlank(message = "First name should not be empty")
  @Size(max = 31, message = "First name can have upto 31 characters")
  @Pattern(regexp = ValidPatterns.PATTERN_WITHOUT_SPECIAL_CHARACTERS_AND_DIGITS, message = "First name should not contain special characters")
  private String firstName;

  @Size(max = 31, message = "Middle name can have upto 31 characters")
  @Pattern(regexp = ValidPatterns.PATTERN_WITHOUT_SPECIAL_CHARACTERS_AND_DIGITS, message = "Middle name should not contain special characters")
  private String middleName;

  @NotBlank(message = "Last name should not be empty")
  @Size(max = 31, message = "Last name can have upto 31 characters")
  @Pattern(regexp = ValidPatterns.PATTERN_WITHOUT_SPECIAL_CHARACTERS_AND_DIGITS, message = "Last name should not contain special characters")
  private String lastName;

  @Size(max = 100, message = "Address can have upto 100 characters")
  private String address;

  @Pattern(regexp = ValidPatterns.PATTERN_FOR_VALID_INDIAN_MOBILE_NUMBER, message = "Mobile number should contain only digits")
  private String mobileNo;

  private boolean isPrimary;
  
  public UserProfileDTO() {
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public boolean isPrimary() {
    return isPrimary;
  }

  public void setPrimary(boolean isPrimary) {
    this.isPrimary = isPrimary;
  }

}