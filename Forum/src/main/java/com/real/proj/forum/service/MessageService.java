package com.real.proj.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("messageService")
public class MessageService {

  @Autowired
  static MessageRepository repository;

  /*
   * public static List<Message> getMessages(Forum f, int start, int count) {
   * return repository.findByForumId(f.getId(), new PageRequest(start, count,
   * Sort.DEFAULT_DIRECTION)); }
   */
}
