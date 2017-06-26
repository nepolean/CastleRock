package com.company.model.amc;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="subscription")
public class Subscription {

	private String id;
	
	@DBRef
	private SubscriptionPackageVariant subscriptionPackageVariant;
	
	private List<Amenity> amenitiesApplicableTo;
	
	private String assetApplicableTo; // It will be "Asset assetApplicableTo" later.

	private String user; // It will be "User user" later.
	
	private Date subscriptionStartDate;
	
	private Date subscriptionEndDate;
	
	private float price; // Administrator can set the final custom price for the subscription.

	public String getId() {
		return id;
	}

	public SubscriptionPackageVariant getSubscriptionPackageVariant() {
		return subscriptionPackageVariant;
	}

	public void setSubscriptionPackageVariant(SubscriptionPackageVariant subscriptionPackageVariant) {
		this.subscriptionPackageVariant = subscriptionPackageVariant;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getSubscriptionStartDate() {
		return subscriptionStartDate;
	}

	public void setSubscriptionStartDate(Date subscriptionStartDate) {
		this.subscriptionStartDate = subscriptionStartDate;
	}

	public Date getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	public void setSubscriptionEndDate(Date subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	public List<Amenity> getAmenitiesApplicableTo() {
		return amenitiesApplicableTo;
	}

	public void setAmenitiesApplicableTo(List<Amenity> amenitiesApplicableTo) {
		this.amenitiesApplicableTo = amenitiesApplicableTo;
	}
	
	public void addAmenityApplicableTo(Amenity amenity) {
		if(this.amenitiesApplicableTo == null) {
			this.amenitiesApplicableTo = new LinkedList<Amenity>();
		}
		this.amenitiesApplicableTo.add(amenity);
	}
	
	public void removeAmenityApplicableTo(Amenity amenity) {
		this.amenitiesApplicableTo.remove(amenity);
	}

	public String getAssetApplicableTo() {
		return assetApplicableTo;
	}

	public void setAssetApplicableTo(String assetApplicableTo) {
		this.assetApplicableTo = assetApplicableTo;
	}

}