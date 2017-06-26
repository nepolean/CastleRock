package com.company.exception.handlers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.company.controllers.HTMLControllerMarker;
import com.company.controllers.rest.user.RestControllerMarker;
import com.company.exceptions.BindingFailureException;
import com.company.exceptions.EmailAlreadyExistsException;
import com.company.exceptions.UserAlreadyExistsException;
import com.company.exceptions.UserNotFoundException;
import com.company.exceptions.VerificationTokenNotFoundException;
import com.company.model.error.BindingError;

@ControllerAdvice(basePackageClasses = { HTMLControllerMarker.class, RestControllerMarker.class })
public class GenericExceptionHandler {

	private @Autowired HttpServletRequest request;
	
	private static final Logger logger = Logger.getLogger(GenericExceptionHandler.class);
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="USER could not be found")
	@ExceptionHandler(UserNotFoundException.class)
	public void handleUserNotFoundException(UserNotFoundException e) {

	}
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="USER could not be found")
	@ExceptionHandler(VerificationTokenNotFoundException.class)
	public void handleVerificationTokenNotFoundException(VerificationTokenNotFoundException e) {
		
	}
	
	@ResponseStatus(value=HttpStatus.CONFLICT, reason="USER with the username already exists")
	@ExceptionHandler(UserAlreadyExistsException.class)
	public void handleUserAlreadyExistsException(UserAlreadyExistsException e) {
		
	}
	
	@ResponseStatus(value=HttpStatus.CONFLICT, reason="USER with the email already exists")
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public void handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
		
	}
	
	@ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason="User not authorized")
	@ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
	public void handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException e) {

	}
	
	@ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason="Credentials not valid")
	@ExceptionHandler(BadCredentialsException.class)
	public void handleBadCredentialsException(BadCredentialsException e) {

	}
	
	@ExceptionHandler(BindingFailureException.class)
	public ResponseEntity<BindingError> handleBindingFailureException(BindingFailureException e) {
		BindingError bindingError = new BindingError(e.getErrors());
		return new ResponseEntity<BindingError>(bindingError, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(BindException.class)
	public ModelAndView handleInvalidArgumentException(BindException e) {
		request.setAttribute("status", 422);
		request.setAttribute("message", "INCORRECT PARAMETERS");
		request.setAttribute("description","Parameters supplied were incorrect.");
		return new ModelAndView("forward:/oops");
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ModelAndView handleInvalidArgumentException(MethodArgumentNotValidException e) {
		request.setAttribute("status", 422);
		request.setAttribute("message", "INCORRECT PARAMETERS");
		request.setAttribute("description","Parameters supplied were incorrect.");
		return new ModelAndView("forward:/oops");
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleCustomException(Exception e) {
		long timestamp = System.currentTimeMillis();
		logger.error("Oops something went wrong at " + timestamp + "!", e);
		request.setAttribute("status", 500);
		request.setAttribute("message", "APPLICATION ERROR");
		request.setAttribute("description","Something went wrong.");
		request.setAttribute("action", 
				"We are sorry to tell you that something went wrong in our system. " + 
				"Please try again or contact our administrator with the code " + timestamp +
				" for support. Sorry for the inconvenience.");
		return new ModelAndView("forward:/oops");

	}

//	@ExceptionHandler(MissingServletRequestParameterException.class)
//	public ModelAndView handleMissingParameterException(Exception e) {
//		logger.error("Required parameter not found!", e);
//		return new ModelAndView("/oops","errorMessage","Oops something went wrong!");
//	}
}