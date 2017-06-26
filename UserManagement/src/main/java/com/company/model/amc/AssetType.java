package com.company.model.amc;

public enum AssetType {
	
	NONE("NONE"), APARTMENT("APARTMENT"), FLAT("FLAT"), VENTURE("VENTURE"), PLOT("PLOT"), HOUSE("HOUSE");

	private final String name;

	AssetType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	
}
