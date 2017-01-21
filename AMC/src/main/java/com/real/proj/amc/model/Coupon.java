package com.real.proj.amc.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Coupon extends BaseMasterEntity {

  @Id
  private String id;
  private String name;
  private Date validFrom;
  private Date validTo;
  private double discPct;

  public Coupon(String name, Date validFrom, Date validTo, double discPct) {
    super();
    this.name = name;
    this.validFrom = validFrom;
    this.validTo = validTo;
    this.discPct = discPct;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public double getDiscPct() {
    return this.discPct;
  }

  public void setDiscPct(double discPct) {
    this.discPct = discPct;
  }

  /******************************
   * BUISINESS LOGIC
   *****************************/

  public double applyDiscount(double totalAmount) throws InvalidCouponException {
    return totalAmount * this.discPct / 100;
  }

}
