package com.company.model.amc;

public enum PackageType {
	
	SERVICE_PACKAGE("SERVICE PACKAGE"), SUBSCRIPTION_PACKAGE("SUBSCRIPTION PACKAGE");

	private final String name;

	PackageType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
