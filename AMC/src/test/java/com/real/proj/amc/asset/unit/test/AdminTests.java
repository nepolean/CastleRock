package com.real.proj.amc.asset.unit.test;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import com.real.proj.amc.model.Coupon;
import com.real.proj.amc.model.Tax;
import com.real.proj.amc.service.GenericFCRUDService;
import com.real.proj.user.model.User;

@EnableAutoConfiguration
public class AdminTests extends BaseTest {

  @Autowired
  GenericFCRUDService crudService;

  @Test
  public void testCreateAdminServices() {
    User defaultUser = this.users.get(0);
    Tax tax1 = new Tax("ST", 12.0);
    tax1 = crudService.create(tax1, defaultUser.getEmail());
    assertNotNull(tax1.getId());
    Coupon coupon = new Coupon("F1000", new Date(), new Date(), 20.0);
    coupon = crudService.create(coupon, defaultUser.getEmail());

  }

  @Test
  public void testUpdateAdminServices() {
    User defaultUser = this.users.get(0);
    testCreateAdminServices();
    List<Tax> taxes = crudService.findAll(Tax.class);
    assertNotNull(taxes);
    assert (taxes.size() > 0);
    double pct = 15.0;
    Tax tax = taxes.get(0);
    tax.setPercentage(pct);
    tax = crudService.update(tax, defaultUser.getEmail());
    tax = crudService.findBy(tax.getId(), Tax.class);
    assertNotNull(tax);
    assert (pct == tax.getPercentage());

    List<Coupon> coupons = crudService.findAll(Coupon.class);
    assertNotNull(coupons);
    assert (coupons.size() > 0);
    Coupon coupon = coupons.get(0);
    coupon.setDiscPct(25.0);
    coupon = crudService.update(coupon, defaultUser.getEmail());
    coupon = crudService.findBy(coupon.getId(), Coupon.class);
    assert (25.0 == coupon.getDiscPct());

  }

  @After
  public void cleanUp() {
    /*
     * List<Tax> taxes = crudService.findAll(Tax.class); for (Tax tax : taxes) {
     * crudService.delete(tax, "tax"); }
     */
  }
}
