package com.real.proj.notif.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;



public class ValidationError {
  
  private String message;
  private List<FieldError> errors;
  
  public ValidationError() {
    
  }
  
  public ValidationError(String message) {
    this.setMessage(message);
  }
  
  public void setFieldErrors(List<FieldError> fieldErrors) {
    if (null != fieldErrors) {
      if (errors == null)
        errors = new ArrayList<FieldError>();
      errors.addAll(fieldErrors);
    }
  }
  
  public List<FieldError> getFieldErrors() {
    return errors;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
