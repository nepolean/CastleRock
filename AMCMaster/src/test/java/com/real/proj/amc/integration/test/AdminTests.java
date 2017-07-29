package com.real.proj.amc.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import com.real.proj.amc.model.AMCPackage;
import com.real.proj.amc.model.BaseMasterEntity;
import com.real.proj.amc.model.BaseService;
import com.real.proj.amc.model.Coupon;
import com.real.proj.amc.model.PackageScheme;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.Tax;
import com.real.proj.amc.model.TimeLine;
import com.real.proj.amc.model.UserInput;
import com.real.proj.amc.model.asset.AssetBasedService;
import com.real.proj.amc.model.asset.AssetType;
import com.real.proj.amc.model.deleted.FixedPricingScheme;
import com.real.proj.amc.model.deleted.FixedPricingScheme.FixedPrice;
import com.real.proj.amc.model.deleted.PricingStrategy;
import com.real.proj.amc.model.deleted.RatingBasedPricingScheme;
import com.real.proj.amc.model.deleted.RatingBasedPricingScheme.RatingBasedPrice;
import com.real.proj.amc.model.deleted.ServiceData;
import com.real.proj.amc.model.deleted.SubscriptionService;
import com.real.proj.amc.service.GenericFCRUDService;
import com.real.proj.unit.test.BaseTest;
import com.real.proj.user.model.User;
import com.real.proj.user.service.UserService;

@SpringBootTest(classes = { UserService.class, GenericFCRUDService.class })
@EnableAutoConfiguration
public class AdminTests extends BaseTest {

  @Autowired
  GenericFCRUDService crudService;
  User defaultUser;

  @Before
  public void setup() throws Exception {
    super.setup();
    defaultUser = this.users.get(0);
  }

  @Test
  public void testCreateTax() {
    Tax tax1 = new Tax("ST", 12.0);
    this.createEntity(tax1);
  }

  @Test
  public void testUpdateTax() {
    testCreateTax();
    List<Tax> taxes = findAll(Tax.class);
    Tax tax = taxes.get(0);
    double pct = 15.0;
    tax.setPercentage(pct);
    tax = updateAndFind(tax, Tax.class);
    assertNotNull(tax);
    assert (pct == tax.getPercentage());
  }

  @Test
  public void testCreateCoupon() {
    Coupon coupon = new Coupon("F1000", new Date(), new Date(), 20.0);
    this.createEntity(coupon);
  }

  @Test
  public void testUpdateCoupon() {
    testCreateCoupon();
    List<Coupon> coupons = findAll(Coupon.class);
    Coupon coupon = coupons.get(0);
    coupon.setDiscPct(25.0);
    coupon = updateAndFind(coupon, Coupon.class);
    assert (25.0 == coupon.getDiscPct());
  }

  @Test
  public void testCreateService() {
    Date dt = new Date(System.currentTimeMillis());
    List<AssetType> applicableTo = new ArrayList<AssetType>();
    applicableTo.add(AssetType.APARTMENT);
    applicableTo.add(AssetType.FLAT);
    List<String> amenities = new ArrayList<String>();
    amenities.add(new String("ELECTRICITY"));

    RatingBasedPrice price = new RatingBasedPrice();
    price.addPriceFor(Rating.ONE, 100.0);
    price.addPriceFor(Rating.TWO, 80.0);
    price.addPriceFor(Rating.THREE, 60.0);

    RatingBasedPricingScheme pricing = new RatingBasedPricingScheme(price, dt);

    RatingBasedPrice price1 = new RatingBasedPrice();
    price1.addPriceFor(Rating.ONE, 105.0);
    price1.addPriceFor(Rating.TWO, 85.0);
    price1.addPriceFor(Rating.THREE, 65.0);
    price1.addPriceFor(Rating.FOUR, 90.0);
    price1.addPriceFor(Rating.FIVE, 95.0);

    pricing.updatePrice(price1, getFutureDate(2));

    AssetBasedService service = new SubscriptionService("New Category", "ELECTRICAL",
        "Maintain electrical equipment",
        applicableTo,
        amenities);
    service.setPricingStrategy(pricing);
    ServiceData sld = new ServiceData(PackageScheme.GOLD, 10);
    ((SubscriptionService) service).addServiceLevelData(sld);
    service = (AssetBasedService) createEntity(service);
    UserInput<String, Object> input = new UserInput<String, Object>();
    input.add(RatingBasedPricingScheme.RATING, Rating.ONE);
    double currentPrice = service.getPricingStrategy().getPrice(input);
    assertEquals(100.0, currentPrice, 10);
  }

