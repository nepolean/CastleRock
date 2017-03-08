package com.real.proj.amc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RatingBasedPricingScheme extends PricingStrategy implements Serializable {

  public final static String NAME = "RATING_BASED";
  public static final String RATING = "RATING";
  TimeLine<RatingBasedPrice> priceHistory = new TimeLine<RatingBasedPrice>();

  public RatingBasedPricingScheme() {

  }

  public RatingBasedPricingScheme(RatingBasedPrice price) {
    this(price, new Date());
  }

  public RatingBasedPricingScheme(RatingBasedPrice price, Date validFrom) {
    priceHistory.addToHistory(price, validFrom);
  }

  public void setName(String name) {

  }

  public String getName() {
    return NAME;
  }

  public void setPriceHistory(TimeLine<RatingBasedPrice> priceHistory) {
    this.priceHistory = priceHistory;
  }

  public TimeLine<RatingBasedPrice> getPriceHistory() {
    return this.priceHistory;
  }

  @Override
  public void updatePrice(PriceData price, Date validFrom) {
    if (price == null)
      throw new IllegalArgumentException("Null price data is passed");
    if (!(price instanceof RatingBasedPrice))
      throw new IllegalArgumentException("Invalid TYPE for price data is passed");
    RatingBasedPrice ratingBasedPrice = (RatingBasedPrice) price;
    if (ratingBasedPrice.getPrice().size() != Rating.values().length)
      throw new IllegalArgumentException("Price must be defined for all possible ratings");
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

  public static class RatingBasedPrice extends PriceData implements Serializable {

    private Map<Rating, Double> price;

    public RatingBasedPrice() {
      this.price = new HashMap<Rating, Double>();
    }

    public RatingBasedPrice(Map<Rating, Double> price) {
      this.price = price;
    }

    public Map<Rating, Double> getPrice() {
      return price;
    }

    public void setPrice(Map<Rating, Double> price) {
      this.price = price;
    }

    public void addPriceFor(Rating rating, double value) {
      this.price.put(rating, value);
    }

    public double getPriceFor(Rating rating) {
      return this.price.get(rating);
    }

  }
}
