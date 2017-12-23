package com.subsede.amc.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.mongodb.util.JSON;
import com.subsede.amc.catalog.model.asset.AssetType;

public class Apartment extends Asset {

  Details details;

  private Apartment() {
    super("Test", new Location(), AssetType.APARTMENT);
    type = AssetType.APARTMENT;
    this.setDetails(new Details(0, 0, 0.0, UOM.SFT));
  }

  public Apartment(String name, Location location, Details details) {
    super(name, location, AssetType.APARTMENT);
    this.details = details;
    type = AssetType.APARTMENT;
  }

  public Details getDetails() {
    return details;
  }

  public void setDetails(Details details) {
    this.details = details;
  }

  public String toJson() {
    return JSON.serialize(this);
  }

  public static Apartment getMetadata() {
    return  new Apartment();
  }

  public static class Block {
    private int name;
    private int noOfFlats;
  }

  public static class Details {
    private int noOfFlats;
    private double area;
    private UOM uom;
    private int noOfBlocks;
    private List<Block> blocks;

    public Details(int noOfBlocks, int noOfFlats, double area, UOM uom) {
      this.noOfBlocks = noOfBlocks;
      this.noOfFlats = noOfFlats;
      this.area = area;
      this.uom = uom;
      blocks = new ArrayList<Block>(noOfBlocks);
    }

    public int getNoOfFlats() {
      return noOfFlats;
    }

    public void setNoOfFlats(int noOfFlats) {
      this.noOfFlats = noOfFlats;
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
}