  @Test
  public void testUpdateService() {
    testCreateService();
    List<AssetBasedService> services = findAll(AssetBasedService.class);
    AssetBasedService service = services.get(0);
    // modify pricing strategy
    PricingStrategy pricing = new FixedPricingScheme(new FixedPrice(100.0));
    service.setPricingStrategy(pricing);
    // modify delivery method
    service = updateAndFind(service, AssetBasedService.class);
    assertEquals(100.0, service.getPrice(null), 0.1);
  }

  @Test
  public void testCurrentPrice() {
    this.testCreateService();
    List<AssetBasedService> services = findAll(AssetBasedService.class);
    BaseService service = services.get(0);
    PricingStrategy pricingStrategy = service.getPricingStrategy();
    UserInput<String, Object> userInput = new UserInput<String, Object>();
    userInput.add(RatingBasedPricingScheme.RATING, Rating.ONE);
    double currentPrice = pricingStrategy.getPrice(userInput);
    assertEquals(100.0, currentPrice, 0.1);
    double futurePrice = pricingStrategy.getPrice(userInput, getFutureDate(3));
    assertEquals(100.0, futurePrice, 0.1);
  }

  @Test
  public void testHistoryObject() {
    TimeLine<Integer> history = new TimeLine<Integer>();
    Integer one = Integer.valueOf(1);
    history.addToHistory(one, new Date());
    assertEquals(one, history.getCurrentValue());
    Integer two = Integer.valueOf(2);
    Date tomorrow = getFutureDate(1);
    history.addToHistory(two, tomorrow);
    assertEquals(one, history.getCurrentValue());
    tomorrow = getFutureDate(2);
    Integer three = Integer.valueOf(3);
    history.addToHistory(three, tomorrow);
    history.addToHistory(Integer.valueOf(4), getFutureDate(3));
    Date myDate = getFutureDate(2);
    Integer output = history.getValueForDate(myDate);
    assertEquals(three, output);
  }

  @Test
  public void testCreateAMCPackage() {
    AMCPackage pkg = createAMCPackage();
    pkg = (AMCPackage) this.createEntity(pkg);
    assertNotNull(pkg.getId());
  }

  private AMCPackage createAMCPackage() {
    List<SubscriptionService> services = this.crudService.findAll(SubscriptionService.class);
    String name = "Annual Package";
    String description = "Offers services for 1 year";
    Long tenure = Long.valueOf(12);
    Double disc = Double.valueOf(10);
    AMCPackage pkg = new AMCPackage(name, description, tenure, disc, services);
    return pkg;
  }

  @Test
  public void testPackage() {
    AMCPackage pkg = this.createAMCPackage();
    try {
      pkg.addService(null);
      fail("Package should reject null service");
    } catch (IllegalArgumentException ex) {
    }
  }

  @After
  public <E extends BaseMasterEntity> void cleanUp() {
    Class[] adminClasses = { Tax.class, Coupon.class,
        AssetBasedService.class };
    for (Class<E> cls : adminClasses) {
      List<E> objects = crudService.findAll(cls);
      // crudService.removeAll(objects);
    }

  }

  /**********************
   * PRIVATE METHODS *
   **********************/
  private BaseMasterEntity createEntity(BaseMasterEntity service) {
    service = crudService.create(service, defaultUser.getEmail());
    assertNotNull(service.getId());
    assertEquals(service.getCreatedBy(), defaultUser.getEmail());
    return service;
  }

  private <E extends BaseMasterEntity> List<E> findAll(Class<E> e) {
    List<E> objects = crudService.findAll(e);
    assertNotNull(objects);
    assert (objects.size() > 0);
    return objects;
  }

  private Date getFutureDate(int daysAfter) {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + daysAfter);
    System.out.println(cal.toString());
    return cal.getTime();
  }

  private Date getOldDate() {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 10);
    System.out.println(cal.toString());
    return cal.getTime();
  }

  private <E extends BaseMasterEntity> E updateAndFind(E entity, Class<E> clazz) {
    entity = crudService.update(entity, defaultUser.getEmail());
    entity = crudService.findBy(entity.getId(), clazz);
    assertNotNull(entity);
    return entity;
  }
}
