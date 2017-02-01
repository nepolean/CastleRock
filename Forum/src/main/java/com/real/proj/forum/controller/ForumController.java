package com.real.proj.forum.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.real.proj.forum.model.Forum;
import com.real.proj.forum.service.IForumService;
import com.real.proj.message.SimpleMessage;

@RestController
public class ForumController {
  private static final Logger logger = LogManager.getLogger(ForumController.class);
  @Autowired
  private IForumService forumService;

  @RequestMapping(path = { "/forum/create" }, method = { RequestMethod.POST }, produces = { "application/json" })
  public Forum createForum(@Validated @RequestBody String subject, Principal loggedInUser) throws Exception {
    return this.forumService.createForum(subject, loggedInUser.getName());
  }

  @RequestMapping(path = { "/forum/{forumId}" }, method = { RequestMethod.GET }, produces = { "application/json" })
  public Forum getForum(@Valid @PathVariable String forumId, Principal loggedInUser, WebRequest request)
      throws Exception {
    return this.forumService.getRequestedForum(forumId, loggedInUser.getName());
  }

  // TODO: Result must be handled correctly
  @RequestMapping(path = { "/forum/{forumId}/subscribe" }, method = { RequestMethod.POST }, produces = {
      "application/json" })
  public SimpleMessage subscribeMe(@Valid @PathVariable String forumId, Principal loggedInUser) throws Exception {
    return this.forumService.subscribeMe(forumId, loggedInUser.getName());

  }

  @RequestMapping(path = { "/forum/{forumId}/subscribe/{userId}" }, method = { RequestMethod.POST }, produces = {
      "application/json" })
  public ResponseEntity<String> subscribeUser(@Valid @PathVariable String forumId, @Valid @PathVariable String userId,
      Principal loggedInUser, WebRequest request) throws Exception {
    this.forumService.addUserToForum(forumId, loggedInUser.getName(), userId);
    return ResponseEntity.ok("successful");
  }

  @RequestMapping(path = { "/forum" }, method = { RequestMethod.GET }, produces = { "application/json" })
  public List<Forum> forumsBelongingTo(Principal loggedInUser) throws Exception {
    List<Forum> response = this.forumService.getForums(loggedInUser.getName());
    return response;
  }

  @RequestMapping(path = { "/forum/{forumId}/post" }, method = { RequestMethod.POST })
  public ResponseEntity<String> postTextMessage(@Valid @PathVariable String forumId, @Valid @RequestBody String message,
      Principal loggedInUser, WebRequest request) throws Exception {
    if (logger.isDebugEnabled())
      logger.debug("posting new message to " + forumId);
    Forum f = forumService.postMessage(message, forumId, loggedInUser.getName());
    return ResponseEntity.ok("successful");
  }

  @RequestMapping(path = "/forum/{forumId}/close", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public void closeForum(@Valid @PathVariable String forumId, Principal loggedInUser) throws Exception {
    this.forumService.closeForum(forumId, loggedInUser.getName());
  }

  public void uploadFile(Long forumId, Principal loggedInUser) {
  }

  @RequestMapping(value = "/forum/{forumId}/unsubscribe", produces = MediaType.APPLICATION_JSON_VALUE)
  public SimpleMessage unsubscribeMe(@Valid @PathVariable String forumId, Principal loggedInUser) throws Exception {
    return forumService.unsubscribeMe(forumId, loggedInUser.getName());
  }

  @RequestMapping(value = "/forum/{forumId}/unsubscribe/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public SimpleMessage unsubscribeUser(@Valid @PathVariable String forumId, @Valid @PathVariable String userId,
      Principal loggedInUser) throws Exception {
    return forumService.removeUserFromForum(forumId, userId, loggedInUser.getName());
  }

}