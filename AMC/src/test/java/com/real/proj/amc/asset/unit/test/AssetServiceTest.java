package com.real.proj.amc.asset.unit.test;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.real.proj.amc.model.Apartment;
import com.real.proj.amc.model.Apartment.Details;
import com.real.proj.amc.model.Asset;
import com.real.proj.amc.model.Location;
import com.real.proj.amc.model.UOM;
import com.real.proj.amc.service.AssetService;
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

  public void setDependencies(UserRepository userRepository, AssetService assetService) {
    this.userRepository = userRepository;
    this.assetService = assetService;
  }

  protected User authorizedUser;
  protected User assetOwner;

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

  @After
  public void cleanUp() {
    // userRepository.deleteAll();
    // assetRepository.deleteAll();
  }

  protected Asset newAsset(User authorizedUser, User assetOwner) {
    Location loc = new Location("#123", "Andreson Street", "Benson Colony", "Near Railway Station", "Bengaluru", "KA",
        "INDIA", "560002", 100.0, 100.0);
    Asset myAsset = new Apartment(new Details(100, 2, 1000.0, UOM.SFT));
    myAsset.setOwnedBy(assetOwner);
    // myAsset.setCreatedBy(authorizedUser);
    myAsset.setLocation(loc);
    return assetService.createAsset(myAsset, default_user);
  }

}
