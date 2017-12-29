package com.subsede.amc.model.job;

import java.util.List;

import com.subsede.user.model.user.User;

public class BillPaymentJob extends AbstractJob {

  public BillPaymentJob(
      String name, 
      List<String> serviceType, 
      String sourceType, 
      String sourceId, 
      User customer) {
    super(name, serviceType, sourceType, sourceId, customer);
    // TODO Auto-generated constructor stub
  }

}
