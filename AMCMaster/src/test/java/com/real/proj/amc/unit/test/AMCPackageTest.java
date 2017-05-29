package com.real.proj.amc.unit.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.real.proj.amc.model.AMCPackage;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.SubscriptionData;
import com.real.proj.amc.model.TenureBasedDiscount;
import com.real.proj.amc.model.UserInput;

public class AMCPackageTest {

  private Logger logger = LoggerFactory.getLogger(AMCPackage.class);
  static {
    System.setProperty("ENVIRONMENT", "TEST");
  }

  @Test
  public void testCreateAMCPackage() {
    AMCPackage pkg = ServiceTestHelper.createRawPackage();
    assertEquals(2, pkg.getServices().size());
  }

  @Test
  public void testPrice() {
    AMCPackage pkg = ServiceTestHelper.createFullPackage();
    UserInput<String, Object> input = new UserInput<String, Object>();
    input.add("RATING", Rating.ONE);
    SubscriptionData price = pkg.getActualPrice(input);
    if (logger.isDebugEnabled())
      logger.debug("Price returned {}", price);
    assert (Double.valueOf(price.getSubscriptionPrice()).compareTo(200.0) == 0);
    // assert (Double.valueOf(price.getDiscount()).compareTo(200.0) == 0);
  }

  @Test

  public void testTenureDiscount() {
    AMCPackage pkg = ServiceTestHelper.createFullPackage();
    TenureBasedDiscount disc = new TenureBasedDiscount();
    disc.addDiscount(1, 10.0);
    disc.addDiscount(2, 12.0);
    pkg.setTenureBasedDisc(disc);
    assertEquals(2, pkg.getTenureBasedDisc().getAllTenures().size());
    assert (Double.valueOf(pkg.getTenureBasedDisc().getDiscountFor(1)).compareTo(10.0) == 0.0);
  }

}
