package com.company.controllers.rest;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.events.RegistrationCompleteEvent;
import com.company.exceptions.BindingFailureException;
import com.company.model.user.User;
import com.company.model.user.UserProfileDTO;
import com.company.services.user.UserService;

public abstract class AbstractUserRestController {

	@Autowired
	private UserService userService;

	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	public ResponseEntity<User> getUser(@PathVariable("username") String username) {
		User user = userService.findByUsername(username);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	public ResponseEntity<User> createUser(@Valid @RequestBody UserProfileDTO userProfileDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new BindingFailureException(result);
		}
		User user = userService.createUserAccount(userProfileDTO);
		String applicationUrl = "http://localhost/user/";
		eventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl));
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
	
	public ResponseEntity<User> updateUser(@PathVariable String username, @Valid @RequestBody UserProfileDTO userProfileDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new BindingFailureException(result);
		}
		User user = userService.updateUser(username, userProfileDTO);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	public ResponseEntity<Map<String,String>> sendPasswordResetLink(@PathVariable String username) {

		User user = userService.findByUsername(username);
		userService.sendPasswordResetLink(user.getUsername(),user.getEmail());
		String message = String.format("Password link sent successfully for %s.", username);
		Map<String,String> map = new HashMap<String, String>();
		map.put("message",message);
		return new ResponseEntity<Map<String,String>>(map, HttpStatus.OK);

	}
}