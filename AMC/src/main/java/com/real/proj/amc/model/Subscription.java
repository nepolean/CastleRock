package com.real.proj.amc.model;

import java.util.Date;
import java.util.List;

public class Subscription {

  List<Package> packages;
  Date createdOn;
  Date subscribedOn;
  Quotation quotation;
  boolean isPaid;
  SubscriptionStatus status;

  public SubscriptionStatus getStatus() {
    return status;
  }

  public List<Package> getsubscribedPackages() {
    return packages;
  }

}
