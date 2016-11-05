package com.real.proj.controller.exception.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;



public class ValidationError {
  
  private String message;
  private List<FieldLevelErrorMessage> errors;
  private int count;
  
  public ValidationError() {
    message = "Validation Errors";
  }
  
  public void setCount(int count) {
  	this.count = count;
  }
  
  public int getCount() {
    return count;
  }
  
  public String getMessage() {
    return message;
  }

  public List<FieldLevelErrorMessage> getFieldErrors() {
  	if (errors == null)
  		errors = new ArrayList<FieldLevelErrorMessage>();
    return errors;
  }


  public void setMessage(String message) {
    //this.message = message;
  }

  public void setGlobalErrors(List<ObjectError> globalErrors) {
    if (null == globalErrors)
      return;
 }

  public void setFieldErrors(List<FieldError> fieldErrors) {
    if (null != fieldErrors) {
      count = fieldErrors.size();
      for (FieldError error : fieldErrors) {
        FieldLevelErrorMessage fieldErrorMsg = new FieldLevelErrorMessage(error.getField(), error.getDefaultMessage());
        getFieldErrors().add(fieldErrorMsg);
      }
    }
  }  
  
  public static class FieldLevelErrorMessage {
    String fieldName;
    String errorMessage;
    
    public FieldLevelErrorMessage() {
      
    }
    
    public FieldLevelErrorMessage(String fieldName, String errorMessage) {
      super();
      this.fieldName = fieldName == null ? "" : fieldName;
      this.errorMessage = errorMessage == null ? "" : errorMessage;
    }

    public String getFieldName() {
      return fieldName;
    }

    public void setFieldName(String fieldName) {
      this.fieldName = fieldName;
    }

    public String getErrorMessage() {
      return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
    }
    
  }
}
