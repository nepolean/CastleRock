package com.real.proj.amc.model;

public class ServiceLevelData {

  public static enum Scheme {
    PLATINUM, GOLD, SILVER
  }

  private PackageScheme name;

  private int visits;

  public ServiceLevelData(PackageScheme name, int visits) {
    this.name = name;
    this.visits = visits;
  }

  public int getVisits() {
    return visits;
  }

  public void setVisits(int visits) {
    this.visits = visits;
  }

  public PackageScheme getName() {
    return name;
  }

  public void setName(PackageScheme name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "ServiceLevelData [name=" + name + ", visits=" + visits + "]";
  }

  public boolean validate() {
    return name != null && visits > 0;
  }

}
