package com.real.proj.forum.service;

import com.real.proj.forum.model.Forum;

public interface IForumService {

  Forum createForum(String subject, String userName) throws Exception;

  Iterable<Forum> getForums(String userName) throws Exception;

  Forum getMessagesForForum(String forumId, int start, int count) throws Exception;

  Forum postMessage(String message, String forumId, String userId) throws Exception;

  void closeForum(String forumId, String loggedInUser) throws Exception;

  Forum getRequestedForum(String requestedBy, String forumId) throws Exception;

  Forum addUserToForum(String forumId, String loggedInUser, String targetUser) throws Exception;

  String subscribeMe(String forumId, String name) throws Exception;

}