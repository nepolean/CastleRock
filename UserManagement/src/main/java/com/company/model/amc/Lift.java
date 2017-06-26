package com.company.model.amc;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="amenity")
public class Lift extends Amenity {

	private String brand;
	
	private String model;
	
	private int capacity;

	public Lift() {
		super.setAmenityName(AmenityType.LIFT.getName());
		super.setTitle("Lift title");
	}
	
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		this.model = model;
	}


	public int getCapacity() {
		return capacity;
	}


	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

}