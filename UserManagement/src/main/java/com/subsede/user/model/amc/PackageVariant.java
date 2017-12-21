package com.subsede.user.model.amc;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="packagevariant")
public abstract class PackageVariant {

	private String id;

	private String name;
	
	protected String description;
	
	protected PackageBase packageBase;
	
	protected boolean active;
	
	protected float price;
	
	protected float discount; // Absolute discount
	
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

	public PackageBase getPackageBase() {
		return packageBase;
	}

	public void setPackageBase(PackageBase packageBase) {
		this.packageBase = packageBase;
	}
	
	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}
	
	public void setDiscountByPercentage(float discountPercentage) {
		this.discount = this.price * discountPercentage / 100;
	}

}