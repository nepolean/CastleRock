package com.real.proj.amc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.util.JSON;

public class Apartment extends Asset implements Serializable {

  private static final long serialVersionUID = 1L;

  Details details;

  private Apartment() {
    super();
    type = AssetType.APARTMENT;
    this.setDetails(new Details(0, 0, 0.0, UOM.SFT));
  }

  public Apartment(Details details) {
    this.details = details;
    type = AssetType.APARTMENT;
  }

  public Details getDetails() {
    return details;
  }

  public void setDetails(Details details) {
    this.details = details;
  }

  public AssetType getType() {
    return type;
  }

  public String toJson() {
    return JSON.serialize(this);
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
