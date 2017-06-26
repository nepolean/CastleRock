package com.company.eventListeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.company.services.security.LoginAttemptService;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

	@Autowired
	private LoginAttemptService loginAttemptService;

	public void onApplicationEvent(AuthenticationSuccessEvent e) {
//		WebAuthenticationDetails auth = (WebAuthenticationDetails) e.getAuthentication().getDetails();

		loginAttemptService.loginSucceeded(e.getAuthentication().getName());
	}
}