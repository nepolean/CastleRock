package com.real.proj.amc.unit.test;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

import com.real.proj.amc.model.PriceData;
import com.real.proj.amc.model.PricingStrategy;
import com.real.proj.amc.model.RatingBasedPricingScheme;
import com.real.proj.amc.model.RatingBasedPricingScheme.RatingBasedPrice;

public class ServicePricingStrategyTest {
  static {
    System.setProperty("ENVIRONMENT", "TEST");
  }

  @Test
  public void testUpdatePrice() {
  }

  @Test
  public void testCreatePriceStrategy() {
    PricingStrategy ratingStrategy = new RatingBasedPricingScheme();
    PriceData ratingBasedPrice = new RatingBasedPrice();

    try {
      ratingStrategy.updatePrice(ratingBasedPrice, new Date());
      fail("Incomplete data must be rejected");
    } catch (IllegalArgumentException ex) {
    }
  }

}
