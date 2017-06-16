package com.real.proj.amc.model.deleted;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.real.proj.amc.model.TimeLine;
import com.real.proj.amc.model.UserInput;

public class FixedPricingScheme extends PricingStrategy implements Serializable {

  public final static String NAME = "Fixed";
  private final static Logger logger = LoggerFactory.getLogger(FixedPricingScheme.class);

  TimeLine<FixedPrice> price = new TimeLine<FixedPrice>();

  public FixedPricingScheme() {

  }

  public FixedPricingScheme(FixedPrice price) {
    this(price, new Date());
  }

  public FixedPricingScheme(FixedPrice price, Date validFrom) {
    this.price.addToHistory((FixedPrice) price, validFrom);
  }

  @Override
  public void setName(String name) {
    // this.name = "Fixed Price";
  }

  @Override
  public String getName() {
    return NAME;
  }

  public void setPrice(TimeLine<FixedPrice> price) {
    this.price = price;
  }

  public TimeLine<FixedPrice> getPrice() {
    return this.price;
  }

  @Override
  public ServiceData getServiceLevelData(UserInput<String, Object> input) {
    FixedPrice data = this.price.getCurrentValue();
    if (data == null) {
      if (logger.isErrorEnabled())
        logger.error("Pricing not defined for this service");
      return null;
    }

    return data.getPrice();
  }

  @Override
  public ServiceData getServiceLevelData(UserInput<String, Object> input, Date on) {
    FixedPrice data = this.price.getValueForDate(on);
    if (data == null) {
      if (logger.isDebugEnabled())
        logger.debug("pricing not available for the given date {}", on);
      return null;
    }
    return data.getPrice();
  }

  @Override
  public void updatePrice(PriceData price, Date validFrom) {
    if (price == null)
      throw new IllegalArgumentException("Null price data is passed");
    if (!(price instanceof FixedPrice))
      throw new IllegalArgumentException("Invalid price data TYPE is passed");
    this.price.addToHistory((FixedPrice) price, validFrom);
  }

  @Override
  public ServiceData getDefaultServiceLevelData() {
    return this.getServiceLevelData(null);
  }

  @Override
  public String toString() {
    return "FixedPricingScheme [price=" + price + "]";
  }

  public static class FixedPrice extends PriceData implements Serializable {

    private static final long serialVersionUID = 1L;
    ServiceData price;

    public FixedPrice(ServiceData price) {
      this.price = price;
    }

    public FixedPrice(double d) {
      // TODO Auto-generated constructor stub
    }

    public ServiceData getPrice() {
      return price;
    }

    public void setPrice(ServiceData price) {
      this.price = price;
    }

    @Override
    public String toString() {
      return "FixedPrice [price=" + price + "]";
    }

  }

}
