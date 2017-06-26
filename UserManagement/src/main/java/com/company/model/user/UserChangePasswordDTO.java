package com.company.model.user;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.company.validators.PasswordMatches;

@PasswordMatches
public class UserChangePasswordDTO implements PasswordDTO {

	@NotBlank(message = "Old password should not be empty")
	private String oldPassword;
	
	@NotBlank(message = "Password should not be empty")
	@Size(min=8, max=15, message = "Password length should be between 8 and 15 characters")
	private String password;
	
	@NotBlank(message = "Verify password should not be empty")
	@Size(min=8, max=15, message = "Verify password length should be between 8 and 15 characters")
	private String verifyPassword;
	
    public UserChangePasswordDTO() {}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerifyPassword() {
		return verifyPassword;
	}

	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
}