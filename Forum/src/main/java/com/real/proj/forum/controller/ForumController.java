/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.real.proj.forum.controller;

import com.real.proj.controller.exception.DBException;
import com.real.proj.controller.exception.EntityNotFoundException;
import com.real.proj.controller.exception.SecurityPermissionException;
import com.real.proj.forum.model.Forum;
import com.real.proj.forum.model.User;
import com.real.proj.forum.service.ForumService;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import javax.validation.Valid;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
public class ForumController {
	private static final Logger logger = LogManager.getLogger(ForumController.class);
	@Autowired
	private ForumService forumService;

	@RequestMapping(path = { "/forum/create" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public Forum createForum(@RequestBody String subject, Principal loggedInUser) throws EntityNotFoundException {
		try {
			return this.forumService.createForum(subject, loggedInUser.getName());
		} catch (Exception arg3) {
			this.handleException(arg3);
			return null;
		}
	}

	@RequestMapping(path = { "/forum/{forumId}" }, method = { RequestMethod.GET }, produces = { "application/json" })
	public Forum getForum(@Valid @PathVariable String forumId, Principal loggedInUser, WebRequest request)
			throws EntityNotFoundException, SecurityPermissionException {
		Forum f = null;

		try {
			f = this.forumService.getForum(forumId);
		} catch (Exception arg5) {
			this.handleException(arg5);
		}

		if (!this.hasRequiredPermission(loggedInUser, request, f)) {
			logger.error("User, " + loggedInUser.getName() + " , is not permitted to view the forum");
			throw new SecurityPermissionException();
		} else {
			return f;
		}
	}

	@RequestMapping(path = { "/forum/{forumId}/subscribe" }, method = { RequestMethod.POST }, produces = {
			"application/json" })
	public Forum subscribeMe(@PathVariable String forumId, Principal loggedInUser) throws EntityNotFoundException {
		try {
			return this.forumService.subscribeUser(forumId, loggedInUser.getName());
		} catch (Exception arg3) {
			this.handleException(arg3);
			return null;
		}
	}

	@RequestMapping(path = { "/forum/{forumId}/subscribe/{userId}" }, method = { RequestMethod.POST }, produces = {
			"application/json" })
	public void subscribeUser(@PathVariable String forumId, @PathVariable String userId, Principal loggedInUser,
			WebRequest request) throws EntityNotFoundException, SecurityPermissionException {
		Forum f = null;

		try {
			f = this.forumService.getForum(forumId);
		} catch (Exception arg7) {
			this.handleException(arg7);
		}

		if (request.isUserInRole("sa") && f.getOwner().getEmail().equals(userId)) {
			try {
				this.forumService.addSubscriber(forumId, userId);
			} catch (Exception arg6) {
				this.handleException(arg6);
			}

		} else {
			throw new SecurityPermissionException();
		}
	}

	@RequestMapping(path = { "/forum" }, method = { RequestMethod.GET }, produces = { "application/json" })
	public Iterable<Forum> forumsBelongingTo(Principal loggedInUser) throws EntityNotFoundException {
		try {
			return this.forumService.getForums(loggedInUser.getName());
		} catch (Exception arg2) {
			this.handleException(arg2);
			return null;
		}
	}

	public void postTextMessage(Long forumId, String message, Principal loggedInUser) {
	}

	public void uploadFile(Long forumId, Principal loggedInUser) {
	}

	public void inviteOthersByEmail(List<String> emails, Principal loggedInUser) {
	}

	private boolean hasRequiredPermission(Principal loggedInUser, WebRequest request, Forum f) {
		return request.isUserInRole("sa") || this.userBelongsToForum(f, loggedInUser.getName());
	}

	private boolean userBelongsToForum(Forum f, String name) {
		Iterator arg3 = f.getSubscribers().iterator();

		while (arg3.hasNext()) {
			User usr = (User) arg3.next();
			if (usr.getEmail().equals(name)) {
				return true;
			}
		}

		return false;
	}

	private void handleException(Exception ex) throws EntityNotFoundException {
		if (ex instanceof EntityNotFoundException) {
			throw (EntityNotFoundException) ex;
		} else if (ex instanceof DBException) {
			throw (DBException) ex;
		} else {
			throw new RuntimeException();
		}
	}
}