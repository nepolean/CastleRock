package com.real.proj.notif.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ControllerValidationHandler {


  private MessageSource msgSource;
  
  @Autowired
  public ControllerValidationHandler(MessageSource msgSource) {
    this.msgSource = msgSource;
  }
  
  public ResponseEntity<ValidationError> processValidationError(MethodArgumentNotValidException ex) {
    BindingResult binding = ex.getBindingResult();
    List<FieldError> fieldErrors = binding.getFieldErrors();
    binding.g
    ValidationError _err = new ValidationError("There is an error in the input arguments");
    _err.setFieldErrors(fieldErrors);
    ResponseEntity<ValidationError> resp = ResponseEntity.badRequest().body(_err);
    return resp;
  }
  
  
}
