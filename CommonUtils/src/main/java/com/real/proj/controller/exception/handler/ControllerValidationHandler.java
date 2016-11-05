package com.real.proj.controller.exception.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.real.proj.controller.exception.EntityNotFoundException;

@ControllerAdvice
public class ControllerValidationHandler {


  private MessageSource msgSource;
  
  @Autowired
  public ControllerValidationHandler(MessageSource msgSource) {
    this.msgSource = msgSource;
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ValidationError processValidationError(MethodArgumentNotValidException ex) {
    BindingResult binding = ex.getBindingResult();
    ValidationError _err = new ValidationError();
    if (binding.hasGlobalErrors())
      _err.setGlobalErrors(binding.getGlobalErrors());
    if (binding.hasFieldErrors())
      _err.setFieldErrors(binding.getFieldErrors());
    return _err;
  }
  
  @ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public SimpleError handleEmptyBodyForJson(HttpMessageNotReadableException ex) {
  	return new SimpleError("The request body is empty or is not formatted correctly");
  }
  
  @ExceptionHandler({com.real.proj.controller.exception.EntityNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public SimpleError handleEntityNotFound(EntityNotFoundException ex) {
  	return new SimpleError(ex.toString());
  }
}
