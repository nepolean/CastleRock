package com.real.proj.amc.controller;

import java.security.Principal;
import java.util.Date;
import java.util.Objects;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.amc.model.AMCPackage;
import com.real.proj.amc.model.Amenity;
import com.real.proj.amc.model.AssetBasedService;
import com.real.proj.amc.model.Category;
import com.real.proj.amc.model.Coupon;
import com.real.proj.amc.model.Product;
import com.real.proj.amc.model.RatingBasedSubscriptionMetadata;
import com.real.proj.amc.model.Service;
import com.real.proj.amc.model.SubscriptionMetadata;
import com.real.proj.amc.model.Tax;
import com.real.proj.amc.model.deleted.FixedPricingScheme;
import com.real.proj.amc.model.deleted.PriceData;
import com.real.proj.amc.model.deleted.PricingStrategy;
import com.real.proj.amc.model.deleted.RatingBasedPricingScheme;
import com.real.proj.amc.model.deleted.ServiceData;
import com.real.proj.amc.model.deleted.SubscriptionService;
import com.real.proj.amc.repository.AmenityRepository;
import com.real.proj.amc.repository.CouponRepository;
import com.real.proj.amc.repository.PackageRepository;
import com.real.proj.amc.repository.ServiceRepository;
import com.real.proj.amc.repository.TaxRepository;
import com.real.proj.amc.service.GenericFCRUDService;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class AMCAdminController {

  private static Logger logger = LoggerFactory.getLogger(AMCAdminController.class);

  GenericFCRUDService crudService;

  private CouponRepository couponRepo;
  private TaxRepository taxRepo;

  private AmenityRepository amenityRepo;
  private ServiceRepository serviceRepo;

  private PackageRepository packageRepo;

  @Autowired
  public void setGenericFCRUDService(GenericFCRUDService crudService) {
    this.crudService = crudService;
  }

  @Autowired
  public void setCouponRespository(CouponRepository couponRepo) {
    this.couponRepo = couponRepo;
  }

  @Autowired
  public void setTaxRespository(TaxRepository taxRepo) {
    this.taxRepo = taxRepo;
  }

  @Autowired
  public void setAmenityRespository(AmenityRepository amenityRepo) {
    this.amenityRepo = amenityRepo;
  }

  @Autowired
  public void setServiceRespository(ServiceRepository serviceRepo) {
    this.serviceRepo = serviceRepo;
  }

  /******************* coupon *****************************/

  @RequestMapping(path = "/coupons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Coupon>> getCoupons(Pageable pageable) {
    Page<Coupon> coupons = this.couponRepo.findAll(pageable);
    return new ResponseEntity<Page<Coupon>>(coupons, HttpStatus.OK);
  }

  @RequestMapping(path = "/coupons/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Coupon>> getActiveCoupons(Pageable pageable) {
    Page<Coupon> coupons = this.couponRepo.findByIsActiveTrue(pageable);
    return new ResponseEntity<Page<Coupon>>(coupons, HttpStatus.OK);
  }

  @RequestMapping(path = "/coupon/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Coupon> getCoupon(@PathVariable String id) {
    Coupon cpn = this.couponRepo.findOne(id);
    HttpStatus status = (cpn == null) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
    return new ResponseEntity<Coupon>(cpn, HttpStatus.OK);
  }

  @RequestMapping(path = "/coupon", method = { RequestMethod.POST,
      RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Coupon> createCoupon(@RequestBody @Valid Coupon coupon) {
    Coupon cpn = this.couponRepo.save(coupon);
    return new ResponseEntity<Coupon>(cpn, HttpStatus.OK);
  }

  /******************* tax *****************************/
  @RequestMapping(path = "/tax", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Tax>> getTax(Pageable pageable) {
    return new ResponseEntity<Page<Tax>>(this.taxRepo.findAll(pageable), HttpStatus.OK);
  }

  @RequestMapping(path = "/tax/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Tax> getTax(@PathVariable String id) {
    Tax tax = this.taxRepo.findOne(id);
    HttpStatus status = (tax == null) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
    return new ResponseEntity<Tax>(tax, status);
  }

  @RequestMapping(path = "/tax", method = { RequestMethod.POST,
      RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Tax> createCoupon(@RequestBody @Valid Tax tax, Principal adminUser) {
    Tax tx = this.taxRepo.save(tax);
    return new ResponseEntity<Tax>(tx, HttpStatus.OK);
  }

  /******************* Amenity *****************************/
  @RequestMapping(path = "/amenities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Amenity>> getAminities(Pageable pageable) {
    return new ResponseEntity<Page<Amenity>>(this.amenityRepo.findAll(pageable), HttpStatus.OK);
  }

  @RequestMapping(path = "/amenities/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Amenity> getAminities(@PathVariable String id) {
    Amenity amenity = this.amenityRepo.findOne(id);
    HttpStatus status = (amenity == null) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
    return new ResponseEntity<Amenity>(amenity, status);
  }

  @RequestMapping(path = "/amenity", method = { RequestMethod.POST,
      RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Amenity> createCoupon(@RequestBody @Valid Amenity category, Principal adminUser) {
    Amenity amenity = this.amenityRepo.save(category);
    return new ResponseEntity<Amenity>(amenity, HttpStatus.OK);
  }

  /******************* Service *****************************/

  @RequestMapping(path = "/services", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AssetBasedService> createNewAssetService(AssetBasedService baseService, Category category) {
    AssetBasedService newService = this.serviceRepo.save(baseService);
    return new ResponseEntity<AssetBasedService>(newService, HttpStatus.OK);
  }

  @RequestMapping(path = "/services/{id}/subscription/scheme/rating", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Service> setRatingBasedSubscription(
      @PathVariable @Validated String serviceId,
      @RequestBody @Validated RatingBasedSubscriptionMetadata svcData) {
    Service service = this.serviceRepo.findOne(serviceId);
    service = Objects.requireNonNull(service, "Service with id" + serviceId + "not found");
    service.setSubscriptionData(svcData);
    return new ResponseEntity<Service>(service, HttpStatus.OK);
  }

  @RequestMapping(path = "/services/{id}/subscription/scheme/onetime", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Service> setBasicSubscriptionData(
      @PathVariable @Validated String serviceId,
      @RequestBody @Validated SubscriptionMetadata svcData) {
    Service service = this.serviceRepo.findOne(serviceId);
    service = Objects.requireNonNull(service, "Service with id" + serviceId + "not found");
    service.setSubscriptionData(svcData);
    return new ResponseEntity<Service>(service, HttpStatus.OK);
  }

  @RequestMapping(path = "/services", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Service>> getServices(Pageable pageable) {
    logger.info("Requested for services for page : {}", pageable.getPageNumber());
    Page<Service> services = this.serviceRepo.findAll(pageable);
    if (logger.isDebugEnabled())
      logger.debug("Services loaded from DB -> {}", services);
    return new ResponseEntity<Page<Service>>(services, HttpStatus.OK);
  }

  @RequestMapping(path = "/services/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Service> getService(@PathVariable String id) {
    Service service = this.serviceRepo.findOne(id);
    HttpStatus status = Objects.isNull(service) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
    return new ResponseEntity<Service>(service, status);
  }

  @RequestMapping(path = "/admin/services/subscritpion", method = { RequestMethod.POST,
      RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SubscriptionService> creteService(SubscriptionService service) {
    SubscriptionService svc = this.serviceRepo.save(service);
    return new ResponseEntity<SubscriptionService>(svc, HttpStatus.OK);
  }

  // There are multiple cases here
  // - Defining the price for the first time
  // - Updating the price
  // - Updating the strategy
  // - The challenges are many fold:
  // - How to deal with strategy change?
  // - How to deal with whether a given strategy applies to the given service or
  // not?
  @RequestMapping(path = "/admin/service/{id}/price", method = { RequestMethod.POST,
      RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AssetBasedService> definePrice(
      @PathVariable String id,
      @RequestBody String scheme,
      @RequestBody PriceData price,
      @RequestBody Long date) {
    Product svc = this.serviceRepo.findOne(id);
    if (Objects.isNull(svc))
      return new ResponseEntity<AssetBasedService>(HttpStatus.NOT_FOUND);
    PricingStrategy priceStrategy = null;
    if (priceStrategy == null)
      priceStrategy = createPriceStrategy(scheme);
    // changing the scheme?
    else if (!priceStrategy.getName().equals(scheme))
      priceStrategy = createPriceStrategy(scheme);
    Date wef = Objects.isNull(date) ? new Date() : new Date(date);
    priceStrategy.updatePrice(price, wef);
    // svc = this.serviceRepo.save(svc);
    return new ResponseEntity<AssetBasedService>(HttpStatus.OK);
  }

  @RequestMapping(path = "/admin/service/subscritpion/{id}/data", method = { RequestMethod.POST,
      RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SubscriptionService> defineServiceLevelData(
      @PathVariable String id,
      @RequestBody ServiceData sld) {
    Product service = this.serviceRepo.findOne(id);
    if (Objects.isNull(service))
      return new ResponseEntity<SubscriptionService>((SubscriptionService) null, HttpStatus.NOT_FOUND);
    else if (!(service instanceof SubscriptionService))
      return new ResponseEntity<SubscriptionService>((SubscriptionService) null,
          HttpStatus.UNPROCESSABLE_ENTITY);
    SubscriptionService subsService = (SubscriptionService) service;
    // subsService.addServiceLevelData(sld);
    return new ResponseEntity<SubscriptionService>(subsService, HttpStatus.OK);
  }

  private PricingStrategy createPriceStrategy(String scheme) {
    PricingStrategy strategy = null;
    if ("Fixed".equals(scheme))
      strategy = new FixedPricingScheme();
    else if ("RATING_BASED".equals(scheme))
      strategy = new RatingBasedPricingScheme();
    else
      throw new IllegalArgumentException("Invalid scheme specified");
    return strategy;
  }

  /******************* package *****************************/

  @RequestMapping(path = "/admin/package/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AMCPackage> getPackage(@PathVariable String packageId) {
    return new ResponseEntity<AMCPackage>(this.packageRepo.findOne(packageId), HttpStatus.OK);
  }

  @RequestMapping(path = "/admin/package", method = { RequestMethod.POST,
      RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AMCPackage> createPackage(@RequestBody @Valid AMCPackage pkg, Principal adminUser) {
    AMCPackage newPkg = this.packageRepo.save(pkg);
    return new ResponseEntity<AMCPackage>(newPkg, HttpStatus.OK);
  }

}
