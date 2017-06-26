package com.company.model.user;

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

import com.company.validators.ValidEmail;
import com.company.validators.ValidPatterns;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Document(collection="user")
public class User {

	public interface UserView {};
	public interface AdminView extends UserView {};
	
	private String userType;
	
	@Id
	@NotBlank(message = "Username should not be empty")
	@Pattern(regexp = ValidPatterns.PATTERN_WITH_DIGITS_AND_ALLOWED_SPECIAL_CHARACTERS, message = "Username does not match the required pattern")
	@Size(min=8, max=15)
	@Indexed(dropDups=true)
	private String username;
	
	@NotBlank(message = "Email should not be empty")
	@ValidEmail(message = "Valid email address required")
	@Indexed(unique=true, dropDups=true)
	private String email;
	
	@JsonIgnore
	@NotBlank(message = "Password should not be empty")
	@Size(min=8, max=15, message = "Password length should be between 8 and 15 characters")
	private String password;
	
	@NotBlank(message = "First name should not be empty")
	@Size(max=31, message = "First name can have upto 31 characters")
	@Pattern(regexp = ValidPatterns.PATTERN_WITHOUT_SPECIAL_CHARACTERS_AND_DIGITS, message = "First name should not contain special characters")
	private String firstName;
	
	@Size(max=31, message = "Middle name can have upto 31 characters")
	@Pattern(regexp = ValidPatterns.PATTERN_WITHOUT_SPECIAL_CHARACTERS_AND_DIGITS, message = "Middle name should not contain special characters")
	private String middleName;
	
	@Size(max=31, message = "Last name can have upto 31 characters")
	@Pattern(regexp = ValidPatterns.PATTERN_WITHOUT_SPECIAL_CHARACTERS_AND_DIGITS, message = "Last name should not contain special characters")
	private String lastName;
	
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

	@JsonView(UserView.class)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonView(UserView.class)
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@JsonView(UserView.class)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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
		if(this.roles == null) {
			this.roles = new HashSet<Role>();
		}
		this.roles.add(role);
	}
	
	public void removeRole(Role role) {
		if(this.roles != null) {
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
}