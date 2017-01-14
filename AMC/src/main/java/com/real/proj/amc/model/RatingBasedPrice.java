package com.real.proj.amc.model;

public class RatingBasedPrice extends Price {

  private Rating rating;
  private double price;

  public RatingBasedPrice(Rating rating, double price) {
    this.rating = rating;
    this.price = price;
  }

  public double getPrice() {
    return this.priceList.get(rating);
  }
}
