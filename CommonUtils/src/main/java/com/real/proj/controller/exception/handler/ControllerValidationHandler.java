package com.real.proj.controller.exception.handler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
import com.real.proj.controller.exception.handler.SimpleError.Category;

@ControllerAdvice
public class ControllerValidationHandler {

  private MessageSource msgSource;
  private Logger logger = LogManager.getLogger(ControllerValidationHandler.class);

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
    print(ex);
    return new SimpleError(HttpStatus.BAD_REQUEST.value(),
        "The request body is empty or the field values are not formatted properly", SimpleError.Category.USER);
  }

  @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ResponseBody
  public SimpleError handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
    return new SimpleError(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage(), SimpleError.Category.USER);
  }

  @ExceptionHandler({ com.real.proj.controller.exception.EntityNotFoundException.class })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public SimpleError handleEntityNotFound(EntityNotFoundException ex) {
    print(ex);
    return new SimpleError(HttpStatus.NOT_FOUND.value(), ex.toString(), Category.SYSTEM);
  }

  @ExceptionHandler({ com.real.proj.controller.exception.DBException.class })
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public SimpleError handleDBExcepiton(DBException ex) {
    print(ex);
    return new SimpleError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), Category.SYSTEM);
  }

  @ExceptionHandler({ java.lang.IllegalStateException.class })
  @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
  @ResponseBody
  public SimpleError handleIllegalState(IllegalStateException ex) {
    print(ex);
    return new SimpleError(HttpStatus.PRECONDITION_FAILED.value(), ex.toString(), Category.SYSTEM);
  }

  @ExceptionHandler({ java.lang.RuntimeException.class })
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public SimpleError handleInternalFailures(RuntimeException ex) {
    print(ex);
    return new SimpleError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Unexpected error", Category.SYSTEM);
  }

  @ExceptionHandler({ com.real.proj.controller.exception.SecurityPermissionException.class })
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public SimpleError handlePermissionErrors(SecurityPermissionException ex) {
    return new SimpleError(HttpStatus.UNAUTHORIZED.value(), "You are not authorized to perform this action",
        Category.USER);
  }

  public void print(Throwable t) {
    logger.error("Error", t);
  }

}
