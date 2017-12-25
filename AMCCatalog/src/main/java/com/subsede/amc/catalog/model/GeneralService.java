package com.subsede.amc.catalog.model;

public class GeneralService extends BaseService {

  public GeneralService(
      Category category,
      String name,
      String description,
      ServiceType serviceType) {
    super(category, name, description, serviceType);
  }

  @Override
  protected void rejectIfDataIsNotValid(DeliveryMethod delivery, ServiceMetadata sld) {

  }

  @Override
  protected UserInput<String, Object> getDefaultInput() {
    return null;
  }

}
