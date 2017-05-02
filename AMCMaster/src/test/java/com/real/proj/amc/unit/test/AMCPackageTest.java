package com.real.proj.amc.unit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.real.proj.amc.model.AMCPackage;
import com.real.proj.amc.model.AssetType;
import com.real.proj.amc.model.FixedPricingScheme;
import com.real.proj.amc.model.FixedPricingScheme.FixedPrice;
import com.real.proj.amc.model.PackagePriceInfo;
import com.real.proj.amc.model.PriceData;
import com.real.proj.amc.model.PricingStrategy;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.RatingBasedPricingScheme;
import com.real.proj.amc.model.RatingBasedPricingScheme.RatingBasedPrice;
import com.real.proj.amc.model.ServiceData;
import com.real.proj.amc.model.SubscriptionService;
import com.real.proj.amc.model.UserInput;

public class AMCPackageTest {

  static {
    System.setProperty("ENVIRONMENT", "TEST");
  }

  @Test
  public void testCreateAMCPackage() {
    AMCPackage pkg = createRawPackage();
    assertEquals(2, pkg.getServices().size());
    SubscriptionService anotherService = this.createRawSubscriptionService("P", "Phani", "Cleaning Job");
    anotherService = decorateService(anotherService);
    pkg.addService(anotherService);
    assertEquals(3, pkg.getServices().size());
  }

  private SubscriptionService decorateService(SubscriptionService service) {
    PricingStrategy price = this.getFixedPrice();
    service.setPricingStrategy(price);
    // Set<ServiceLevelData> sla = this.createServiceLevelData();
    // service.setServiceLevelData(sla);
    return service;
  }

  private AMCPackage createRawPackage() {
    String name = "Annual Package";
    String description = "Offers services for 1 year";
    Long tenure = Long.valueOf(12);
    Double disc = Double.valueOf(10);
    List<SubscriptionService> services = new ArrayList<SubscriptionService>();
    SubscriptionService service1 = createRawSubscriptionService("A", "AB", "ABC");
    SubscriptionService service2 = createRawSubscriptionService("M", "MN", "MNO");

    services.add(service1);
    services.add(service2);
    AMCPackage pkg = new AMCPackage(name, description, tenure, disc, services);
    return pkg;
  }

  @Test
  public void testGetPackagePriceForIncompleteService() {
    AMCPackage pkg = this.createRawPackage();
    UserInput input = new UserInput();
    SubscriptionService service = this.createRawSubscriptionService("A", "AB", "ABC");
    List<SubscriptionService> services = new ArrayList<SubscriptionService>();
    services.add(service);
    PackagePriceInfo priceInfo;
    try {
      priceInfo = pkg.getActualPrice(services, Rating.ONE);
      fail("System should not return price for the given service is not set any price details.");
    } catch (IllegalStateException ex) {
    }
    PricingStrategy price = this.getFixedPrice();
    services.remove(services);
    service.setPricingStrategy(price);
    services.add(service);
    try {
      priceInfo = pkg.getActualPrice(services, Rating.ONE);
      // fail("System should not return price for the given service is not set
      // any scheme details.");
    } catch (IllegalArgumentException ex) {
    }

  }

  @Test
  public void testGetPackagePriceForCompleteService() {
    AMCPackage pkg = this.createRawPackage();
    UserInput input = new UserInput();
    SubscriptionService service1 = this.createRawSubscriptionService("A", "AB", "ABC");
    SubscriptionService service2 = this.createRawSubscriptionService("M", "MN", "MNO");

    PricingStrategy price = this.getFixedPrice();
    service1.setPricingStrategy(price);
    service2.setPricingStrategy(price);
    // Set<ServiceLevelData> sla = this.createServiceLevelData();
    // service.setServiceLevelData(sla);
    List<SubscriptionService> services = new ArrayList<SubscriptionService>();
    services.add(service1);
    services.add(service2);
    // Map<PackageScheme, PackagePriceInfo> priceByScheme =
    PackagePriceInfo priceInfo = pkg.getActualPrice(services, null);
    // assertEquals(PackageScheme.GOLD, info.getScheme());
    assertTrue((200.0 - priceInfo.getActualPrice()) < 0.1);
    assertTrue((20.0 - priceInfo.getDiscount()) < 0.1);
  }

  /*
   * private Set<ServiceLevelData> createServiceLevelData() {
   * Set<ServiceLevelData> sla = new HashSet<ServiceLevelData>(); int count =
   * 10; PackageScheme[] schemes = PackageScheme.values(); for (PackageScheme
   * scheme : schemes) { sla.add(new ServiceLevelData(scheme, count++)); }
   * return sla; }
   */
  @Test
  public void createSubscriptionService() {
    SubscriptionService service = createRawSubscriptionService("A", "AB", "ABC");
    assertEquals("AB", service.getName());
    assertEquals(false, service.isActive()); // service must NOT be active when
                                             // defined
  }

  private SubscriptionService createRawSubscriptionService(String category, String name, String desc) {
    // String category = "Test";
    // String name = "HomeMaintenance";
    // String desc = "This service takes care of your house hold service
    // requirements";
    List<AssetType> assets = new ArrayList<AssetType>();
    assets.add(AssetType.APARTMENT);
    assets.add(AssetType.FLAT);
    assets.add(AssetType.HOUSE);
    List<String> amenities = new ArrayList<String>();
    amenities.add("ELECTRICAL");
    SubscriptionService service = new SubscriptionService(category, name, desc, assets, amenities);
    return service;
  }

  @Test
  public void testForPrice() {
    SubscriptionService service = this.createRawSubscriptionService("A", "AB", "ABC");
    assertNull(service.getPricingStrategy());
    try {
      service.getServiceData(null);
      fail("System must reject this call at this time");
    } catch (IllegalStateException ex) {
    }
    try {
      service.setPricingStrategy(null);
      fail("System must reject null price");
    } catch (IllegalArgumentException ex) {
    }
    PricingStrategy pricingStrategy = getFixedPrice();
    service.setPricingStrategy(pricingStrategy);
    assertEquals(pricingStrategy.getName(), service.getPricingStrategy().getName());
    assertEquals(100.0, service.getServiceData(null).getPrice(), 0.1);
    PricingStrategy ratingBasedStrategy = getRatingBasedPrice();
    service.setPricingStrategy(ratingBasedStrategy);
  }

  private PricingStrategy getRatingBasedPrice() {
    PricingStrategy pricingStrategy = new RatingBasedPricingScheme();
    PriceData ratingBasedPrice;
    Map<Rating, ServiceData> price = new HashMap<Rating, ServiceData>();
    price.put(Rating.ONE, new ServiceData(100.0, 3));
    price.put(Rating.TWO, new ServiceData(120.0, 3));
    price.put(Rating.THREE, new ServiceData(130.0, 3));
    price.put(Rating.FOUR, new ServiceData(140.0, 3));
    price.put(Rating.FIVE, new ServiceData(150.0, 3));
    ratingBasedPrice = new RatingBasedPrice(price);
    pricingStrategy.updatePrice(ratingBasedPrice, new Date());
    return pricingStrategy;
  }

  private PricingStrategy getFixedPrice() {
    PriceData fixedPriceData = new FixedPrice(new ServiceData(100.0, 1));
    PricingStrategy pricingStrategy = new FixedPricingScheme();
    pricingStrategy.updatePrice(fixedPriceData, new Date());
    return pricingStrategy;
  }

}
