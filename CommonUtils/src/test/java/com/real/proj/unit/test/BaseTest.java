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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.real.proj.user.model.User;
import com.real.proj.user.service.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("default")
@EnableAutoConfiguration
// @ComponentScan(basePackages = { "com.real.proj.controller.exception",
// "com.real.proj.controller.exception.handler",
// "com.real.proj.amc.controller", "com.real.proj.amc.service" })
public class BaseTest {

  @Autowired
  protected UserRepository userRepository;

  protected String default_user = "user";
  protected String default_pwd = "user";

  protected List<User> users = new ArrayList<User>();

  @Before
  public void setup() throws MalformedURLException {
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

}
