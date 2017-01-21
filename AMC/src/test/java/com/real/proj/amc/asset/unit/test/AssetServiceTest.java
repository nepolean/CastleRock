package com.real.proj.amc.asset.unit.test;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.real.proj.amc.model.Apartment;
import com.real.proj.amc.model.Apartment.Details;
import com.real.proj.amc.model.Asset;
import com.real.proj.amc.model.AssetMaster;
import com.real.proj.amc.model.Location;
import com.real.proj.amc.service.AssetMasterService;
import com.real.proj.amc.service.AssetRepository;
import com.real.proj.amc.service.AssetService;
import com.real.proj.user.model.User;
import com.real.proj.user.service.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("default")
@EnableMongoRepositories("com.real.proj.amc.service")
@ComponentScan
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.real.proj.controller.exception", "com.real.proj.controller.exception.handler",
    "com.real.proj.amc.controller", "com.real.proj.amc.service" })
public class AssetServiceTest extends BaseTest {

  @Autowired
  UserRepository userRepository;
  @Autowired
  AssetRepository assetRepository;
  @Autowired
  AssetService assetService;

  // TODO: DELETE IF THE EXPERIMENT FAILS
  @Autowired
  AssetMasterService assetMasterService;

  @Test
  public void testCreateAsset() throws Exception {
    User authorizedUser = users.get(0);
    User assetOwner = users.get(1);
    Asset myAsset = newAsset(authorizedUser, assetOwner);
    assertNotNull(myAsset);
  }

  @Test
  public void testGetMyAssets() throws Exception {
    User authorizedUser = users.get(0);
    User assetOwner = users.get(1);
    Asset myAsset = newAsset(authorizedUser, authorizedUser);
    List<Asset> myAssets = this.assetService.getMyAssets(authorizedUser.getEmail());
    assertNotNull(myAssets);
    assert (myAssets.size() > 0);
  }

  @After
  public void cleanUp() {
    userRepository.deleteAll();
    // assetRepository.deleteAll();
  }

  private Asset newAsset(User authorizedUser, User assetOwner) {
    Location loc = new Location("#123", "Andreson Street", "Benson Colony", "Near Railway Station", "Bengaluru", "KA",
        "INDIA", "560002", 100.0, 100.0);
    Asset myAsset = new Apartment(new Details(100, 1000.0));
    myAsset.setOwnedBy(assetOwner);
    myAsset.setCreatedBy(authorizedUser);
    myAsset.setLocation(loc);

    return assetService.createAsset(myAsset, default_user);

  }

  // TODO: DELETE THIS METHOD
  @Test
  public void testAssetMaster() {
    Map<String, Object> details = new HashMap<String, Object>();
    details.put("ONE", 1);
    details.put("TWO", 2);
    AssetMaster am = new AssetMaster("MYASSET1", new Location(), details);
    am = this.assetMasterService.createAssetMaster(am);

  }

}
