package com.subsede.amc.model.job;

import com.subsede.amc.catalog.model.ServiceType;
import com.subsede.user.model.user.User;

public class BillPaymentJob extends AbstractJob {

  public BillPaymentJob(String name, ServiceType serviceType, String sourceType, String sourceId, User customer) {
    super(name, serviceType, sourceType, sourceId, customer);
    // TODO Auto-generated constructor stub
  }

}
