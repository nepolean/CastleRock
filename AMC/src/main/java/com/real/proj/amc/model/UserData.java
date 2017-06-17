package com.real.proj.amc.model;

public class UserData {

  private double measuredArea;
  private int tenure;
  private UserInput<String, Object> input;

  public UserData() {

  }

  public UserData(int rating, int tenure, double measuredArea) {
    this.measuredArea = measuredArea;
    this.tenure = tenure;
    this.setRating(rating);
  }

  public double getMeasuredArea() {
    return measuredArea;
  }

  public void setMeasuredArea(double measuredArea) {
    this.measuredArea = measuredArea;
  }

  public int getTenure() {
    return tenure;
  }

  public void setTenure(int tenure) {
    this.tenure = tenure;
  }

  public Rating getRating() {
    return (Rating) input.get(Rating.getKey());
  }

  public void setRating(int rating) {
    UserInput<String, Object> userInput = new UserInput<String, Object>();
    userInput.add(Rating.getKey(), Rating.values()[rating - 1]);
    this.input = userInput;
  }

  public UserInput<String, Object> getInput() {
    return this.input;
  }

}