package com.real.proj.amc.model;

import java.util.ArrayList;
import java.util.List;

public class Flat extends Asset {

  private static Flat flat = null;
  Details details;

  private Flat() {
    type = AssetType.FLAT;
    services = new ArrayList<AssetBasedService>();
    services.add(new ElectricalService());
    details = new Details();
  }

  Flat(Details details) {
    this.details = details;
  }

  public static class Details {
    int numberOfRooms;
    double area;
    UOM uom;

    public int getNumberOfRooms() {
      return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
      this.numberOfRooms = numberOfRooms;
    }

    public double getArea() {
      return area;
    }

    public void setArea(double area) {
      this.area = area;
    }

    public UOM getUom() {
      return uom;
    }

    public void setUom(UOM uom) {
      this.uom = uom;
    }

  }

  public Details getDetails() {
    return details;
  }

  public void setDetails(Details details) {
    this.details = details;
  }

  public List<AssetBasedService> getServices() {
    return services;
  }

  public void setServices(List<AssetBasedService> services) {
    this.services = services;
  }

  public static Flat getMetadata() {
    if (flat == null)
      flat = new Flat();
    return flat;
  }

}
