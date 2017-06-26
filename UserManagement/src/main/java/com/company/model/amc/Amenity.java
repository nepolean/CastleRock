package com.company.model.amc;

import org.springframework.data.annotation.Id;

public abstract class Amenity {

	@Id
	protected String id;
	
	protected String amenityName;
	
	protected String title;
	
	public String getId() {
		return id;
	}

	public String getAmenityName() {
		return amenityName;
	}

	public void setAmenityName(String amenityName) {
		this.amenityName = amenityName;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
}