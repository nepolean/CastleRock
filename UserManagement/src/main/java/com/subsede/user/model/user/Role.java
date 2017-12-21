package com.subsede.user.model.user;

import javax.validation.constraints.NotNull;

public class Role {

	private String id;
	
	@NotNull
	private String name;

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Role(String name) {
		this.name = name;
	}
	
}