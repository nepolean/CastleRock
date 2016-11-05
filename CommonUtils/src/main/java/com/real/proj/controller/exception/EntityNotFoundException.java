package com.real.proj.controller.exception;

public class EntityNotFoundException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  String id = "";
  String message = "";
  String type = "";

  public EntityNotFoundException() {

  }

  public EntityNotFoundException(String id, String message, String type) {
    super();
    this.id = id;
    this.message = message;
    this.type = type;
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
    String msg = type + "with id," + this.getId() + " , not found";
    return msg;
  }

}
