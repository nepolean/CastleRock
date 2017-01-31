package com.real.proj.amc.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.amc.model.Coupon;
import com.real.proj.amc.model.Tax;
import com.real.proj.amc.service.GenericFCRUDService;

@RestController
public class AMCAdminController {

  GenericFCRUDService crudService;

  @Autowired
  public void setGenericFCRUDService(GenericFCRUDService crudService) {
    this.crudService = crudService;
  }

  // public void defineAService(@Validated @RequestBody MaintenanceService
  // service, Principal loggedInUser) {

  // }

  public void defineAPackage(@RequestParam String packageName, Principal loggedInUser) {

  }

  public void addAServiceToPackage(@RequestParam String packageId, @RequestParam String sericeId,
      Principal loggedInUser) {

  }

  public void definePricingForAService(@PathVariable String service, @Validated @RequestBody String priceDetails,
      Principal loggedInUser) {

  }

  @RequestMapping(path = "/admin/coupon", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Coupon getCoupon() {
    return this.crudService.findAll(Coupon.class).get(0);
  }

  @RequestMapping(path = "/admin/coupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Coupon> createCoupon(@RequestBody @Valid Coupon coupon) {
    Coupon cpn = this.crudService.create(coupon, "user");
    return new ResponseEntity<Coupon>(cpn, HttpStatus.OK);
  }

  @RequestMapping(path = "/admin/tax", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Tax getTax() {
    return this.crudService.findAll(Tax.class).get(0);
  }

  @RequestMapping(path = "/admin/tax", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Tax> createCoupon(@RequestBody @Valid Tax tax, Principal adminUser) {
    Tax tx = this.crudService.create(tax, adminUser.getName());
    return new ResponseEntity<Tax>(tx, HttpStatus.OK);
  }
}
