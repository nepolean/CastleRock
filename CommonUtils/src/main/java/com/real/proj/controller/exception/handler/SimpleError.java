package com.real.proj.controller.exception.handler;

import java.util.UUID;

public class SimpleError {

  private int errorCode;
  private String message;
  private Category category;

  public SimpleError(int errorCode, String message, Category category) {
    this.errorCode = errorCode;
    this.category = category;
    this.message = format(message);

  }

  public int getErrorCode() {
    return this.errorCode;
  }

  public String getMessage() {
    return this.message;
  }

  private String format(String message) {
    if (category == Category.SYSTEM) {
      UUID uuid = UUID.randomUUID();
      message += " Contact support with the following reference no. " + uuid.toString();
    }
    return message;
  }

  static enum Category {
    SYSTEM, USER;
  }

}
