package com.real.proj.forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.real.proj.controller.exception.handler.SimpleError;
import com.real.proj.forum.model.User;
import com.real.proj.forum.service.UserRepository;

@SpringBootApplication
@ComponentScan(basePackages = { "com.real.proj.controller.exception", "com.real.proj.controller.exception.handler",
    "com.real.proj.forum.controller", "com.real.proj.forum.service" })
@EnableMongoRepositories
@EnableAutoConfiguration
public class ForumServiceApp implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public ForumServiceApp() {

  }

  public static void main(String[] args) {
    SpringApplication.run(ForumServiceApp.class, args);
  }

  public void run(String... args) throws Exception {
    SimpleError err = new SimpleError("");

    System.out.println("User Service " + userRepository);
    createFewUsers();
  }

  private void createFewUsers() {
    User user = userRepository.findByEmail("user");
    if (null == user) {
      User dummyUser = new User();
      dummyUser.setFirstName("Dummy");
      dummyUser.setLastName("Dummy");
      dummyUser.setEmail("user");
      dummyUser.setMobileNo("99999999");
      System.out.println("User service " + userRepository);
      userRepository.save(dummyUser);
      System.out.println("Dummy Saved");
    }
    user = userRepository.findByEmail("user1");
    if (null == user) {
      User dummyUser = new User();
      dummyUser.setFirstName("Dummy");
      dummyUser.setLastName("Dummy");
      dummyUser.setEmail("user1");
      dummyUser.setMobileNo("99999999");
      System.out.println("User service " + userRepository);
      userRepository.save(dummyUser);
      System.out.println("Dummy Saved");
    }
  }
}
