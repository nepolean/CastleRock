package com.company.model.amc;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="maintenanceservice")
public class LiftMaintenanceService extends Service {

	public LiftMaintenanceService() {
		this.setName("Lift Service");
		this.setDescription("Lift maintenance service description");
		this.addAmenityTypeApplicableTo(AmenityType.LIFT);
	}

	@Override
	public void setDescription(String description) {
		super.setDescription(description);
	}

}