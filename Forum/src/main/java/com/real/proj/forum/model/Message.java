package com.real.proj.forum.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Message {
  @Id
  String id;
  Long forumId;
  String message;
  Date postedDate;
  User postedBy;
  String type;
  

}
