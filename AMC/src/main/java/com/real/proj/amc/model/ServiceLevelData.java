package com.real.proj.amc.model;

public class ServiceLevelData {

  String name;
  Rating userRating;

  public ServiceLevelData(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setUserRating(Rating rating) {
    this.userRating = rating;
  }

  public Rating getRating() {
    return this.userRating;
  }

}
