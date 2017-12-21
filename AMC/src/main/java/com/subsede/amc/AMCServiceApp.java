package com.subsede.amc;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.real.proj.amc.unit.test.ServiceTestHelper;
import com.subsede.amc.catalog.model.AMCPackage;
import com.subsede.amc.catalog.model.Category;
import com.subsede.amc.catalog.model.Service;
import com.subsede.amc.catalog.model.TenureBasedDiscount;
import com.subsede.amc.catalog.repository.PackageRepository;
import com.subsede.amc.catalog.repository.ServiceRepository;
import com.subsede.amc.model.Asset;
import com.subsede.amc.model.job.Technician;
import com.subsede.amc.repository.AssetRepository;
import com.subsede.util.user.model.User;
import com.subsede.util.user.service.UserRepository;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.subsede.util.controller.exception",
    "com.subsede.util.controller.exception.handler",
    "com.subsede.amc.catalog.controller",
    "com.subsede.amc.catalog.service",
    "com.subsede.amc.catalog.repository",
    "com.subsede.util.user.model",
    "com.subsede.util.user.model.workflow",
    "com.subsede.util.user.service",
    "com.subsede.amc.catalog.model.quote",
    "com.subsede.amc.catalog.model.subscription",
    "com.subsede.util.util" })
@EnableMongoRepositories({
    "com.subsede.util.user.service",
    "com.subsede.amc.catalog.service",
    "com.subsede.amc.catalog.repository",
    "com.subsede.amc.catalog.model.quote" })
@EnableAutoConfiguration
public class AMCServiceApp implements CommandLineRunner {

  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AMCServiceApp.class);

  private UserRepository userRepository;

  private com.subsede.amc.repository.AssetRepository assetRepository;

  private PackageRepository packageRepository;

  private ServiceRepository serviceRepository;

  @Autowired
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Autowired
  public void setQuotationRepository(AssetRepository quoteRepository) {
    this.assetRepository = quoteRepository;
  }

  @Autowired
  public void setServiceRepository(ServiceRepository serviceRepository) {
    this.serviceRepository = serviceRepository;
  }

  @Autowired
  public void setPackageRepository(PackageRepository packageRepository) {
    this.packageRepository = packageRepository;
  }

  public AMCServiceApp() {

  }

  public static void main(String[] args) {
    logger.info("The AMC Service is starting..");
    SpringApplication.run(AMCServiceApp.class, args);
    logger.info("The AMC Service started successfully..");

  }

  public void run(String... args) throws Exception {
    System.setProperty("ENVIRONMENT", "TEST");
    createFewUsers();
    // createAsset();
    createPackage();
  }

  private void createPackage() {
    List<Service> services = ServiceTestHelper.createFewServices();
    logger.info("before saved service with id {}", services);
    services = this.serviceRepository.save(services);
    logger.info("saved service with id {}", services.get(0).getId());
    AMCPackage pkg = new AMCPackage(Category.ASSET, "GOOD PACKAGE", "Package");
    pkg.addService(services.get(0));
    pkg.setActive(true);
    TenureBasedDiscount disc = new TenureBasedDiscount();
    disc.addDiscount(1, 10);
    disc.addDiscount(2, 12);
    disc.addDiscount(3, 15);
    disc.addDiscount(4, 20);
    pkg.setTenureBasedDisc(disc);
    this.packageRepository.save(pkg);
  }

  private void createAsset() {
    User user = userRepository.findByUserName("user1");
    if (user == null)
      throw new NullPointerException();
    Asset newAsset = new Asset(user);
    newAsset = this.assetRepository.save(newAsset);
    String id = newAsset.getId();
    logger.info("{}", id);
    newAsset = null;
    newAsset = this.assetRepository.findOne(id);
    assert (newAsset != null);
  }

  private void createFewUsers() {
    User user = userRepository.findByEmail("user");
    if (null == user) {
      User dummyUser = new User("Dummy", "User", "dummy@email.com", "999999");
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
      User dummyUser = new User("Dummy", "User", "dummy@email.com", "999999");
      dummyUser.setFirstName("Dummy");
      dummyUser.setLastName("Dummy");
      dummyUser.setEmail("user1");
      dummyUser.setMobileNo("99999999");
      System.out.println("User service " + userRepository);
      userRepository.save(dummyUser);
      System.out.println("Dummy Saved");
    }
    Technician technician = (Technician) userRepository.findByEmail("tech1");
    logger.info("{}", technician);
    if (null == technician) {
      Technician newTechnician = new Technician("Dummy", "User", "dummy@email.com", "999999", null, null);
      newTechnician.setFirstName("Technician");
      newTechnician.setLastName("Electrician");
      newTechnician.setEmail("tech1");
      newTechnician.setMobileNo("99999999");
      System.out.println("User service " + userRepository);
      userRepository.save(newTechnician);
      System.out.println("Dummy Technician Saved");
    }
  }
}
