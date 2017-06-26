package com.company.eventListeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import com.company.services.security.LoginAttemptService;

@Component
public class AuthenticationFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

	@Autowired
	private LoginAttemptService loginAttemptService;

	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
//		WebAuthenticationDetails auth = (WebAuthenticationDetails) e.getAuthentication().getDetails();
		loginAttemptService.loginFailed(e.getAuthentication().getName());
	}
}