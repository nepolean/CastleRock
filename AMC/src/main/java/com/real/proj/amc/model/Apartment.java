package com.real.proj.amc.model;

import java.io.Serializable;

import com.mongodb.util.JSON;

public class Apartment extends Asset implements Serializable {

  private static final long serialVersionUID = 1L;

  String type;
  Details details;

  public static class Details {
    int noOfFlats;
    double area;

    public Details(int noOfFlats, double area) {
      this.noOfFlats = noOfFlats;
      this.area = area;
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

  }

  public Apartment(Details details) {
    this.type = "APARTMENT";
    this.details = details;
  }

  public Details getDetails() {
    return details;
  }

  public void setDetails(Details details) {
    this.details = details;
  }

  public String getType() {
    return type;
  }

  public String toJson() {
    return JSON.serialize(this);
  }
}
