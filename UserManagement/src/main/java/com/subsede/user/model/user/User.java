package com.subsede.user.model.user;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.subsede.user.validators.ValidEmail;
import com.subsede.user.validators.ValidPatterns;

@Document(collection = "Users")
public class User {

  public interface PublicView { }
  
  public interface CommitteView extends PublicView { }
  
  public interface UserView extends CommitteView {
  };

  public interface AdminView extends UserView {
  };

  private String userType;

  @Id
  String id;

  @NotBlank(message = "Username should not be empty")
  @Pattern(regexp = ValidPatterns.PATTERN_WITH_DIGITS_AND_ALLOWED_SPECIAL_CHARACTERS, message = "Username does not match the required pattern")
  @Size(min = 8, max = 15)
  @Indexed(dropDups = true)
  private String username;

  @NotBlank(message = "Email should not be empty")
  @ValidEmail(message = "Valid email address required")
  @Indexed(unique = true, dropDups = true)
  private String email;

  @JsonIgnore
  @NotBlank(message = "Password should not be empty")
  @Size(min = 8, max = 15, message = "Password length should be between 8 and 15 characters")
  private String password;

  @NotBlank(message = "First name should not be empty")
  @Size(max = 31, message = "First name can have upto 31 characters")
  @Pattern(regexp = ValidPatterns.PATTERN_WITHOUT_SPECIAL_CHARACTERS_AND_DIGITS, message = "First name should not contain special characters")
  private String firstName;

  @Size(max = 31, message = "Middle name can have upto 31 characters")
  @Pattern(regexp = ValidPatterns.PATTERN_WITHOUT_SPECIAL_CHARACTERS_AND_DIGITS, message = "Middle name should not contain special characters")
  private String middleName;

  @Size(max = 31, message = "Last name can have upto 31 characters")
  @Pattern(regexp = ValidPatterns.PATTERN_WITHOUT_SPECIAL_CHARACTERS_AND_DIGITS, message = "Last name should not contain special characters")
  private String lastName;

  @Size(max = 10, message = "Mobile no can have upto 10 digits")
  @Pattern(regexp = ValidPatterns.PATTERN_FOR_VALID_INDIAN_MOBILE_NUMBER, message = "MobileNo should not contain characters other than digits")
  private String mobileNo;

  private String profilePic;
  
  private boolean enabled = true;
  private boolean accountExpired = false;
  private boolean accountLocked = true;
  private boolean passwordExpired = false;

  private Set<Role> roles;

  private Date dateCreated;

  @DBRef
  private User manager;

  public User() {
    this.setUserType("USER");
    this.dateCreated = Calendar.getInstance().getTime();
  }

  public User(String username, String password) {
    this();
    this.username = username;
    this.password = password;
  }

  public String getId() {
    return this.id;
  }

  @JsonView(UserView.class)
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @JsonView(UserView.class)
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @JsonView(PublicView.class)
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @JsonView(PublicView.class)
  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  @JsonView(PublicView.class)
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @JsonView(CommitteView.class)
  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  @JsonView(AdminView.class)
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @JsonView(AdminView.class)
  public boolean isAccountExpired() {
    return accountExpired;
  }

  public void setAccountExpired(boolean accountExpired) {
    this.accountExpired = accountExpired;
  }

  @JsonView(AdminView.class)
  public boolean isAccountLocked() {
    return accountLocked;
  }

  public void setAccountLocked(boolean accountLocked) {
    this.accountLocked = accountLocked;
  }

  @JsonView(AdminView.class)
  public boolean isPasswordExpired() {
    return passwordExpired;
  }

  public void setPasswordExpired(boolean passwordExpired) {
    this.passwordExpired = passwordExpired;
  }

  @JsonView(AdminView.class)
  public Set<Role> getRoles() {
    return roles;
  }

  public void addRole(Role role) {
    if (this.roles == null) {
      this.roles = new HashSet<Role>();
    }
    this.roles.add(role);
  }

  public void removeRole(Role role) {
    if (this.roles != null) {
      this.roles.remove(role);
    }
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public String getUserType() {
    return userType;
  }

  protected void setUserType(String userType) {
    this.userType = userType;
  }

  @JsonView(AdminView.class)
  public User getManager() {
    return manager;
  }

  public void setManager(User manager) {
    this.manager = manager;
  }

  public void setProfilePic(String profilePic) {
    this.profilePic = profilePic;
  }
  
  @JsonView(PublicView.class)
  public String getProfilePic() {
    return this.profilePic;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
    result = prime * result + ((mobileNo == null) ? 0 : mobileNo.hashCode());
    result = prime * result + ((userType == null) ? 0 : userType.hashCode());
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    User other = (User) obj;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    if (firstName == null) {
      if (other.firstName != null)
        return false;
    } else if (!firstName.equals(other.firstName))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (lastName == null) {
      if (other.lastName != null)
        return false;
    } else if (!lastName.equals(other.lastName))
      return false;
    if (middleName == null) {
      if (other.middleName != null)
        return false;
    } else if (!middleName.equals(other.middleName))
      return false;
    if (mobileNo == null) {
      if (other.mobileNo != null)
        return false;
    } else if (!mobileNo.equals(other.mobileNo))
      return false;
    if (userType == null) {
      if (other.userType != null)
        return false;
    } else if (!userType.equals(other.userType))
      return false;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    return true;
  }

}