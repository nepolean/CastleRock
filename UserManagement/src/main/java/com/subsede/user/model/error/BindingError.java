package com.subsede.user.model.error;

import java.util.Map;

public class BindingError extends ApplicationError {
	
	private Map<String, String> errors;
	
	public BindingError() {
		
	}
	
	public BindingError(Map<String, String> errors) {
		super(	422,
				"VALIDATION FAILED",
				"The request could not be processed due to validation failures.",
				"Please correct the errors for the fields given here."
		);
		this.errors = errors;
	}
	
	public Map<String, String> getErrors() {
		return this.errors;
	}

	@Override
	public String toString() {
		return "BindingError [errors=" + errors + ", " + super.toString()	+ "]";
	}
}