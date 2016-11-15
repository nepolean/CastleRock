package com.real.proj.message;

public class SimpleMessage {

  private String message;

  public SimpleMessage() {

  }

  public SimpleMessage(String message) {
    this.message = (message == null) ? "" : message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
