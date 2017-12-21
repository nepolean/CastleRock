package com.subsede.user.model.amc;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="service")
public abstract class Service {

	private String id;
	
	private String name;
	
	protected String description;
	
	protected List<AmenityType> amenityTypesApplicableTo;
	
	protected List<AssetType> assetTypesApplicableTo;
	
	protected boolean active;
	
	protected float price;
	
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
	
	public void addAmenityTypeApplicableTo(AmenityType amenityType) {
		if(this.amenityTypesApplicableTo == null) {
			this.amenityTypesApplicableTo = new LinkedList<AmenityType>();
		}
		this.amenityTypesApplicableTo.add(amenityType);
	}
	
	public void removeAmenityTypeApplicableTo(AmenityType amenityType) {
		this.amenityTypesApplicableTo.remove(amenityType);
	}
	
	public List<AmenityType> getAmenityTypesApplicableTo() {
		return this.amenityTypesApplicableTo;
	}
	
	public void addAssetTypeApplicableTo(AssetType assetType) {
		if(this.assetTypesApplicableTo == null) {
			this.assetTypesApplicableTo = new LinkedList<AssetType>();
		}
		this.assetTypesApplicableTo.add(assetType);
	}
	
	public void removeAssetTypeApplicableTo(AssetType assetType) {
		this.assetTypesApplicableTo.remove(assetType);
	}
	
	public List<AssetType> getAssetTypesApplicableTo() {
		return this.assetTypesApplicableTo;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

}