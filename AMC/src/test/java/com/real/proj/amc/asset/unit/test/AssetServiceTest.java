package com.real.proj.amc.asset.unit.test;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
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
public class AssetServiceTest {

  @Autowired
  UserRepository userRepository;
  @Autowired
  AssetRepository assetRepository;
  @Autowired
  AssetService assetService;

  // TODO: DELETE IF THE EXPERIMENT FAILS
  @Autowired
  AssetMasterService assetMasterService;

  private String default_user = "user";
  private String default_pwd = "user";

  private List<User> users = new ArrayList<User>();

  @Before
  public void setup() throws MalformedURLException {
    // base = new URL("http://localhost:" + port);
    userRepository.deleteAll();
    // assetRepository.deleteAll();
    createDummyUsers();
  }

  private void createDummyUsers() {

    User u = new User();
    u.setEmail("user");
    users.add(userRepository.save(u));

    User u1 = new User();
    u1.setEmail("user1@email.com");
    users.add(userRepository.save(u1));

    User u2 = new User();
    u2.setEmail("user2@email.com");
    users.add(userRepository.save(u2));

    User u3 = new User();
    u3.setEmail("user3@email.com");
    users.add(userRepository.save(u3));

  }

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
