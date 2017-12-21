package com.subsede.user.model.amc;

public enum AmenityType {
	
	ELECTRICITY("ELECTRICITY"), WATER("WATER"), LIFT("LIFT"), GARDEN("GARDEN"), NONE("NONE");
	
	private final String name;

	AmenityType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
