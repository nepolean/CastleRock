package com.subsede.user.controllers.rest.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.subsede.user.model.user.User;
import com.subsede.user.services.user.UserService;

@RestController
@EnableAutoConfiguration
@RequestMapping("/api/v1/")
public class UserRestController {

	@Autowired
	private UserService userService;

	@Autowired
	ApplicationEventPublisher eventPublisher;

	@InitBinder("updateUser")
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("middleName");
	}

	/* User admin actions */

	@GetMapping("/users/{username}")
	public ResponseEntity<User> getUser(@PathVariable("username") String username) {

		User user = userService.findByUsername(username);
		return new ResponseEntity<User>(user, HttpStatus.FOUND);
	}

//	@PutMapping("/users/{username}")
//	public ResponseEntity<User> updateUser(@PathVariable String username,
//			@ModelAttribute("updateUser") @Valid User user) throws UserNotFoundException {
//
//		user = userService.updateUser(username, user);
//		return new ResponseEntity<User>(user, HttpStatus.OK);
//
//	}

}