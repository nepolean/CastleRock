package com.company.model.user;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.company.validators.PasswordMatches;

@PasswordMatches
public class UserResetPasswordDTO implements PasswordDTO {

	@NotBlank(message = "Username should not be empty")
	private String username;
	
	@NotBlank(message = "Password reset token is missing")
	private String token;

	@NotBlank(message = "Password should not be empty")
	@Size(min=8, max=15, message = "Password length should be between 8 and 15 characters")
	private String password;
	
	@NotBlank(message = "Verify password should not be empty")
	@Size(min=8, max=15, message = "Verify password length should be between 8 and 15 characters")
	private String verifyPassword;
	
    public UserResetPasswordDTO() {}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}