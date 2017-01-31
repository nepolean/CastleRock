package com.real.proj.amc.asset.unit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import com.real.proj.amc.model.BaseMasterEntity;
import com.real.proj.amc.model.Coupon;
import com.real.proj.amc.model.MaintenanceService;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.Tax;
import com.real.proj.amc.service.GenericFCRUDService;
import com.real.proj.unit.test.BaseTest;
import com.real.proj.user.model.User;
import com.real.proj.user.service.UserService;

@SpringBootTest(classes = { GenericFCRUDService.class, UserService.class })
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
    tax = crudService.update(tax, defaultUser.getEmail());
    tax = crudService.findBy(tax.getId(), Tax.class);
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
    coupon = crudService.update(coupon, defaultUser.getEmail());
    coupon = crudService.findBy(coupon.getId(), Coupon.class);
    assert (25.0 == coupon.getDiscPct());
  }

  @Test
  public void testCreateService() {
    MaintenanceService service = new MaintenanceService("ELECTRICAL", "Maintain electrical equipment");
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
    service.addPricing(Rating.ONE, 120.0, 12);
    service = updateAndFind(service, MaintenanceService.class);
    // assertEquals(service.getPrice(Rating.ONE), 120.0);
    assertEquals(service.getVisitCount(Rating.ONE), 12);
  }

  @After
  public void cleanUp() {
    /*
     * List<Tax> taxes = crudService.findAll(Tax.class); for (Tax tax : taxes) {
     * crudService.delete(tax, "tax"); }
     */
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

  private <E extends BaseMasterEntity> E updateAndFind(E entity, Class<E> clazz) {
    entity = crudService.update(entity, defaultUser.getEmail());
    entity = crudService.findBy(entity.getId(), clazz);
    assertNotNull(entity);
    return entity;
  }
}
