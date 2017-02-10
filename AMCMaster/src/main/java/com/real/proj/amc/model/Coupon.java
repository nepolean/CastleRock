package com.real.proj.amc.model;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "Coupons")
public class Coupon extends BaseMasterEntity {

  @Id
  private String id;

  @NotEmpty
  private String name;
  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date validFrom;
  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date validTo;
  @Min(1)
  @Max(100)
  private double discPct;

  public Coupon() {

  }

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

  /*****************************
   *      BUISINESS LOGIC      *
   *****************************/

  public double applyDiscount(double totalAmount) throws InvalidCouponException {
    Date now = new Date();
    if (now.before(validFrom) || now.after(validTo))
      throw new InvalidCouponException("This coupon has expired.");
    return totalAmount * this.discPct / 100;
  }

}
