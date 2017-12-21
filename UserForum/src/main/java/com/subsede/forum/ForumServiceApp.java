package com.subsede.forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.subsede.forum.service.ForumService;
import com.subsede.util.user.model.User;
import com.subsede.util.user.service.UserRepository;
import com.subsede.util.user.service.UserService;

@SpringBootApplication(scanBasePackageClasses = { UserService.class, ForumService.class })
@ComponentScan(basePackages = { "com.real.proj.controller.exception", "com.real.proj.controller.exception.handler",
    "com.subsede.forum.controller", "com.subsede.forum.service", "com.real.proj.user.service" })
@EnableMongoRepositories({ "com.real.proj.user.service", "com.subsede.forum.service" })
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
