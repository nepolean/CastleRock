package com.real.proj.amc.model;

import java.io.Serializable;

public class Location implements Serializable {

  private static final long serialVersionUID = 1L;

  String plotNumber;
  String streetName;
  String area;
  String landMark;
  String city;
  String state;
  String country;
  String zipcode;
  double lattitude;
  double longitude;

  public Location() {
    plotNumber = "";
    streetName = "";
    area = "";
    landMark = "";
    city = "";
    state = "";
    state = "";
    country = "";
    zipcode = "";
    lattitude = 0.0;
    longitude = 0.0;

  }

  public Location(String plotNumber, String streetName, String area, String landMark, String city, String state,
      String country, String zipcode, double lattitude, double logitude) {
    super();
    this.plotNumber = plotNumber;
    this.streetName = streetName;
    this.area = area;
    this.landMark = landMark;
    this.city = city;
    this.state = state;
    this.country = country;
    this.zipcode = zipcode;
    this.lattitude = lattitude;
    this.longitude = logitude;
  }

  public String getPlotNumber() {
    return plotNumber;
  }

  public void setPlotNumber(String plotNumber) {
    this.plotNumber = plotNumber;
  }

  public String getStreetName() {
    return streetName;
  }

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getLandMark() {
    return landMark;
  }

  public void setLandMark(String landMark) {
    this.landMark = landMark;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public double getLattitude() {
    return lattitude;
  }

  public void setLattitude(double lattitude) {
    this.lattitude = lattitude;
  }

  public double getLogitude() {
    return longitude;
  }

  public void setLogitude(double logitude) {
    this.longitude = logitude;
  }

}
