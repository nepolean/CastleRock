package com.real.proj.amc.model;

public enum PackageScheme {

  PLATINUM("Platinum"), GOLD("Gold"), SILVER("Silver");

  private String scheme;

  PackageScheme(String scheme) {
    this.scheme = scheme;
  }

  public String toString() {
    return scheme;
  }
}
