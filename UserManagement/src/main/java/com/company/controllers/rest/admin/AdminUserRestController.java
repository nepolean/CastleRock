package com.company.controllers.rest.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.controllers.rest.AbstractUserRestController;
import com.company.model.user.Role;
import com.company.model.user.User;
import com.company.model.user.UserProfileDTO;
import com.company.services.user.UserService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@EnableAutoConfiguration
@RequestMapping("/api/admin/")
public class AdminUserRestController extends AbstractUserRestController {

	@Autowired
	private UserService userService;

	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@GetMapping("/users")
	public ResponseEntity<Page<User>> getUsers(Pageable pageable,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "username", required = false) String username) {
		if(username!=null)
			return new ResponseEntity<Page<User>>(userService.findAllUsersByUsername(pageable, username), HttpStatus.OK);
		if(type!=null)
			return new ResponseEntity<Page<User>>(userService.findAllUsersByUserType(pageable, type), HttpStatus.OK);
		return new ResponseEntity<Page<User>>(userService.findAll(pageable), HttpStatus.OK);
	}
	
	@GetMapping("/roles")
	public ResponseEntity<Page<Role>> getRoles(Pageable pageable) {
		return new ResponseEntity<Page<Role>>(userService.findAllRoles(pageable), HttpStatus.OK);
	}
	
	@Override
	@GetMapping(value="/users/{username}")
	@JsonView(User.AdminView.class)
	public ResponseEntity<User> getUser(@PathVariable("username") String username) {
		return super.getUser(username);
	}

	@Override
	@PostMapping(value = "/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody UserProfileDTO userProfileDTO, BindingResult result) {
		return super.createUser(userProfileDTO, result);
	}
	
	@Override
	@PutMapping("/users/{username}")
	public ResponseEntity<User> updateUser(@PathVariable String username, @Valid @RequestBody UserProfileDTO userProfileDTO, BindingResult result) {
		return super.updateUser(username, userProfileDTO, result);
	}

	@DeleteMapping("/users/{username}")
	public ResponseEntity<String> deleteUser(@PathVariable String username) {

		userService.deleteUser(username);
		String message = String.format("%s deleted successfully.", username);
		return new ResponseEntity<String>(message, HttpStatus.OK);

	}

	@GetMapping("/users/{username}/disable")
	public ResponseEntity<Map<String, String>> disableUser(@PathVariable String username) {

		userService.disableUser(username);
		String message = String.format("%s disabled successfully.", username);
		Map<String,String> map = new HashMap<String, String>();
		map.put("message",message);
		return new ResponseEntity<Map<String,String>>(map, HttpStatus.OK);

	}

	@GetMapping("/users/{username}/enable")
	public ResponseEntity<Map<String,String>> enableUser(@PathVariable String username) {

		userService.enableUser(username);
		String message = String.format("%s enabled successfully.", username);
		Map<String,String> map = new HashMap<String, String>();
		map.put("message",message);
		return new ResponseEntity<Map<String,String>>(map, HttpStatus.OK);

	}
	
	@GetMapping("/users/{username}/lock")
	public ResponseEntity<Map<String,String>> lockUser(@PathVariable String username) {

		userService.lockUser(username);
		String message = String.format("%s locked successfully.", username);
		Map<String,String> map = new HashMap<String, String>();
		map.put("message",message);
		return new ResponseEntity<Map<String,String>>(map, HttpStatus.OK);

	}
	
	@GetMapping("/users/{username}/unlock")
	public ResponseEntity<Map<String,String>> unlockUser(@PathVariable String username) {

		userService.unblockUser(username);
		String message = String.format("%s unlocked successfully.", username);
		Map<String,String> map = new HashMap<String, String>();
		map.put("message",message);
		return new ResponseEntity<Map<String,String>>(map, HttpStatus.OK);

	}
	
	@Override
	@GetMapping("/users/{username}/sendPasswordResetLink")
	public ResponseEntity<Map<String,String>> sendPasswordResetLink(@PathVariable String username) {
		return super.sendPasswordResetLink(username);
	}
	
	@PutMapping("/users/{username}/setRoles")
	public ResponseEntity<Map<String,String>> setRoles(@PathVariable String username, @RequestBody Set<String> roles) {

		userService.setRoles(username, roles);
		String message = String.format("Roles set successfully for %s.", username);
		Map<String,String> map = new HashMap<String, String>();
		map.put("message",message);
		return new ResponseEntity<Map<String,String>>(map, HttpStatus.OK);

	}

}