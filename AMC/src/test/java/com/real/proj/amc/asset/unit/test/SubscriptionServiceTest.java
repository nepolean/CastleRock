package com.real.proj.amc.asset.unit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import com.real.proj.amc.model.AMCPackage;
import com.real.proj.amc.model.Asset;
import com.real.proj.amc.model.AssetBasedService;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.ServiceData;
import com.real.proj.amc.model.States;
import com.real.proj.amc.model.Subscription;
import com.real.proj.amc.model.Tax;
import com.real.proj.amc.service.AssetService;
import com.real.proj.amc.service.GenericFCRUDService;
import com.real.proj.amc.service.SubscriptionRepository;
import com.real.proj.amc.service.SubscriptionService;
import com.real.proj.unit.test.BaseTest;
import com.real.proj.user.model.User;

@EnableAutoConfiguration
public class SubscriptionServiceTest extends BaseTest {

  @Autowired
  private SubscriptionRepository subscRepo;
  @Autowired
  private GenericFCRUDService crudService;
  @Autowired
  private SubscriptionService subsService;
  @Autowired
  private AssetService assetService;

  AssetServiceTest assetTest = new AssetServiceTest();
  User authorizedUser = assetTest.authorizedUser;
  User assetOwner = assetTest.assetOwner;

  @Before
  public void setup() throws Exception {
    super.setup();
    assetTest.setDependencies(userRepository, assetService);
    authorizedUser = users.get(0);
    assetOwner = users.get(1);
  }

  @Test
  public void testCreateSubscription() {
    Asset asset = assetTest.newAsset(authorizedUser, assetOwner);
    Map<String, ServiceData> pkgs = createTestAssetServiceDetails();
    Subscription sub = this.subsService.createSubscription(asset.getId(), pkgs);
    subscRepo.save(sub);// , authorizedUser.getEmail());
    List<Subscription> subscriptions = this.subscRepo.findAll();
    assertNotNull(subscriptions);
    assert (subscriptions.size() >= 1);
  }

  @Test
  public void testSubscribe() {
    this.testCreateSubscription();
    List<Subscription> subscriptions = this.subscRepo.findAll();
    assertNotNull(subscriptions);
    assert (subscriptions.size() > 0);
    Subscription sb = subscriptions.get(0);
    this.subsService.subscribe(sb.getId(), this.createTestPackage());
    sb = this.subscRepo.findOne(sb.getId());
    assertEquals(sb.getState(), QuoteStates.SUBSCRIPTION_REQUESTED);

  }

  @Test
  public void testRateService() {
    this.createTax();
    this.testCreateSubscription();
    this.testSubscribe();
    Subscription sb = this.subscRepo.findAll().get(0);
    System.out.println(sb.getServices());
    this.subsService.rateServices(sb.getId(), "ELECTRICAL", Rating.FIVE);
    sb = this.subscRepo.findOne(sb.getId());
    assertEquals(QuoteStates.RATED, sb.getState());
    assertEquals(sb.areAllServicesRated(), false);
    Map<String, Rating> ratings = new HashMap<String, Rating>();
    ratings.put("ELECTRICAL", Rating.FIVE);
    ratings.put("PLUMBING", Rating.FOUR);
    this.subsService.rateAllServices(sb.getId(), ratings);
    sb = this.subscRepo.findOne(sb.getId());
    assertEquals(sb.getServices().get("ELECTRICAL").getRating(), Rating.FIVE);
    assertEquals(sb.getServices().get("PLUMBING").getRating(), Rating.FOUR);
    assertEquals(sb.areAllServicesRated(), true);
    assert (this.subsService.getStatus(sb.getId()).equals(QuoteStates.QUOTATION_SENT.name()));
  }

  @Test
  public void testRenew() {
    this.testRateService();
    this.createTax();
    Subscription sb = this.subscRepo.findAll().get(0);
    this.subsService.renew(sb.getId(), this.createTestPackage());
    sb = this.subscRepo.findOne(sb.getId());
    assertEquals(sb.getState(), QuoteStates.RENEWED);
  }

  private void createTax() {
    Tax t1 = new Tax("ST", 12.0);
    this.crudService.create(t1, authorizedUser.getEmail());
  }

  private Map<String, ServiceData> createTestAssetServiceDetails() {
    Map<String, ServiceData> datum = new HashMap<String, ServiceData>();
    datum.put("ELECTRICAL", new ServiceData("ELECTRICAL"));
    datum.put("PLUMBING", new ServiceData("PLUMBING"));
    return datum;
  }

  private List<AMCPackage> createTestPackage() {
    AssetBasedService svc = new AssetBasedService("PLUMBING", "Enables the plubming service");
    svc.addPricing(Rating.FIVE, 10.0, 6);
    svc.addPricing(Rating.FOUR, 12.0, 8);
    svc.addPricing(Rating.THREE, 14.0, 10);
    svc = this.crudService.create(svc, authorizedUser.getEmail());
    List<AssetBasedService> services = new ArrayList<AssetBasedService>();
    services.add(svc);
    AMCPackage pkg = new AMCPackage("DefaultPackage", services);
    pkg = crudService.create(pkg, authorizedUser.getEmail());
    List<AMCPackage> pkgs = new ArrayList<AMCPackage>();
    pkgs.add(pkg);
    return pkgs;
  }

  @After
  public void cleanup() {
    this.subscRepo.deleteAll();
    this.assetTest.cleanUp();
    Class[] classes = { Tax.class, AssetBasedService.class, AMCPackage.class };
    for (Class clz : classes)
      this.crudService.removeAll(clz);
  }
}
