package com.subsede.amc.catalog.model.deleted;

import com.subsede.amc.catalog.model.PackageScheme;

public class PackagePriceInfo {

  private PackageScheme scheme;
  private double actualPrice;
  private double discount;

  public PackagePriceInfo(double actualPrice, double discount) {
    // this.scheme = scheme;
    this.actualPrice = actualPrice;
    this.discount = discount;
  }

  public PackageScheme getScheme() {
    return scheme;
  }

  public void setScheme(PackageScheme scheme) {
    this.scheme = scheme;
  }

  public double getActualPrice() {
    return actualPrice;
  }

  public void setActualPrice(double actualPrice) {
    this.actualPrice = actualPrice;
  }

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  @Override
  public String toString() {
    return "PackagePriceInfo [scheme=" + scheme + ", actualPrice=" + actualPrice + ", discount=" + discount + "]";
  }

}
