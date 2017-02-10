package com.real.proj.amc.asset.unit.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.real.proj.amc.model.AMCPackage;
import com.real.proj.amc.model.Apartment;
import com.real.proj.amc.model.Apartment.Details;
import com.real.proj.amc.model.Asset;
import com.real.proj.amc.model.Location;
import com.real.proj.amc.model.MaintenanceService;
import com.real.proj.amc.model.Subscription;
import com.real.proj.amc.model.UOM;
import com.real.proj.amc.service.AssetService;
import com.real.proj.amc.service.GenericFCRUDService;
import com.real.proj.amc.service.SubscriptionRepository;
import com.real.proj.unit.test.BaseTest;
import com.real.proj.user.model.User;
import com.real.proj.user.service.UserRepository;

@EnableAutoConfiguration
@EnableMongoRepositories("com.real.proj.amc.service")
public class AssetServiceTest extends BaseTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  AssetService assetService;

  @Autowired
  private SubscriptionRepository subscRepo;

  @Autowired
  private GenericFCRUDService crudService;

  User authorizedUser;
  User assetOwner;

  @Before
  public void setup() throws Exception {
    super.setup();
    authorizedUser = users.get(0);
    assetOwner = users.get(1);
  }

  @Test
  public void testCreateAsset() throws Exception {
    Asset myAsset = newAsset(authorizedUser, assetOwner);
    assertNotNull(myAsset);
  }

  @Test
  public void testGetMyAssets() throws Exception {
    Asset myAsset = newAsset(authorizedUser, assetOwner);
    List<Asset> myAssets = this.assetService.getMyAssets(assetOwner.getEmail());
    assertNotNull(myAssets);
    assert (myAssets.size() > 0);
  }

  @Test
  public void testCreateSubscription() {
    Asset asset = newAsset(authorizedUser, assetOwner);
    List<AMCPackage> pkgs = createTestPackage();
    Subscription sub = new Subscription(asset, pkgs);
    subscRepo.save(sub);// , authorizedUser.getEmail());
  }

  @Test
  public void testUpdateSubscription() {
    List<Subscription> subscriptions = this.subscRepo.findAll();// Subscription.class);
    subscriptions.forEach(subscription -> {
      subscription.markForDeletion();
      subscription.getsubscribedPackages().add(new AMCPackage("Update", new ArrayList<MaintenanceService>()));
      subscRepo.save(subscription);// , authorizedUser.getEmail());
    });
  }

  @After
  public void cleanUp() {
    // userRepository.deleteAll();
    // assetRepository.deleteAll();
  }

  private Asset newAsset(User authorizedUser, User assetOwner) {
    Location loc = new Location("#123", "Andreson Street", "Benson Colony", "Near Railway Station", "Bengaluru", "KA",
        "INDIA", "560002", 100.0, 100.0);
    Asset myAsset = new Apartment(new Details(100, 2, 1000.0, UOM.SFT));
    myAsset.setOwnedBy(assetOwner);
    // myAsset.setCreatedBy(authorizedUser);
    myAsset.setLocation(loc);
    return assetService.createAsset(myAsset, default_user);
  }

  private List<AMCPackage> createTestPackage() {
    MaintenanceService svc = new MaintenanceService("PLUMBING", "Enables the plubming service");
    svc = crudService.create(svc, authorizedUser.getEmail());
    List<MaintenanceService> services = new ArrayList<MaintenanceService>();
    services.add(svc);
    AMCPackage pkg = new AMCPackage("DefaultPackage", services);
    pkg = crudService.create(pkg, authorizedUser.getEmail());
    List<AMCPackage> pkgs = new ArrayList<AMCPackage>();
    pkgs.add(pkg);
    return pkgs;
  }

}
