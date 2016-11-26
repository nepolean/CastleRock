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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.real.proj.amc.model.Asset;
import com.real.proj.amc.service.AssetRepository;
import com.real.proj.amc.service.AssetService;
import com.real.proj.user.model.User;
import com.real.proj.user.service.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AssetServiceTest {

  @Autowired
  UserRepository userRepository;
  @Autowired
  AssetRepository assetRepository;
  @Autowired
  AssetService assetService;

  private String default_user = "user";
  private String default_pwd = "user";

  private List<User> users = new ArrayList<User>();

  @Before
  public void setup() throws MalformedURLException {
    // base = new URL("http://localhost:" + port);
    userRepository.deleteAll();
    assetRepository.deleteAll();
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
    Map<String, String> description = new HashMap<String, String>();
    description.put("type", "Apartment");
    description.put("Description", "2BHK");
    Asset myAsset = new Asset("New Apartment", assetOwner.getEmail(), description, authorizedUser);
    Asset newAsset = assetService.createAsset(myAsset, default_user);
    assertNotNull(newAsset);
  }

  @After
  public void cleanUp() {
    userRepository.deleteAll();
    assetRepository.deleteAll();
  }

}
