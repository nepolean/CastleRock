package com.real.proj.amc.model.job;

import com.real.proj.amc.model.ServiceType;
import com.real.proj.user.model.User;

public class BillPaymentJob extends AbstractJob {

  public BillPaymentJob(String name, ServiceType serviceType, String sourceType, String sourceId, User customer) {
    super(name, serviceType, sourceType, sourceId, customer);
    // TODO Auto-generated constructor stub
  }

}
