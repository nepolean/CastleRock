package com.subsede.amc.catalog.controller;

import java.security.Principal;
import java.util.List;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.subsede.amc.catalog.model.AMCPackage;
import com.subsede.amc.catalog.model.BaseService;
import com.subsede.amc.catalog.model.Category;
import com.subsede.amc.catalog.model.Coupon;
import com.subsede.amc.catalog.model.GeneralService;
import com.subsede.amc.catalog.model.ISubscriptionPackage;
import com.subsede.amc.catalog.model.OneTimeData;
import com.subsede.amc.catalog.model.OneTimeMetadata;
import com.subsede.amc.catalog.model.Service;
import com.subsede.amc.catalog.model.SubscriptionData;
import com.subsede.amc.catalog.model.SubscriptionMetadata;
import com.subsede.amc.catalog.model.Tax;
import com.subsede.amc.catalog.model.TenureBasedDiscount;
import com.subsede.amc.catalog.model.asset.Amenity;
import com.subsede.amc.catalog.model.asset.AssetBasedService;
import com.subsede.amc.catalog.model.asset.RatingBasedSubscriptionData;
import com.subsede.amc.catalog.repository.AmenityRepository;
import com.subsede.amc.catalog.repository.CouponRepository;
import com.subsede.amc.catalog.repository.PackageRepository;
import com.subsede.amc.catalog.repository.ServiceRepository;
import com.subsede.amc.catalog.repository.TaxRepository;
import com.subsede.amc.catalog.service.GenericFCRUDService;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class AMCAdminController {

  private static Logger logger = LoggerFactory.getLogger(AMCAdminController.class);

  GenericFCRUDService crudService;

  private CouponRepository couponRepo;
  private TaxRepository taxRepo;

  private AmenityRepository amenityRepo;
  private ServiceRepository serviceRepo;

  private PackageRepository<ISubscriptionPackage> packageRepo;

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

  @Autowired
  public void setPackageRespository(PackageRepository<ISubscriptionPackage> packageRepo) {
    this.packageRepo = packageRepo;
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
    cpn = Objects.requireNonNull(cpn, "Coupon with id " + id + " nout found.");
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

  /******************* Service 
   * @return *****************************/
  
  @GetMapping(path="/service/categories")
  public ResponseEntity<Category[]> getCategories() {
    return ResponseEntity.ok(Category.values());
  }

  @PostMapping(path = "/service/asset")
  public ResponseEntity<AssetBasedService> createNewAssetService(
      @RequestBody @Validated AssetBasedService baseService) {
    logger.info("Create new service with details {}", baseService);
    AssetBasedService newService = this.serviceRepo.save(baseService);
    return new ResponseEntity<AssetBasedService>(newService, HttpStatus.OK);
  }

  @PostMapping(path = "/service/general")
  public ResponseEntity<GeneralService> createNewGeneralService(
      @RequestBody @Validated GeneralService baseService) {
    logger.info("Create new service with details {}, category {}", baseService, baseService.getCategory());
    GeneralService newService = this.serviceRepo.save(baseService);
    return new ResponseEntity<GeneralService>(newService, HttpStatus.OK);
  }

  @PostMapping(path = "/service/{id}/general/subscription")
  public ResponseEntity<Service> setSubscriptionData(
      @PathVariable @Validated String id,
      @RequestBody @Validated SubscriptionData ratingBasedMetadata) {
    logger.info("Set subscription data for the general service {}", id);
    return updateServiceData(id, ratingBasedMetadata);
  }

  @PostMapping(path = "/service/{id}/asset/subscription")
  public ResponseEntity<Service> setAssetSubscriptionData(
      @PathVariable @Validated String id,
      @RequestBody @Validated RatingBasedSubscriptionData ratingBasedMetadata) {
    logger.info("Set subscription data for the asset based service {}", id);
    return this.updateServiceData(id, ratingBasedMetadata);
  }

  private ResponseEntity<Service> updateServiceData(String id, SubscriptionMetadata ratingBasedMetadata) {
    Service assetService = this.getServiceObject(id);
    if (logger.isDebugEnabled())
      logger.debug("Subscription Metadata \n {}", ratingBasedMetadata);
    assetService.setSubscriptionData(ratingBasedMetadata);
    Service newService = this.serviceRepo.save(assetService);
    return new ResponseEntity<Service>(newService, HttpStatus.OK);
  }

  @PostMapping(path = "/service/{id}/general/onetime")
  public ResponseEntity<Service> setOneTimeData(
      @PathVariable @Validated String id,
      @RequestBody @Validated OneTimeData oneTimeData) {
    logger.info("Set OneTime data for the general service {}", id);
    return updateOneTimeData(id, oneTimeData);
  }

  @PostMapping(path = "/service/{id}/asset/onetime")
  public ResponseEntity<Service> setAssetOneTimeData(
      @PathVariable @Validated String id,
      @RequestBody @Validated OneTimeMetadata ratingBasedMetadata) {
    logger.info("Set OneTime data for asset based service {}", id);
    return this.updateOneTimeData(id, ratingBasedMetadata);
  }

  private ResponseEntity<Service> updateOneTimeData(String id, OneTimeMetadata oneTimeData) {
    Service assetService = getServiceObject(id);
    if (logger.isDebugEnabled())
      logger.debug("Onetime Metadata \n {}", oneTimeData);
    assetService.setOneTimeData(oneTimeData);
    Service newService = this.serviceRepo.save(assetService);
    return new ResponseEntity<Service>(newService, HttpStatus.OK);
  }

  @PostMapping(path = "/service/{id}/enable")
  public ResponseEntity<String> enableServices(@PathVariable @Validated String serviceId) {
    logger.info("Enable service ({})", serviceId);
    Service service = this.serviceRepo.findOne(serviceId);
    ((BaseService) service).setActive(true);
    this.serviceRepo.save(service);
    return new ResponseEntity<String>("Successfully enabled all the services.", HttpStatus.OK);
  }

  @PostMapping(path = "/service/{id}/disable")
  public ResponseEntity<String> disableServices(@PathVariable String serviceId) {
    logger.info("Disable service {}", serviceId);
    Service service = this.serviceRepo.findOne(serviceId);
    ((BaseService) service).setActive(false);
    this.serviceRepo.save(service);
    return new ResponseEntity<String>("Successfully disabled all the services.", HttpStatus.OK);
  }

  @GetMapping(path = "/services")
  public ResponseEntity<Page<Service>> getServices(Pageable pageable) {
    logger.info("Requested for services for page : {}", pageable.getPageNumber());
    Page<Service> services = this.serviceRepo.findAll(pageable);
    if (logger.isDebugEnabled())
      logger.debug("Services loaded from DB -> {}", services);
    return new ResponseEntity<Page<Service>>(services, HttpStatus.OK);
  }

  @GetMapping(path = "/services/{id}")
  public ResponseEntity<Service> getService(@PathVariable String id) {
    Service service = getServiceObject(id);
    return new ResponseEntity<Service>(service, HttpStatus.OK);
  }

  private Service getServiceObject(String id) {
    Service service = this.serviceRepo.findOne(id);
    service = Objects.requireNonNull(service, "Service with id " + id + " not found.");
    return service;
  }

  /******************* package *****************************/

  @RequestMapping(path = "/package", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<ISubscriptionPackage>> getPackages(Pageable page) {
    logger.info("Get packages");
    Page<ISubscriptionPackage> pkgs = this.packageRepo.findAll(page);
    return new ResponseEntity<>(pkgs, HttpStatus.OK);
  }

  @RequestMapping(path = "/package/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ISubscriptionPackage> getPackage(@PathVariable String id) {
    logger.info("Get package with id {}", id);
    ISubscriptionPackage pkg = this.packageRepo.findOne(id);
    pkg = Objects.requireNonNull(pkg, "Package with id " + id + " not found.");
    return new ResponseEntity<>(pkg, HttpStatus.OK);
  }

  @RequestMapping(path = "/package", method = { RequestMethod.POST,
      RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AMCPackage> createPackage(@RequestBody @Validated AMCPackage pkg) {
    logger.info("Create new package for category {}", pkg.getCategory());
    AMCPackage newPkg = this.packageRepo.save(pkg);
    return new ResponseEntity<AMCPackage>(newPkg, HttpStatus.OK);
  }

  @RequestMapping(path = "/package/{id}", method = {
      RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ISubscriptionPackage> setTenureBasedDiscount(
      @PathVariable @Validated String id,
      @RequestBody @Validated TenureBasedDiscount discount) {
    logger.info("Set discount for the package with id {}", id);
    ISubscriptionPackage pkg = this.packageRepo.findOne(id);
    pkg = Objects.requireNonNull(pkg, "Package with id " + id + " not found.");
    pkg.setTenureBasedDisc(discount);
    pkg = this.packageRepo.save(pkg);
    return new ResponseEntity<>(pkg, HttpStatus.OK);
  }

  @RequestMapping(path = "/package/enable", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> enablePackages(@RequestBody @Validated List<String> packages) {
    logger.info("Enable packages ({})", packages.size());
    Iterable<ISubscriptionPackage> allPackages = this.packageRepo.findAll(packages);
    for (ISubscriptionPackage eachPkg : allPackages)
      eachPkg.setActive(true);
    this.packageRepo.save(allPackages);
    return new ResponseEntity<String>("Successfully enabled all the packages.", HttpStatus.OK);
  }

  @RequestMapping(path = "/package/disable", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> disablePackages(@RequestBody @Validated List<String> packages) {
    logger.info("Disable packages ({})", packages.size());
    Iterable<ISubscriptionPackage> allPackages = this.packageRepo.findAll(packages);
    for (ISubscriptionPackage eachPkg : allPackages)
      eachPkg.setActive(false);
    this.packageRepo.save(allPackages);
    return new ResponseEntity<String>("Successfully disabled all the packages.", HttpStatus.OK);
  }

}
