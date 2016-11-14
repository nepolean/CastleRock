package com.real.proj.forum.service;

import java.util.List;

import com.real.proj.forum.model.Forum;
import com.real.proj.message.SimpleMessage;

public interface IForumService {

  Forum createForum(String subject, String userName) throws Exception;

  List<Forum> getForums(String userName) throws Exception;

  Forum getMessagesForForum(String forumId, int start, int count) throws Exception;

  Forum postMessage(String message, String forumId, String userId) throws Exception;

  void closeForum(String forumId, String loggedInUser) throws Exception;

  Forum getRequestedForum(String forumId, String requestedBy) throws Exception;

  Forum addUserToForum(String forumId, String loggedInUser, String targetUser) throws Exception;

  SimpleMessage subscribeMe(String forumId, String name) throws Exception;

}