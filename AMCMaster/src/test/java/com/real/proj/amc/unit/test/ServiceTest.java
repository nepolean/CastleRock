package com.real.proj.amc.unit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.real.proj.amc.model.AssetBasedService;
import com.real.proj.amc.model.AssetType;
import com.real.proj.amc.model.BaseService;
import com.real.proj.amc.model.OneTimeData;
import com.real.proj.amc.model.OneTimeMetadata;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.RatingBasedOneTimeMetadata;
import com.real.proj.amc.model.RatingBasedSubscriptionMetadata;
import com.real.proj.amc.model.ServiceMetadata;
import com.real.proj.amc.model.SubscriptionData;
import com.real.proj.amc.model.SubscriptionMetadata;

public class ServiceTest {

  static {
    System.setProperty("ENVIRONMENT", "TEST");
  }

  @Test
  public void testCreateService() {
    BaseService svc = createBasicService();
    assertNotNull(svc);
    assertEquals("PLUMBING SERVICE", svc.getName());
  }

  @Test
  public void testSetValidSubscriptionData() {
    BaseService svc = createBasicService();
    SubscriptionMetadata metadata = createRatingBasedSubscriptionData();
    svc.setSubscriptionServiceData(metadata);
    assertEquals(true, svc.canSubscribe());
    assertEquals(false, svc.canRequestOneTime());
    SubscriptionData data = svc.getSubscriptionData();
    assert (180 - data.getSubscriptionPrice() < 0.1);
    assertEquals(10, data.getVisitCount());
  }

  @Test
  public void testSetValidOneTimeData() {

    BaseService svc = this.createBasicService();
    OneTimeMetadata metadata = this.createOneTimeMetadata();
    svc.setOneTimeServiceData(metadata);
    assertEquals(true, svc.canRequestOneTime());
    assertEquals(false, svc.canSubscribe());
    OneTimeData data = svc.getOneTimeData();
    assert (data.getPrice() - 18.0 < 0.1);
  }

  @Test
  public void testSetInvalidSubscriptionData() {
    BaseService svc = this.createBasicService();
    ServiceMetadata metadata = this.createOneTimeMetadata();
    try {
      svc.setSubscriptionServiceData(metadata);
      fail("The service must not accept in-compatible service metadata");
    } catch (IllegalArgumentException ex) {
    }
  }

  @Test
  public void testSetInvalidOneTimeData() {
    BaseService svc = this.createBasicService();
    ServiceMetadata metadata = this.createRatingBasedSubscriptionData();
    try {
      svc.setOneTimeServiceData(metadata);
      fail("The service must not accept in-compatible service metadata");
    } catch (IllegalArgumentException ex) {

    }

  }

  private RatingBasedSubscriptionMetadata createRatingBasedSubscriptionData() {
    Map<Rating, SubscriptionData> data = createRatingBasedSubscriptionMap();
    return new RatingBasedSubscriptionMetadata(data);
  }

  private Map<Rating, SubscriptionData> createRatingBasedSubscriptionMap() {
    Map<Rating, SubscriptionData> map = new HashMap<Rating, SubscriptionData>();
    map.put(Rating.ONE, new SubscriptionData(100.0, 3));
    map.put(Rating.TWO, new SubscriptionData(120.0, 4));
    map.put(Rating.THREE, new SubscriptionData(140.0, 6));
    map.put(Rating.FOUR, new SubscriptionData(160.0, 8));
    map.put(Rating.FIVE, new SubscriptionData(180.0, 10));
    return map;
  }

  private OneTimeMetadata createOneTimeMetadata() {
    Map<Rating, OneTimeData> map = this.createOneTimeDataMap();
    OneTimeMetadata metadata = new RatingBasedOneTimeMetadata(map);
    return metadata;
  }

  private Map<Rating, OneTimeData> createOneTimeDataMap() {
    Map<Rating, OneTimeData> map = new HashMap<Rating, OneTimeData>();
    map.put(Rating.ONE, new OneTimeData(10.0));
    map.put(Rating.TWO, new OneTimeData(12.0));
    map.put(Rating.THREE, new OneTimeData(14.0));
    map.put(Rating.FOUR, new OneTimeData(16.0));
    map.put(Rating.FIVE, new OneTimeData(18.0));
    return map;
  }

  private AssetBasedService createBasicService() {
    String name = "PLUMBING SERVICE";
    String desc = "The plumbing service takes care of plumbing needs.";
    List<AssetType> applicableTo = createSampleApplicableList();
    List<String> amenities = createSampleAmenities();
    return new AssetBasedService(name, desc, applicableTo, amenities);
  }

  private List<AssetType> createSampleApplicableList() {
    List<AssetType> assets = new ArrayList<AssetType>();
    assets.add(AssetType.APARTMENT);
    assets.add(AssetType.FLAT);
    assets.add(AssetType.HOUSE);
    return assets;
  }

  private List<String> createSampleAmenities() {
    List<String> amenities = new ArrayList<String>();
    amenities.add("Sewage");
    return amenities;
  }

}
