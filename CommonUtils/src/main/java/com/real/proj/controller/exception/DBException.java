package com.real.proj.controller.exception;

public class DBException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public DBException() {
    super("Error during db operation");
  }

  public DBException(String reason) {
    super(reason);
  }

}
