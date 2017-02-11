package com.real.proj.amc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Quotation {

  double totalAmount;
  double taxAmount;
  double discount;
  double netAmount;
  List<String> comments;
  Date createdOn;
  Date validUpto;

  Quotation(double totalAmount, double taxAmount, double discount) {
    this.totalAmount = totalAmount;
    this.taxAmount = taxAmount;
    this.discount = discount;
    this.netAmount = this.totalAmount
        + this.taxAmount
        - this.discount;
    this.createdOn = new Date();
    // valid for 30 days
    long duration = 30 * 24 * 60 * 60 * 100;
    this.validUpto = new Date(this.createdOn.getTime() + duration);
  }

  public double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(double totalAmount) {
    this.totalAmount = totalAmount;
  }

  public double getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(double taxAmount) {
    this.taxAmount = taxAmount;
  }

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public double getNetAmount() {
    return netAmount;
  }

  public void setNetAmount(double netAmount) {
    this.netAmount = netAmount;
  }

  public List<String> getComments() {
    return comments;
  }

  public void setComments(List<String> comments) {
    this.comments = comments;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public Date getValidUpto() {
    return validUpto;
  }

  public void setValidUpto(Date validUpto) {
    this.validUpto = validUpto;
  }

  public void addComments(String message) {
    if (this.comments == null)
      comments = new ArrayList<String>();
    comments.add(message);
  }

  public boolean hasExpired() {
    return System.currentTimeMillis() > this.validUpto.getTime();
  }

  @Override
  public String toString() {
    return "Quotation [totalAmount=" + totalAmount + ", taxAmount=" + taxAmount + ", discount=" + discount
        + ", netAmount=" + netAmount + "]";
  }

}
