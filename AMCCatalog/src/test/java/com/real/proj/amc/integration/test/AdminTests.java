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

import com.real.proj.unit.test.BaseTest;
import com.subsede.amc.catalog.model.AMCPackage;
import com.subsede.amc.catalog.model.BaseMasterEntity;
import com.subsede.amc.catalog.model.BasePackages;
import com.subsede.amc.catalog.model.BaseService;
import com.subsede.amc.catalog.model.Coupon;
import com.subsede.amc.catalog.model.Tax;
import com.subsede.amc.catalog.model.TimeLine;
import com.subsede.amc.catalog.model.asset.AssetBasedService;
import com.subsede.amc.catalog.model.asset.AssetType;
import com.subsede.amc.catalog.model.deleted.SubscriptionService;
import com.subsede.amc.catalog.service.GenericFCRUDService;
import com.subsede.user.model.user.User;
import com.subsede.user.services.user.UserService;

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

  }

  @Test
  public void testUpdateService() {
    testCreateService();
    List<AssetBasedService> services = findAll(AssetBasedService.class);
    AssetBasedService service = services.get(0);
    // modify pricing strategy
  }

  @Test
  public void testCurrentPrice() {
    this.testCreateService();
    List<AssetBasedService> services = findAll(AssetBasedService.class);
    BaseService service = services.get(0);

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
    BasePackages pkg = createAMCPackage();
    pkg = (BasePackages) this.createEntity(pkg);
    assertNotNull(pkg.getId());
  }

  private AMCPackage createAMCPackage() {
    List<SubscriptionService> services = this.crudService.findAll(SubscriptionService.class);
    String name = "Annual Package";
    String description = "Offers services for 1 year";
    Long tenure = Long.valueOf(12);
    Double disc = Double.valueOf(10);
    AMCPackage pkg = new AMCPackage();
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
