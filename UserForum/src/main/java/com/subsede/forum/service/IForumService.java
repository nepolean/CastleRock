package com.subsede.forum.service;

import java.util.List;

import com.subsede.forum.model.Forum;
import com.subsede.util.message.SimpleMessage;

public interface IForumService {

  Forum createForum(String subject, String userName) throws Exception;

  List<Forum> getForums(String userId) throws Exception;

  Forum getMessagesForForum(String forumId, int start, int count) throws Exception;

  Forum postMessage(String message, String forumId, String userId) throws Exception;

  void closeForum(String forumId, String loggedInUser) throws Exception;

  Forum getRequestedForum(String forumId, String requestedBy) throws Exception;

  Forum addUserToForum(String forumId, String loggedInUser, String targetUser) throws Exception;

  SimpleMessage subscribeMe(String forumId, String name) throws Exception;

  SimpleMessage unsubscribeMe(String forumId, String name) throws Exception;

  SimpleMessage removeUserFromForum(String forumId, String subscriber, String owner) throws Exception;

}