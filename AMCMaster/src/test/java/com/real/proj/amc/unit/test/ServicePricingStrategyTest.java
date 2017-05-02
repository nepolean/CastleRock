package com.real.proj.amc.unit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.real.proj.amc.model.FixedPricingScheme;
import com.real.proj.amc.model.FixedPricingScheme.FixedPrice;
import com.real.proj.amc.model.PriceData;
import com.real.proj.amc.model.PricingStrategy;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.RatingBasedPricingScheme;
import com.real.proj.amc.model.RatingBasedPricingScheme.RatingBasedPrice;
import com.real.proj.amc.model.ServiceData;
import com.real.proj.amc.model.UserInput;

public class ServicePricingStrategyTest {
  static {
    System.setProperty("ENVIRONMENT", "TEST");
  }

  @Test
  public void testCreateFixedPrice() {
    PricingStrategy pricingStrategy = new FixedPricingScheme();
    PriceData price = new FixedPrice(new ServiceData(100.0, 1));
    pricingStrategy.updatePrice(price, new Date());
    double priceVal = pricingStrategy.getServiceLevelData(null).getPrice();
    assertEquals(100.0, priceVal, 0.1);
  }

  @Test
  public void testCreateRatingBasedPrice() {
    PricingStrategy pricingStrategy = new RatingBasedPricingScheme();
    Map<Rating, ServiceData> priceData = new HashMap<Rating, ServiceData>();
    double start = 100.0;
    Rating[] ratings = Rating.values();
    for (Rating rating : ratings) {
      ServiceData sld = new ServiceData(start += 10.0, 3);
      priceData.put(rating, sld);
    }
    PriceData price = new RatingBasedPrice(priceData);
    pricingStrategy.updatePrice(price, new Date());
    UserInput<String, Object> input = new UserInput<String, Object>();
    input.add(RatingBasedPricingScheme.RATING, Rating.TWO);
    double actualValue = pricingStrategy.getServiceLevelData(input).getPrice();
    assertEquals(120.0, actualValue, 0.1);
    double futureValue = pricingStrategy.getServiceLevelData(input, new Date(System.currentTimeMillis() + 100000))
        .getPrice();
    assertEquals(120.0, futureValue, 0.1);
    try {
      pricingStrategy.getServiceLevelData(null).getPrice();
      fail("Exception is expected here for null input");
    } catch (IllegalArgumentException ex) {
    }
  }

  @Test
  public void testCreatePriceStrategyWithIncompleteData() {
    PricingStrategy ratingStrategy = new RatingBasedPricingScheme();
    PriceData ratingBasedPrice = new RatingBasedPrice();
    try {
      ratingStrategy.updatePrice(ratingBasedPrice, new Date());
      fail("Incomplete data must be rejected");
    } catch (IllegalArgumentException ex) {
    }
  }

  @Test
  public void testMustRejectNonConfirngTypes() {
    PricingStrategy ratingStrategy = new RatingBasedPricingScheme();
    PriceData price = new FixedPrice(new ServiceData(100.0, 1));
    try {
      ratingStrategy.updatePrice(price, new Date());
      fail("In compatible data must be rejected");
    } catch (IllegalArgumentException ex) {
    }
  }

}
