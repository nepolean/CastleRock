package com.subsede.amc.model;

import com.subsede.amc.catalog.model.asset.AssetType;

public class Flat extends Asset {

  private static Flat flat = null;
  Details details;

  private Flat() {
    super("Test Flat", new Location(), AssetType.FLAT);
    details = new Details();
  }

  Flat(String name, Location location, Details details) {
    super(name, location, AssetType.FLAT);
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

  public static Flat getMetadata() {
    if (flat == null)
      flat = new Flat();
    return flat;
  }

}
