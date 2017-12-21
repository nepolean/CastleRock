package com.subsede.user.eventListeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.subsede.user.events.RegistrationCompleteEvent;
import com.subsede.user.model.user.User;
import com.subsede.user.model.user.VerificationToken;
import com.subsede.user.services.user.UserService;

@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

	@Autowired
	private UserService userService;

	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		this.sendConfirmationLink(event);
	}

	private void sendConfirmationLink(RegistrationCompleteEvent event) {
		User user = event.getUser();
		VerificationToken verificationToken = userService.generateVerificationToken(user);
		String applicationUrl = event.getApplicationUrl() + "/confirmRegistration";
		userService.sendVerificationToken(user, applicationUrl, verificationToken.getToken());
	}
}