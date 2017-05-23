package com.real.proj.amc.model.deleted;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.real.proj.amc.model.PriceData;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.TimeLine;
import com.real.proj.amc.model.UserInput;

public class RatingBasedPricingScheme extends PricingStrategy implements Serializable {

  private static final long serialVersionUID = 1L;
  public final static String NAME = "RATING_BASED";
  public static final String RATING = "RATING";
  private static final Logger logger = LoggerFactory.getLogger(RatingBasedPricingScheme.class);

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
  public ServiceData getServiceLevelData(UserInput<String, Object> input) {
    logger.info("getPrice {}", input);
    if (input == null)
      throw new IllegalArgumentException("No user input for this request");
    Rating rating = (Rating) input.get(RATING);
    if (logger.isDebugEnabled())
      logger.debug("user rating: {}", rating);
    RatingBasedPrice currPrice = this.priceHistory.getCurrentValue();
    ServiceData serviceData = currPrice.getServiceLevelData(rating);
    if (serviceData == null) {
      if (logger.isDebugEnabled())
        logger.debug("ServiceLevelData is not available for the given rating");
      throw new IllegalStateException("Price details not defined for the given rating.");
    }
    return serviceData;
  }

  @Override
  public ServiceData getServiceLevelData(UserInput<String, Object> input, Date on) {
    if (input == null)
      throw new IllegalArgumentException("No user input for this request");
    Rating rating = (Rating) input.get(RATING);
    if (rating == null)
      throw new IllegalArgumentException("Invalid rating");
    return this.priceHistory.getValueForDate(on).getServiceLevelData(rating);
  }

  @Override
  public ServiceData getDefaultServiceLevelData() {
    UserInput<String, Object> input = new UserInput<String, Object>();
    input.add(RATING, Rating.FIVE); // assume 5 as the default rating
    return this.getServiceLevelData(input);
  }

  public static class RatingBasedPrice extends PriceData implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<Rating, ServiceData> price;

    public RatingBasedPrice() {
      this.price = new HashMap<Rating, ServiceData>();
    }

    public RatingBasedPrice(Map<Rating, ServiceData> price) {
      this.price = price;
    }

    public Map<Rating, ServiceData> getPrice() {
      return price;
    }

    public void setPrice(Map<Rating, ServiceData> price) {
      this.price = price;
    }

    public void addPriceFor(Rating rating, ServiceData value) {
      this.price.put(rating, value);
    }

    public ServiceData getServiceLevelData(Rating rating) {
      return this.price.get(rating);
    }

  }
}
