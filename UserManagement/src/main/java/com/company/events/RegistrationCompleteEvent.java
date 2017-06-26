package com.company.events;

import org.springframework.context.ApplicationEvent;

import com.company.model.user.User;

public class RegistrationCompleteEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1211177641585005145L;
	private final String applicationUrl;
	private final User user;

	public RegistrationCompleteEvent(User user, String applicationUrl) {

		super(user);
		this.user = user;
		this.applicationUrl = applicationUrl;
	}

	public String getApplicationUrl() {
		return applicationUrl;
	}
	
	public User getUser() {
		return user;
	}

}