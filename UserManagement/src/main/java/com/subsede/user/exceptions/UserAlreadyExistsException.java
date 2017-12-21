package com.subsede.user.exceptions;

public class UserAlreadyExistsException extends ResourceAlreadyExistsException {

	private static final long serialVersionUID = -2171457209799225294L;

	public UserAlreadyExistsException() {
		super("User");
	}
	
	public UserAlreadyExistsException(String resource) {
		super(resource);
	}

}