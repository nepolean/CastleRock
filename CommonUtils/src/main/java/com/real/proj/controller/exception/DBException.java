package com.real.proj.controller.exception;

public class DBException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public enum Operation {
    creating, updating, deleting, retrieving, adding
  }

  private DBException(String message) {
    super(message);
  }

  public DBException(String model, Operation oper) {
    super("Error while " + oper + " the " + model);
  }

  public DBException withUUID(String uuid) {
    String newMessage = this.getMessage() + ", Contact Administrator with the following correlator id = " + uuid;
    return new DBException(newMessage);
  }
}
