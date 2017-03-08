package com.real.proj.amc.model;

public enum PackageScheme {

  PLATINUM("PLATINUM"), GOLD("GOLD"), SILVER("SILVER");

  private String scheme;

  PackageScheme(String scheme) {
    this.scheme = scheme;
  }

  public String getScheme() {
    return this.scheme;
  }

}
