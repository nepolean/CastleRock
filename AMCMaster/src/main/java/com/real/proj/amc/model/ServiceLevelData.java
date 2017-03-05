package com.real.proj.amc.model;

import java.io.Serializable;

public class ServiceLevelData implements Serializable {

  private PackageScheme scheme;

  private int visits;

  public ServiceLevelData(PackageScheme scheme, int visits) {
    this.scheme = scheme;
    this.visits = visits;
  }

  public int getVisits() {
    return visits;
  }

  public void setVisits(int visits) {
    this.visits = visits;
  }

  public PackageScheme getScheme() {
    return scheme;
  }

  public void setName(PackageScheme name) {
    this.scheme = name;
  }

  @Override
  public String toString() {
    return "ServiceLevelData [name=" + scheme + ", visits=" + visits + "]";
  }

  public boolean validate() {
    return scheme != null && visits > 0;
  }

}
