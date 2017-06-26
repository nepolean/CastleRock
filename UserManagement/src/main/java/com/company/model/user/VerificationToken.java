package com.company.model.user;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class VerificationToken {

	private transient static final int EXPIRATION = 60 * 24;
	
	private String id;
	
	@DBRef
	private User user;
	
	@NotBlank
	private String token;
	
	private Date expiryDate;
	
	private boolean verified;
	
	public VerificationToken() {
		
	}
	
	public VerificationToken(User user, String token) {
        this.setUser(user);
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
        this.verified = false;
    }
     
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(cal.getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return cal.getTime();
    }
    
	public String getId() {
		return id;
	}
    
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate() {
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
}