package com.subsede.forum.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.subsede.util.user.model.User;

@Document
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  String id;
  String message;
  Date postedDate;
  User author;

  MessageType type;

  FileData fileProperties;

  static class FileData {
    String path;
    String checksum;
    int size;
  }

  public Message(MessageType type) {
    this.type = type;
    postedDate = new Date();
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Date getPostedDate() {
    return postedDate;
  }

  public void setPostedDate(Date postedDate) {
    this.postedDate = postedDate;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }

  public FileData getFileProperties() {
    return fileProperties;
  }

  public void setFileProperties(FileData fileProperties) {
    this.fileProperties = fileProperties;
  }

}
