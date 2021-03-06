package com.subsede.util.controller.exception;

public class EntityNotFoundException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  String id = "";
  String message = "";
  String type = "";

  public EntityNotFoundException() {

  }
  
  public EntityNotFoundException(String id, String type) {
    this(id, "", type);
  }

  public EntityNotFoundException(String id, String message, String type) {
    super();
    this.id = id;
    this.type = type;
    this.message = toString();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String toString() {
    String msg = type + " with id," + this.getId() + ", not found";
    return msg;
  }

}
