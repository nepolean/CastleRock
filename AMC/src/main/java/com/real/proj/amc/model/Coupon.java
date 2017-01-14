package com.real.proj.amc.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public abstract class Coupon {

  @Id
  private String id;
  private String name;
  private Date validFrom;
  private Date validTo;
  private int discPct;

  public abstract double applyDiscount(double totalAmount) throws InvalidCouponException;

}
