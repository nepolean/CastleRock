package com.real.proj.amc.model;

public class GeneralService extends BaseService {

  /**
   * default serialVersionUID
   */
  private static final long serialVersionUID = 1L;

  public GeneralService() {

  }

  @Override
  protected void rejectIfDataIsNotValid(DeliveryMethod delivery, ServiceMetadata sld) {
    // TODO Auto-generated method stub

  }

  @Override
  protected UserInput<String, Object> getDefaultInput() {
    // TODO Auto-generated method stub
    return null;
  }

}
