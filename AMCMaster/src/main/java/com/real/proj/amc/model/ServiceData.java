package com.real.proj.amc.model;

public class ServiceData {

  private int visits;
  private double price;

  public ServiceData(int visits, double price) {
    this.visits = visits;
    this.price = price;
  }

  public int getVisits() {
    return visits;
  }

  public void setVisits(int visits) {
    this.visits = visits;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

}
