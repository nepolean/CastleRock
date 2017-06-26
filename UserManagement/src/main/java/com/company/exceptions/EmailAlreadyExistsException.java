package com.company.exceptions;

public class EmailAlreadyExistsException extends ResourceAlreadyExistsException {

	private static final long serialVersionUID = -2171457209799225294L;

	public EmailAlreadyExistsException() {
		super("Email");
	}
	
	public EmailAlreadyExistsException(String resource) {
		super(resource);
	}

}