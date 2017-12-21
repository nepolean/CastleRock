package com.real.proj.amc.unit.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.subsede.amc.catalog.model.AMCPackage;
import com.subsede.amc.catalog.model.BaseService;
import com.subsede.amc.catalog.model.Category;
import com.subsede.amc.catalog.model.OneTimeData;
import com.subsede.amc.catalog.model.OneTimeMetadata;
import com.subsede.amc.catalog.model.Rating;
import com.subsede.amc.catalog.model.Service;
import com.subsede.amc.catalog.model.SubscriptionData;
import com.subsede.amc.catalog.model.TenureBasedDiscount;
import com.subsede.amc.catalog.model.asset.AssetBasedService;
import com.subsede.amc.catalog.model.asset.AssetType;
import com.subsede.amc.catalog.model.asset.RatingBasedOneTimeData;
import com.subsede.amc.catalog.model.asset.RatingBasedSubscriptionData;

public class ServiceTestHelper {

  static RatingBasedSubscriptionData createRatingBasedSubscriptionData() {
    Map<Rating, SubscriptionData> data = createRatingBasedSubscriptionMap();
    return new RatingBasedSubscriptionData(data);
  }

  static Map<Rating, SubscriptionData> createRatingBasedSubscriptionMap() {
    Map<Rating, SubscriptionData> map = new HashMap<Rating, SubscriptionData>();
    map.put(Rating.ONE, new SubscriptionData(100.0, 3));
    map.put(Rating.TWO, new SubscriptionData(120.0, 4));
    map.put(Rating.THREE, new SubscriptionData(140.0, 6));
    map.put(Rating.FOUR, new SubscriptionData(160.0, 8));
    map.put(Rating.FIVE, new SubscriptionData(180.0, 10));
    return map;
  }

  static OneTimeMetadata createOneTimeMetadata() {
    Map<Rating, OneTimeData> map = createOneTimeDataMap();
    OneTimeMetadata metadata = new RatingBasedOneTimeData(map);
    return metadata;
  }

  static Map<Rating, OneTimeData> createOneTimeDataMap() {
    Map<Rating, OneTimeData> map = new HashMap<Rating, OneTimeData>();
    map.put(Rating.ONE, new OneTimeData(10.0));
    map.put(Rating.TWO, new OneTimeData(12.0));
    map.put(Rating.THREE, new OneTimeData(14.0));
    map.put(Rating.FOUR, new OneTimeData(16.0));
    map.put(Rating.FIVE, new OneTimeData(18.0));
    return map;
  }

  static AssetBasedService createBasicService() {
    String name = "PLUMBING SERVICE";
    String desc = "The plumbing service takes care of plumbing needs.";
    List<AssetType> applicableTo = createSampleApplicableList();
    List<String> amenities = createSampleAmenities();
    return new AssetBasedService(name, desc, applicableTo, amenities, Category.ASSET.getServiceTypes()[0]);
  }

  static List<AssetType> createSampleApplicableList() {
    List<AssetType> assets = new ArrayList<AssetType>();
    assets.add(AssetType.APARTMENT);
    assets.add(AssetType.FLAT);
    assets.add(AssetType.HOUSE);
    return assets;
  }

  static List<String> createSampleAmenities() {
    List<String> amenities = new ArrayList<String>();
    amenities.add("Sewage");
    return amenities;
  }

  static AMCPackage createRawPackage() {
    Category cat = Category.ASSET;
    String name = "Annual Maintenace Package";
    String description = "Offers services for 1 year";
    AMCPackage pkg = new AMCPackage(cat, name, description);
    List<Service> services = createFewServices();
    pkg.addServices(services);
    return pkg;
  }

  public static AMCPackage createFullPackage() {
    AMCPackage pkg = ServiceTestHelper.createRawPackage();
    pkg.addServices(ServiceTestHelper.createFewServices());
    // ServiceRepository repo = Mockito.mock(ServiceRepository.class);
    // Mockito.when(repo.findAll(pkg.getServiceInfo())).thenReturn(ServiceTestHelper.createFewServices());
    // pkg.setServiceRepository(repo);
    return pkg;
  }

  public static AMCPackage createPackageWithDiscount() {
    AMCPackage pkg = createFullPackage();
    TenureBasedDiscount tenureBasedDisc = new TenureBasedDiscount();
    tenureBasedDisc.addDiscount(1, 10.0);
    tenureBasedDisc.addDiscount(2, 12.0);
    tenureBasedDisc.addDiscount(3, 15.0);
    tenureBasedDisc.addDiscount(4, 20.0);
    pkg.setTenureBasedDisc(tenureBasedDisc);
    return pkg;
  }

  public static List<Service> createFewServices() {
    List<Service> services = new ArrayList<Service>();
    BaseService svc = ServiceTestHelper.createBasicService();
    svc.setSubscriptionData(ServiceTestHelper.createRatingBasedSubscriptionData());
    svc.setOneTimeData(ServiceTestHelper.createOneTimeMetadata());
    services.add(svc);
    svc = ServiceTestHelper.createBasicService();
    svc.setSubscriptionData(ServiceTestHelper.createRatingBasedSubscriptionData());
    svc.setOneTimeData(ServiceTestHelper.createOneTimeMetadata());
    services.add(svc);
    return services;
  }

}
