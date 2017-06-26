package com.company.model.amc;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="packagebase")
public class PackageBase {

	private String id;
	
	private String name;
	
	private String description;
	
	private List<Service> services;
	
	protected List<Amenity> defaultAmenitiesApplicableTo;
	
	private boolean active;
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void addService(Service service) {
		if(this.services == null) {
			this.services = new LinkedList<Service>();
		}
		this.services.add(service);
	}
	
	public void removeService(Service service) {
		this.services.remove(service);
	}
	
	public List<Service> getServices() {
		return this.services;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public List<Amenity> getDefaultAmenitiesApplicableTo() {
		return defaultAmenitiesApplicableTo;
	}

	public void setDefaultAmenitiesApplicableTo(List<Amenity> defaultAmenitiesApplicableTo) {
		this.defaultAmenitiesApplicableTo = defaultAmenitiesApplicableTo;
	}
	
	public void addToDefaultAmenitiesApplicableTo(Amenity amenity) {
		if(this.defaultAmenitiesApplicableTo == null)
			this.defaultAmenitiesApplicableTo = new LinkedList<Amenity>();
		this.defaultAmenitiesApplicableTo.add(amenity);
	}
	
	public void removeFromDefaultAmentiesApplicableTo(Amenity amenity) {
		this.defaultAmenitiesApplicableTo.remove(amenity);
	}

}