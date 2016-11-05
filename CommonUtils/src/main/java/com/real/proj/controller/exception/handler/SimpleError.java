package com.real.proj.controller.exception.handler;

public class SimpleError {

  String message;

  public SimpleError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}
