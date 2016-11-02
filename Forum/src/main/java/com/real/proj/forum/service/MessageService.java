package com.real.proj.forum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.real.proj.forum.model.Forum;
import com.real.proj.forum.model.Message;

@Service("messageService")
public class MessageService {
  
  @Autowired
  static MessageRepository repository;

  public static List<Message> getMessages(Forum f, int start, int count) {
    return repository.findByForumId(f.getId(), new PageRequest(start, count, Sort.DEFAULT_DIRECTION));
  }

}
