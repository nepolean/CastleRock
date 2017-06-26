package com.company.exceptions;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3778850162574008446L;
	
	private final String messageTemplate = "Sorry. %s could not be found.";
	
	private String resourceName = "Resource";
	
	public ResourceNotFoundException() {
		
	}
	
	public ResourceNotFoundException(String resourceName) {
		this.resourceName = resourceName;
	}
	
	public String getError() {
		return this.toString();
	}
	
	public String toString() {
		return String.format(messageTemplate, resourceName);
	}

}