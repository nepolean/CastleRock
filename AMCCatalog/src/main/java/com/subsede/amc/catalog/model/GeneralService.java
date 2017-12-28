package com.subsede.amc.catalog.model;

import java.util.List;

public class GeneralService extends BaseService {

  public GeneralService(
      Category category,
      String name,
      String description,
      List<String> serviceType) {
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
