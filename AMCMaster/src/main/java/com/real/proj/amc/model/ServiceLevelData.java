package com.real.proj.amc.model;

import java.io.Serializable;

public class ServiceLevelData implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private PackageScheme scheme;

  private int visitCount;

  /*
   * public ServiceLevelData(int visitCount) { this.visitCount = visitCount; }
   */

  public ServiceLevelData(PackageScheme scheme, int visitCount) {
    this.scheme = scheme;
    this.visitCount = visitCount;
  }

  public int getVisits() {
    return visitCount;
  }

  public void setVisits(int visits) {
    this.visitCount = visits;
  }

  public PackageScheme getScheme() {
    return scheme;
  }

  public void setScheme(PackageScheme name) {
    this.scheme = name;
  }

  @Override
  public String toString() {
    return "ServiceLevelData [name=" + scheme + ", visits=" + visitCount + "]";
  }

  public boolean validate() {
    return scheme != null && visitCount > 0;
  }

}
