package com.real.proj.amc;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.real.proj.amc.model.AMCPackage;
import com.real.proj.amc.model.Asset;
import com.real.proj.amc.model.BaseService;
import com.real.proj.amc.model.Category;
import com.real.proj.amc.model.TenureBasedDiscount;
import com.real.proj.amc.repository.PackageRepository;
import com.real.proj.amc.repository.ServiceRepository;
import com.real.proj.amc.service.AssetRepository;
import com.real.proj.amc.unit.test.ServiceTestHelper;
import com.real.proj.user.model.User;
import com.real.proj.user.service.UserRepository;

@SpringBootApplication
@ComponentScan(basePackages = { "com.real.proj.controller.exception", "com.real.proj.controller.exception.handler",
    "com.real.proj.amc.controller", "com.real.proj.amc.service", "com.real.proj.amc.repository",
    "com.real.proj.user.model",
    "com.real.proj.user.model.workflow",
    "com.real.proj.user.service",
    "com.real.proj.amc.model.quote",
    "com.real.proj.util" })
@EnableMongoRepositories({ "com.real.proj.user.service", "com.real.proj.amc.service", "com.real.proj.amc.repository",
    "com.real.proj.amc.model.quote" })
@EnableAutoConfiguration
public class AMCServiceApp implements CommandLineRunner {

  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AMCServiceApp.class);

  private UserRepository userRepository;

  private AssetRepository assetRepository;

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
    List<BaseService> services = ServiceTestHelper.createFewServices();
    services = this.serviceRepository.save(services);
    AMCPackage pkg = new AMCPackage(Category.ASSET, "GOOD PACKAGE", "Package");
    pkg.addServices(services);
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
    Asset newAsset = new Asset(user, user);
    newAsset = this.assetRepository.save(newAsset);
    String id = newAsset.getId();
    logger.info("{}", id);
    newAsset = null;
    Asset newQuote = this.assetRepository.findOne(id);
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
  }
}
