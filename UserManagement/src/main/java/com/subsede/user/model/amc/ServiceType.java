package com.subsede.user.model.amc;

public enum ServiceType {
	
	ELECTRICITY_SERVICE("ELECTRICITY SERVICE"), 
	WATER_SERVICE("WATER SERVICE"), 
	LIFT_SERVICE("LIFT SERVICE"), 
	GARDEN_SERVICE("GARDEN SERVICE");
	
	private final String name;

	ServiceType(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

}
