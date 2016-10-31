package com.real.proj.forum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.forum.model.Forum;
import com.real.proj.forum.service.ForumService;

@RestController
public class ForumController {
  
  @RequestMapping(name="/forum/create", method=RequestMethod.POST, produces="application/json")
  public ResponseEntity<Object> create(@RequestBody String subject) {
    Forum forum;
    try {
      forum = ForumService.createForum(subject);
      return new ResponseEntity<Object>(forum, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
  }
  

}
