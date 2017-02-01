package com.real.proj.unit.test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.real.proj.user.model.User;
import com.real.proj.user.service.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("default")
@EnableAutoConfiguration
@EnableMongoRepositories("com.real.proj.user.service")
public class BaseTest {

  @Autowired
  protected UserRepository userRepository;

  protected List<User> users = new ArrayList<User>();

  protected String default_user = "user";

  private String default_pwd = "user";

  @Before
  public void setup() throws MalformedURLException, Exception {
    userRepository.deleteAll();
    createDummyUsers();
    // userRepository.save(users);
  }

  @Test
  public void dummy() {

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

  protected User createDummyUser(String userId) {
    User someUser = new User();
    someUser.setEmail(userId);
    someUser = userRepository.save(someUser);
    return someUser;
  }

}
