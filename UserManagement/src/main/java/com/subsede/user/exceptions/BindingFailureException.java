package com.subsede.user.exceptions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class BindingFailureException extends RuntimeException {

	private static final long serialVersionUID = -761103173139977467L;
	
	private Map<String, String> errors;
	
	public BindingFailureException(BindingResult result) {
		
		errors = new LinkedHashMap<String, String>();
        for (FieldError error : result.getFieldErrors()) {
           errors.put(error.getField(), error.getDefaultMessage());
        }

	}
	
	public Map<String, String> getErrors() {
		return this.errors;
	}
	
	public String toString() {
		return this.errors.toString();
	}

}
