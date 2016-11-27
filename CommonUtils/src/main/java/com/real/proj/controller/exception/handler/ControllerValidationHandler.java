package com.real.proj.controller.exception.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.real.proj.controller.exception.DBException;
import com.real.proj.controller.exception.EntityNotFoundException;
import com.real.proj.controller.exception.SecurityPermissionException;

@ControllerAdvice
public class ControllerValidationHandler {

  private MessageSource msgSource;

  @Autowired
  public ControllerValidationHandler(MessageSource msgSource) {
    this.msgSource = msgSource;
  }

  @ExceptionHandler({ MethodArgumentNotValidException.class })
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

  @ExceptionHandler({ org.springframework.http.converter.HttpMessageNotReadableException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public SimpleError handleEmptyBodyForJson(HttpMessageNotReadableException ex) {
    return new SimpleError(HttpStatus.BAD_REQUEST.value(), "The request body is empty or is not formatted correctly");
  }

  @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ResponseBody
  public SimpleError handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
    return new SimpleError(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage());
  }

  @ExceptionHandler({ com.real.proj.controller.exception.EntityNotFoundException.class })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public SimpleError handleEntityNotFound(EntityNotFoundException ex) {
    return new SimpleError(HttpStatus.NOT_FOUND.value(), ex.toString());
  }

  @ExceptionHandler({ com.real.proj.controller.exception.DBException.class })
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public SimpleError handleDBExcepiton(DBException ex) {
    return new SimpleError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
  }

  @ExceptionHandler({ java.lang.IllegalStateException.class })
  @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
  @ResponseBody
  public SimpleError handleIllegalState(IllegalStateException ex) {
    return new SimpleError(HttpStatus.PRECONDITION_FAILED.value(), ex.toString());
  }

  @ExceptionHandler({ java.lang.RuntimeException.class })
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public SimpleError handleInternalFailures(RuntimeException ex) {
    return new SimpleError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Unexpected error. Please contact the admin with the following key:" + ex.getMessage());
  }

  @ExceptionHandler({ com.real.proj.controller.exception.SecurityPermissionException.class })
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public SimpleError handlePermissionErrors(SecurityPermissionException ex) {
    return new SimpleError(HttpStatus.UNAUTHORIZED.value(), "You are not authorized to perform this action");
  }

}
