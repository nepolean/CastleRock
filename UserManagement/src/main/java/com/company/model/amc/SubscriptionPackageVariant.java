package com.company.model.amc;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="packagevariant")
public class SubscriptionPackageVariant extends PackageVariant {

	protected int period; // In months
	
	protected Map<Service, Integer> serviceVisitCountMap;

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public Map<Service, Integer> getServiceVisitCountMap() {
		return serviceVisitCountMap;
	}

	public void setServiceVisitCountMap(Map<Service, Integer> serviceVisitCountMap) {
		this.serviceVisitCountMap = serviceVisitCountMap;
	}

}