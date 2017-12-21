package com.subsede.user.model.error;

public class ApplicationError {
	
	private int status;
	private String message;
	private String description;
	private String action;
	
	public ApplicationError() {
		
	}
	
	public ApplicationError(int status, String message, String description, String action) {
		super();
		this.status = status;
		this.message = message;
		this.description = description;
		this.action = action;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ApplicationError [status=" + status + ", message=" + message + ", description=" + description
				+ ", action=" + action + "]";
	}

}
