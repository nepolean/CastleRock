package com.real.proj.amc.unit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.subsede.amc.catalog.model.BaseService;
import com.subsede.amc.catalog.model.OneTimeData;
import com.subsede.amc.catalog.model.OneTimeMetadata;
import com.subsede.amc.catalog.model.Rating;
import com.subsede.amc.catalog.model.ServiceMetadata;
import com.subsede.amc.catalog.model.SubscriptionData;
import com.subsede.amc.catalog.model.SubscriptionMetadata;
import com.subsede.amc.catalog.model.UserInput;
import com.subsede.amc.catalog.model.asset.RatingBasedOneTimeData;
import com.subsede.amc.catalog.model.asset.RatingBasedSubscriptionData;

public class ServiceTest {

  private static Logger logger = LoggerFactory.getLogger(ServiceTest.class);
  static {
    System.setProperty("ENVIRONMENT", "TEST");
  }

  @Test
  public void testCreateService() {
    BaseService svc = ServiceTestHelper.createBasicService();
    assertNotNull(svc);
    assertEquals("PLUMBING SERVICE", svc.getName());
  }

  @Test
  public void testSetValidSubscriptionData() {
    BaseService svc = ServiceTestHelper.createBasicService();
    SubscriptionMetadata metadata = ServiceTestHelper.createRatingBasedSubscriptionData();
    svc.setSubscriptionData(metadata);
    assertEquals(true, svc.canSubscribe());
    assertEquals(false, svc.canRequestOneTime());
    SubscriptionData data = svc.fetchSubscriptionData();
    assert (180 - data.getSubscriptionPrice() < 0.1);
    assertEquals(10, data.getVisitCount());
  }

  @Test
  public void testSetValidOneTimeData() {

    BaseService svc = ServiceTestHelper.createBasicService();
    OneTimeMetadata metadata = ServiceTestHelper.createOneTimeMetadata();
    svc.setOneTimeData(metadata);
    assertEquals(true, svc.canRequestOneTime());
    assertEquals(false, svc.canSubscribe());
    OneTimeData data = svc.fetchOneTimeData();
    assert (data.getPrice() - 18.0 < 0.1);
  }

  @Test
  public void testSetInvalidSubscriptionData() {
    BaseService svc = ServiceTestHelper.createBasicService();
    ServiceMetadata metadata = ServiceTestHelper.createOneTimeMetadata();
    try {
      svc.setSubscriptionData(metadata);
      fail("The service must not accept in-compatible service metadata");
    } catch (IllegalArgumentException ex) {
    }
  }

  @Test
  public void testSetInvalidOneTimeData() {
    BaseService svc = ServiceTestHelper.createBasicService();
    ServiceMetadata metadata = ServiceTestHelper.createRatingBasedSubscriptionData();
    try {
      svc.setOneTimeData(metadata);
      fail("The service must not accept in-compatible service metadata");
    } catch (IllegalArgumentException ex) {

    }

  }

  @Test
  public void testUpdateSubscriptionData() {
    BaseService svc = ServiceTestHelper.createBasicService();
    ServiceMetadata metadata = ServiceTestHelper.createRatingBasedSubscriptionData();
    svc.setSubscriptionData(metadata);
    RatingBasedSubscriptionData ratingBasedMD = (RatingBasedSubscriptionData) metadata;
    ratingBasedMD.updateSubscriptionMetadata(Rating.ONE, new SubscriptionData(200.0, 20));
    svc.updateSubscriptionData(ratingBasedMD, new Date());
    // get value for rating one
    UserInput<String, Object> input = new UserInput<String, Object>();
    input.add("RATING", Rating.ONE);
    double price = svc.fetchSubscriptionData(input).getSubscriptionPrice();
    int count = svc.fetchSubscriptionData(input).getVisitCount();
    if (logger.isDebugEnabled())
      logger.debug("Updated price = {} and count = {}", price, count);
    assertEquals(20, count);
    assert (200.0 - price < 0.1);
  }

  @Test
  public void testUpdateOneTimeData() {
    BaseService svc = ServiceTestHelper.createBasicService();
    ServiceMetadata metadata = ServiceTestHelper.createOneTimeMetadata();
    svc.setOneTimeData(metadata);
    RatingBasedOneTimeData ratingBasedMD = (RatingBasedOneTimeData) metadata;
    ratingBasedMD.updateSubscriptionMetadata(Rating.ONE, new OneTimeData(200.0));
    svc.updateOneTimeData(ratingBasedMD, new Date());
    // get value for rating one
    UserInput<String, Object> input = new UserInput<String, Object>();
    input.add("RATING", Rating.ONE);
    double price = svc.fetchOneTimeData(input).getPrice();
    if (logger.isDebugEnabled())
      logger.debug("Updated price = {}", price);
    assert (200.0 - price < 0.1);
  }

  @Test
  public void testUnsetSubscriptionData() {
    BaseService svc = ServiceTestHelper.createBasicService();
    ServiceMetadata metadata = ServiceTestHelper.createRatingBasedSubscriptionData();
    svc.setSubscriptionData(metadata);
    assertEquals(true, svc.canSubscribe());
    svc.unsetSubscriptionServiceData();
    assertEquals(false, svc.canSubscribe());
    try {
      svc.fetchSubscriptionData();
      fail("The service must not return any value");
    } catch (Exception ex) {

    }
  }

  @Test
  public void testUnsetOneTimeData() {
    BaseService svc = ServiceTestHelper.createBasicService();
    ServiceMetadata metadata = ServiceTestHelper.createOneTimeMetadata();
    svc.setOneTimeData(metadata);
    assertEquals(true, svc.canRequestOneTime());
    svc.unsetOneTimeServiceData();
    assertEquals(false, svc.canRequestOneTime());
    try {
      svc.fetchOneTimeData();
      fail("The service must not return any value");
    } catch (IllegalStateException ex) {
    }
  }

}
