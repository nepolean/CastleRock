//package com.example;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class MvcDemoApplication {
//	
//	public static void main(String[] args) {
//		SpringApplication.run(MvcDemoApplication.class, args);
//	}
//}
package com.subsede.user.main;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.subsede.user.config.MVCConfig;
import com.subsede.user.config.SecurityConfig;
import com.subsede.user.model.Customer;
import com.subsede.user.model.user.Role;
import com.subsede.user.model.user.User;
import com.subsede.user.repository.CustomerRepository;
import com.subsede.user.repository.user.RoleRepository;
import com.subsede.user.repository.user.UserRepository;

//@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = { CustomerRepository.class, UserRepository.class })
@Import({ MVCConfig.class, SecurityConfig.class })
public class CompanyApplication implements CommandLineRunner {

  @Autowired
  private CustomerRepository repository;

  @Autowired
  private UserRepository<User> uRepository;

  @Autowired
  private RoleRepository rRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public static void main(String[] args) {
    SpringApplication.run(CompanyApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

    repository.deleteAll();
    uRepository.deleteAll();
    rRepository.deleteAll();

    List<Role> roleList = new LinkedList<>();
    roleList.add(new Role("USER"));
    roleList.add(new Role("USER_ADMIN"));
    roleList.add(new Role("ADMIN"));
    rRepository.save(roleList);

    List<User> userList = new LinkedList<>();

    String encodedPassword = passwordEncoder.encode("password");
    User user = new User("user1234", encodedPassword);
    user.setEmail("user@gmail.com");
    user.setFirstName("Arun");
    user.setMiddleName("Kumar");
    user.setLastName("Yathiraj");
    user.setAccountLocked(false);
    user.addRole(rRepository.findByName("USER"));
    userList.add(user);
    user = new User("admin1234", encodedPassword);
    user.setEmail("admin@gmail.com");
    user.setFirstName("Arun");
    user.setMiddleName("Kumar");
    user.setLastName("Yathiraj");
    user.setAccountLocked(false);
    user.addRole(rRepository.findByName("ADMIN"));
    userList.add(user);
    uRepository.save(userList);

    List<Customer> customerList = new LinkedList<>();
    Customer customer = new Customer("arunkytg", encodedPassword, "Manyata");
    customer.setEmail("arunkytg@gmail.com");
    user.setFirstName("Arun");
    user.setMiddleName("Kumar");
    user.setLastName("Yathiraj");
    customer.addRole(rRepository.findByName("USER"));
    customerList.add(customer);

    customer = new Customer("yathiraj", encodedPassword, "Manyata");
    customer.setEmail("yathiraj@gmail.com");
    user.setFirstName("Yathiraj");
    user.setLastName("Rangasthala");
    customer.addRole(rRepository.findByName("ADMIN"));
    customerList.add(customer);
    repository.save(customerList);

    // fetch all customers
    System.out.println("Customers found with findAll():");
    System.out.println("-------------------------------");
    for (Customer c : repository.findAll()) {
      System.out.println(c);
    }
    System.out.println();

    // fetch an individual customer
    System.out.println("Customer found with findByUsername('arun'):");
    System.out.println("--------------------------------");
    System.out.println(repository.findByUsername("arun"));

  }

}