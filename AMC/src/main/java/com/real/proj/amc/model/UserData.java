package com.real.proj.amc.model;

public class UserData {

  private double measuredArea;
  private int tenure;
  private Rating rating;
  // private UserInput<String, Object> input;

  public UserData() {

  }

  public UserData(int age, int tenure, double measuredArea) {
    this.measuredArea = measuredArea;
    this.tenure = tenure;
    Rating rating = convertYearToRating(age);
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
    return this.rating;
  }

  public void setRating(Rating rating) {
    this.rating = rating;
  }

  private Rating convertYearToRating(int age) {
    if (age <= 3)
      return Rating.ONE;
    if (age <= 5)
      return Rating.TWO;
    if (age <= 7)
      return Rating.THREE;
    if (age <= 10)
      return Rating.FOUR;
    return Rating.FIVE;
  }

  public UserInput<String, Object> getInput() {
    UserInput<String, Object> userInput = new UserInput<String, Object>();
    userInput.add(Rating.getKey(), this.rating);
    return userInput;
  }

  public int getTenureInMonths() {
    return tenure * 3; // Minimum a quarter of subscription
  }

}