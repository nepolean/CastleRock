package com.company.controllers.rest.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.model.user.User;
import com.company.services.user.UserService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@EnableAutoConfiguration
@RequestMapping("/api/v2/")
public class UserRest1Controller {

	@Autowired
	private UserService userService;

	
	@GetMapping("/users")
	@JsonView(User.UserView.class)
	public ResponseEntity<Page<User>> getUsers(Pageable pageable) {
		return new ResponseEntity<Page<User>>(userService.findAll(pageable), HttpStatus.OK);
	}
	
	@GetMapping(value="/users/{username}")
	@JsonView(User.UserView.class)
	public ResponseEntity<User> getUser(@PathVariable("username") String username) {

		User user = userService.findByUsername(username);
		return new ResponseEntity<User>(user, HttpStatus.FOUND);
	}

}