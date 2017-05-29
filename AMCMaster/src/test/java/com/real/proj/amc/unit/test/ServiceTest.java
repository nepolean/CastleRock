package com.real.proj.amc.unit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.real.proj.amc.model.BaseService;
import com.real.proj.amc.model.OneTimeData;
import com.real.proj.amc.model.OneTimeMetadata;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.RatingBasedOneTimeMetadata;
import com.real.proj.amc.model.RatingBasedSubscriptionMetadata;
import com.real.proj.amc.model.ServiceMetadata;
import com.real.proj.amc.model.SubscriptionData;
import com.real.proj.amc.model.SubscriptionMetadata;
import com.real.proj.amc.model.UserInput;

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
    svc.setSubscriptionServiceData(metadata);
    assertEquals(true, svc.canSubscribe());
    assertEquals(false, svc.canRequestOneTime());
    SubscriptionData data = svc.getSubscriptionData();
    assert (180 - data.getSubscriptionPrice() < 0.1);
    assertEquals(10, data.getVisitCount());
  }

  @Test
  public void testSetValidOneTimeData() {

    BaseService svc = ServiceTestHelper.createBasicService();
    OneTimeMetadata metadata = ServiceTestHelper.createOneTimeMetadata();
    svc.setOneTimeServiceData(metadata);
    assertEquals(true, svc.canRequestOneTime());
    assertEquals(false, svc.canSubscribe());
    OneTimeData data = svc.getOneTimeData();
    assert (data.getPrice() - 18.0 < 0.1);
  }

  @Test
  public void testSetInvalidSubscriptionData() {
    BaseService svc = ServiceTestHelper.createBasicService();
    ServiceMetadata metadata = ServiceTestHelper.createOneTimeMetadata();
    try {
      svc.setSubscriptionServiceData(metadata);
      fail("The service must not accept in-compatible service metadata");
    } catch (IllegalArgumentException ex) {
    }
  }

  @Test
  public void testSetInvalidOneTimeData() {
    BaseService svc = ServiceTestHelper.createBasicService();
    ServiceMetadata metadata = ServiceTestHelper.createRatingBasedSubscriptionData();
    try {
      svc.setOneTimeServiceData(metadata);
      fail("The service must not accept in-compatible service metadata");
    } catch (IllegalArgumentException ex) {

    }

  }

  @Test
  public void testUpdateSubscriptionData() {
    BaseService svc = ServiceTestHelper.createBasicService();
    ServiceMetadata metadata = ServiceTestHelper.createRatingBasedSubscriptionData();
    svc.setSubscriptionServiceData(metadata);
    RatingBasedSubscriptionMetadata ratingBasedMD = (RatingBasedSubscriptionMetadata) metadata;
    ratingBasedMD.updateSubscriptionMetadata(Rating.ONE, new SubscriptionData(200.0, 20));
    svc.updateSubscriptionData(ratingBasedMD, new Date());
    // get value for rating one
    UserInput<String, Object> input = new UserInput<String, Object>();
    input.add("RATING", Rating.ONE);
    double price = svc.getSubscriptionData(input).getSubscriptionPrice();
    int count = svc.getSubscriptionData(input).getVisitCount();
    if (logger.isDebugEnabled())
      logger.debug("Updated price = {} and count = {}", price, count);
    assertEquals(20, count);
    assert (200.0 - price < 0.1);
  }

  @Test
  public void testUpdateOneTimeData() {
    BaseService svc = ServiceTestHelper.createBasicService();
    ServiceMetadata metadata = ServiceTestHelper.createOneTimeMetadata();
    svc.setOneTimeServiceData(metadata);
    RatingBasedOneTimeMetadata ratingBasedMD = (RatingBasedOneTimeMetadata) metadata;
    ratingBasedMD.updateSubscriptionMetadata(Rating.ONE, new OneTimeData(200.0));
    svc.updateOneTimeData(ratingBasedMD, new Date());
    // get value for rating one
    UserInput<String, Object> input = new UserInput<String, Object>();
    input.add("RATING", Rating.ONE);
    double price = svc.getOneTimeData(input).getPrice();
    if (logger.isDebugEnabled())
      logger.debug("Updated price = {}", price);
    assert (200.0 - price < 0.1);
  }

  @Test
  public void testUnsetSubscriptionData() {
    BaseService svc = ServiceTestHelper.createBasicService();
    ServiceMetadata metadata = ServiceTestHelper.createRatingBasedSubscriptionData();
    svc.setSubscriptionServiceData(metadata);
    assertEquals(true, svc.canSubscribe());
    svc.unsetSubscriptionServiceData();
    assertEquals(false, svc.canSubscribe());
    try {
      svc.getSubscriptionData();
      fail("The service must not return any value");
    } catch (Exception ex) {

    }
  }

  @Test
  public void testUnsetOneTimeData() {
    BaseService svc = ServiceTestHelper.createBasicService();
    ServiceMetadata metadata = ServiceTestHelper.createOneTimeMetadata();
    svc.setOneTimeServiceData(metadata);
    assertEquals(true, svc.canRequestOneTime());
    svc.unsetOneTimeServiceData();
    assertEquals(false, svc.canRequestOneTime());
    try {
      svc.getOneTimeData();
      fail("The service must not return any value");
    } catch (IllegalStateException ex) {
    }
  }

}
