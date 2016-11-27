package com.real.proj.controller.exception.handler;

public class SimpleError {

  int errorCode;
  String message;

  public SimpleError(int errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }

  public int getErrorCode() {
    return this.errorCode;
  }

  public String getMessage() {
    return this.message;
  }
}
