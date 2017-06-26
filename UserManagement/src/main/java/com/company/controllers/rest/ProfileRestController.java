package com.company.controllers.rest;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.exceptions.BindingFailureException;
import com.company.model.user.User;
import com.company.model.user.UserChangePasswordDTO;
import com.company.model.user.UserProfileDTO;
import com.company.services.user.UserService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@EnableAutoConfiguration
@RequestMapping("/api/profile")
public class ProfileRestController {

	@Autowired
	private UserService userService;

	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@GetMapping(value="/isAuthenticated")
	public boolean isAuthenticated() {
		if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ||
				(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken))
			throw new AuthenticationCredentialsNotFoundException("User not logged in");
		return true;
	}
	
	@GetMapping(value="/isAdmin")
	public boolean isAdmin(HttpServletRequest request) {
		if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ||
				!request.isUserInRole("ROLE_ADMIN"))
			throw new AuthenticationCredentialsNotFoundException("User not authorized");
		return true;
	}
	
	@GetMapping(value="/isUser")
	public boolean isUser(HttpServletRequest request) {
		if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ||
				!request.isUserInRole("ROLE_USER"))
			throw new AuthenticationCredentialsNotFoundException("User not authorized");
		return true;
	}
	
	@GetMapping
	@JsonView(User.UserView.class)
	public ResponseEntity<User> getProfile() {
		if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ||
				(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken))
			throw new AuthenticationCredentialsNotFoundException("User not logged in");
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@Secured("IS_AUTHENTICATED_FULLY")
	@PostMapping
	@JsonView(User.UserView.class)
	public ResponseEntity<User> updateProfile(@Valid @RequestBody UserProfileDTO userProfileDTO, BindingResult result,
			Principal principal) {
		if (result.hasErrors()) {
			throw new BindingFailureException(result);
		}
		User user = userService.updateUser(principal.getName(), userProfileDTO);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@Secured("IS_AUTHENTICATED_FULLY")
	@PostMapping(value = "/changePassword")
	public ResponseEntity<Map<String, String>> changePasswordAction(@Valid @RequestBody UserChangePasswordDTO userChangePasswordDTO, BindingResult result,
			Principal principal) {
		if (result.hasErrors()) {
			throw new BindingFailureException(result);
		}
		userService.changePassword(principal.getName(),userChangePasswordDTO);
		String message = "Password changed successfully";
		Map<String,String> map = new HashMap<String, String>();
		map.put("message",message);
		return new ResponseEntity<Map<String,String>>(map, HttpStatus.OK);
	}

}