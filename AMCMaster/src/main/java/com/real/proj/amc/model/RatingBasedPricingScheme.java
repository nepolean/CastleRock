package com.real.proj.amc.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RatingBasedPricingScheme extends PricingStrategy {

  public static final String RATING = "RATING";
  History<RatingBasedPrice> priceHistory = new History<RatingBasedPrice>();

  public RatingBasedPricingScheme() {

  }

  public RatingBasedPricingScheme(RatingBasedPrice price) {
    this(price, new Date());
  }

  public RatingBasedPricingScheme(RatingBasedPrice price, Date validFrom) {
    priceHistory.addToHistory(price, validFrom);
  }

  public void setPriceHistory(History<RatingBasedPrice> priceHistory) {
    this.priceHistory = priceHistory;
  }

  @Override
  public void updatePrice(PriceData price, Date validFrom) {
    if (price == null)
      throw new IllegalArgumentException("Null price data is passed");
    if (!(price instanceof RatingBasedPrice))
      throw new IllegalArgumentException("Invalid TYPE for price data is passed");
    this.priceHistory.addToHistory((RatingBasedPrice) price, validFrom);
  }

  @Override
  public double getPrice(UserInput<String, Object> input) {
    if (input == null)
      throw new IllegalArgumentException("No user input for this request");
    Rating rating = (Rating) input.get(RATING);
    return this.priceHistory.getCurrentValue().getPriceFor(rating);
  }

  @Override
  public double getPrice(UserInput<String, Object> input, Date on) {
    if (input == null)
      throw new IllegalArgumentException("No user input for this request");
    Rating rating = (Rating) input.get(RATING);
    if (rating == null)
      throw new IllegalArgumentException("Invalid rating");
    return this.priceHistory.getValueForDate(on).getPriceFor(rating);
  }

  public static class RatingBasedPrice extends PriceData {

    private Map<Rating, Double> price;

    public RatingBasedPrice() {
      price = new HashMap<Rating, Double>();
    }

    public void addPriceFor(Rating rating, double value) {
      this.price.put(rating, value);
    }

    public double getPriceFor(Rating rating) {
      return this.price.get(rating);
    }

  }
}
