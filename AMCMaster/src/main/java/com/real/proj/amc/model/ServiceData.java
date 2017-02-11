package com.real.proj.amc.model;

import java.util.Comparator;
import java.util.Date;

public class ServiceData implements Comparator<Date> {

  private int visits;
  private double price;
  private Date validFrom;
  private Date validTo;

  public ServiceData(int visits, double price, Date validFrom) {
    this.visits = visits;
    this.price = price;
    this.validFrom = validFrom;
  }

  public int getVisits() {
    return visits;
  }

  public void setVisits(int visits) {
    this.visits = visits;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Date getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(Date validFrom) {
    this.validFrom = validFrom;
  }

  public Date getValidTo() {
    return validTo;
  }

  public void setValidTo(Date validTo) {
    this.validTo = validTo;
  }

  @Override
  public String toString() {
    return "ServiceData [visits=" + visits + ", price=" + price + ", validFrom=" + validFrom + ", validTo=" + validTo
        + "]";
  }

  public boolean isInRange(Date givenDate) {
    if (validTo == null) {
      return givenDate.after(validFrom);
    }
    return validFrom.before(givenDate) && validTo.after(givenDate);
  }

  @Override
  public int compare(Date arg0, Date arg1) {
    return 0;
  }

}
