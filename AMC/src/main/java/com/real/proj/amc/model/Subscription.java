package com.real.proj.amc.model;

import java.util.Date;
import java.util.List;

public class Subscription {

  List<AMCPackage> packages;
  Date createdOn;
  Date subscribedOn;
  Quotation quotation;
  boolean isPaid;
  SubscriptionStatus status;

  public SubscriptionStatus getStatus() {
    return status;
  }

  public List<AMCPackage> getsubscribedPackages() {
    return packages;
  }

}
