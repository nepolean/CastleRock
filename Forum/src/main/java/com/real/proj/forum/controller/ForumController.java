package com.real.proj.forum.controller;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.real.proj.controller.exception.DBException;
import com.real.proj.controller.exception.EntityNotFoundException;
import com.real.proj.controller.exception.SecurityPermissionException;
import com.real.proj.forum.model.Forum;
import com.real.proj.forum.model.User;
import com.real.proj.forum.service.ForumService;

@RestController
public class ForumController {
  private static final Logger logger = LogManager.getLogger(ForumController.class);
  // @Autowired
  private ForumService forumService = new ForumService();

  @RequestMapping(path = { "/forum/create" }, method = { RequestMethod.POST }, produces = { "application/json" })
  public Forum createForum(@Validated @RequestBody String subject, Principal loggedInUser)
      throws EntityNotFoundException {
    try {
      return this.forumService.createForum(subject, loggedInUser.getName());
    } catch (Exception ex) {
      this.handleException(ex);
      return null;
    }
  }

  @RequestMapping(path = { "/forum/{forumId}" }, method = { RequestMethod.GET }, produces = { "application/json" })
  public Forum getForum(@Valid @PathVariable String forumId, Principal loggedInUser, WebRequest request)
      throws EntityNotFoundException, SecurityPermissionException {
    Forum f = null;
    try {
      f = this.forumService.getForum(forumId);
    } catch (Exception ex) {
      this.handleException(ex);
    }
    if (!this.hasRequiredPermission(loggedInUser, request, f)) {
      if (logger.isErrorEnabled())
        logger.error("User, " + loggedInUser.getName() + " , is not permitted to view the forum");
      throw new SecurityPermissionException();
    }
    return f;
  }

  @RequestMapping(path = { "/forum/{forumId}/subscribe" }, method = { RequestMethod.POST }, produces = {
      "application/json" })
  public Forum subscribeMe(@PathVariable String forumId, Principal loggedInUser) throws EntityNotFoundException {
    try {
      return this.forumService.subscribeUser(forumId, loggedInUser.getName());
    } catch (Exception ex) {
      this.handleException(ex);
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
    } catch (Exception ex) {
      this.handleException(ex);
    }
    if (request.isUserInRole("sa") || f.getOwner().getEmail().equals(userId)) {
      try {
        this.forumService.addSubscriber(forumId, userId);
      } catch (Exception ex) {
        this.handleException(ex);
      }

    } else {
      throw new SecurityPermissionException();
    }
  }

  @RequestMapping(path = { "/forum" }, method = { RequestMethod.GET }, produces = { "application/json" })
  public Iterable<Forum> forumsBelongingTo(Principal loggedInUser) throws EntityNotFoundException {
    try {
      return this.forumService.getForums(loggedInUser.getName());
    } catch (Exception ex) {
      this.handleException(ex);
      return null;
    }
  }

  @RequestMapping(path = { "/forum/{forumId}/post" }, method = { RequestMethod.POST })
  public void postTextMessage(@PathVariable String forumId, @RequestBody String message, Principal loggedInUser,
      WebRequest request) throws EntityNotFoundException, SecurityPermissionException {
    if (logger.isDebugEnabled())
      logger.debug("posting new message to " + forumId);
    // security check
    Forum f = null;
    try {
      f = forumService.getForum(forumId);
    } catch (Exception ex) {
      handleException(ex);
    }
    if (!this.hasRequiredPermission(loggedInUser, request, f)) {
      if (logger.isDebugEnabled())
        logger.debug("User does not have permission to post to this forum " + loggedInUser.getName());
      throw new SecurityPermissionException();
    }
    try {
      forumService.postMessage(message, forumId, loggedInUser.getName());
    } catch (Exception ex) {
      handleException(ex);
    }
  }

  public void uploadFile(Long forumId, Principal loggedInUser) {
  }

  public void inviteOthersByEmail(List<String> emails, Principal loggedInUser) {
  }

  private boolean hasRequiredPermission(Principal loggedInUser, WebRequest request, Forum f) {
    return request.isUserInRole("sa") || this.userBelongsToForum(f, loggedInUser.getName());
  }

  private boolean userBelongsToForum(Forum f, String user) {
    Iterator<User> subscribers = f.getSubscribers().iterator();
    while (subscribers.hasNext()) {
      User usr = (User) subscribers.next();
      if (usr.getEmail().equals(user)) {
        return true;
      }
    }

    return false;
  }

  private void handleException(Exception ex) throws EntityNotFoundException {
    String uuid = UUID.randomUUID().toString();
    logger.warn(uuid);
    if (logger.isErrorEnabled())
      logger.error("Error ", ex);
    if (ex instanceof EntityNotFoundException) {
      throw (EntityNotFoundException) ex;
    } else if (ex instanceof DBException) {
      throw new DBException(uuid);
    } else {
      throw new RuntimeException(uuid);
    }
  }
}