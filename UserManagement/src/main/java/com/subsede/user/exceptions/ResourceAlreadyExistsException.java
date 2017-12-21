package com.subsede.user.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 2015439080847897940L;

	private final String messageTemplate = "Sorry. %s already exists.";
	
	private String resourceName = "Resource";
	
	public ResourceAlreadyExistsException() {
		
	}
	
	public ResourceAlreadyExistsException(String resourceName) {
		this.resourceName = resourceName;
	}
	
	public String getError() {
		return this.toString();
	}
	
	public String toString() {
		return String.format(messageTemplate, resourceName);
	}

}