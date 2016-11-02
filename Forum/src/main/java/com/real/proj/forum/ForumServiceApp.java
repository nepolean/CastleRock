package com.real.proj.forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.real.proj.forum.model.User;
import com.real.proj.forum.service.UserRepository;
import com.real.proj.forum.service.UserServiceImpl;


@SpringBootApplication
@ComponentScan
@EnableMongoRepositories
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
    System.out.println("User Service " + userRepository);
    User dummyUser = new User();
    dummyUser.setFirstName("Dummy");
    dummyUser.setLastName("Dummy");
    dummyUser.setEmail("dummy@gmail.com");
    dummyUser.setMobileNo("99999999");
    System.out.println("User service " + userRepository);
    userRepository.save(dummyUser);
    System.out.println("Dummy Saved");
  }
  
  
  

}
