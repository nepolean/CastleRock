package com.subsede.util.controller.exception.handler;

import java.util.UUID;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SimpleError {

  private int errorCode;
  private String message;
  private Category category;
  private Logger logger = LogManager.getLogger(SimpleError.class);

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
    if (message == null)
      message = "[No Message To Display]";
    if (category == Category.SYSTEM) {
      UUID uuid = UUID.randomUUID();
      logger.error(uuid);
      message += " ;Contact support with the following reference no. " + uuid.toString();
    }
    return message;
  }

  static enum Category {
    SYSTEM, USER;
  }

}
