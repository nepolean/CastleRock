package com.real.proj.amc.unit.test;

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

import com.real.proj.amc.model.AssetType;
import com.real.proj.amc.model.BaseMasterEntity;
import com.real.proj.amc.model.Coupon;
import com.real.proj.amc.model.MaintenanceService;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.Tax;
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
    List<AssetType> applicableTo = new ArrayList<AssetType>();
    applicableTo.add(AssetType.APARTMENT);
    applicableTo.add(AssetType.FLAT);
    MaintenanceService service = new MaintenanceService("ELECTRICAL", "Maintain electrical equipment", applicableTo);
    service.addPricing(Rating.ONE, 100.0, 10);
    service.addPricing(Rating.TWO, 80, 8);
    service.addPricing(Rating.THREE, 60, 6);
    createEntity(service);
  }

  @Test
  public void testUpdateService() {
    testCreateService();
    List<MaintenanceService> services = findAll(MaintenanceService.class);
    MaintenanceService service = services.get(0);
    service.addPricing(Rating.TWO, 120.0, 12);
    service = updateAndFind(service, MaintenanceService.class);
    assertEquals(120.0, service.getPrice(Rating.TWO), 0.1);
    assertEquals(12, service.getVisitCount(Rating.TWO));
    assertEquals(120.0, service.getPrice(Rating.TWO), 0.1);
  }

  @Test
  public void testCurrentPrice() {
    this.testCreateService();
    List<MaintenanceService> services = findAll(MaintenanceService.class);
    MaintenanceService service = services.get(0);
    service.addPricing(Rating.ONE, 120.0, 10, getFutureDate());
    double currentPrice = service.getPrice(Rating.ONE);
    assertEquals(100.0, currentPrice, 0.1);
    double futurePrice = service.getPrice(Rating.ONE, getFutureDate());
    assertEquals(120.0, futurePrice, 0.1);
    try {
      service.getPrice(Rating.ONE, getOldDate());
      fail("Service should fail for non valid date range");
    } catch (Exception ex) {

    }
  }

  @After
  public <E extends BaseMasterEntity> void cleanUp() {
    Class[] adminClasses = { Tax.class, Coupon.class,
        MaintenanceService.class };
    for (Class<E> cls : adminClasses) {
      List<E> objects = crudService.findAll(cls);
      crudService.removeAll(objects);
    }
  }

  /**********************
   * PRIVATE METHODS *
   **********************/
  private void createEntity(BaseMasterEntity service) {
    service = crudService.create(service, defaultUser.getEmail());
    assertNotNull(service.getId());
    assertEquals(service.getCreatedBy(), defaultUser.getEmail());
  }

  private <E extends BaseMasterEntity> List<E> findAll(Class<E> e) {
    List<E> objects = crudService.findAll(e);
    assertNotNull(objects);
    assert (objects.size() > 0);
    return objects;
  }

  private Date getFutureDate() {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 10);
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
