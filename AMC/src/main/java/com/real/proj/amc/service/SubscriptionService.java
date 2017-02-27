package com.real.proj.amc.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import com.real.proj.amc.model.BasePackage;
import com.real.proj.amc.model.Coupon;
import com.real.proj.amc.model.Events;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.ServiceLevelData;
import com.real.proj.amc.model.States;
import com.real.proj.amc.model.Subscription;
import com.real.proj.amc.model.Tax;
import com.real.proj.controller.exception.EntityNotFoundException;
import com.real.proj.user.service.UserService;

@Service
public class SubscriptionService {

  Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

  @Autowired
  StateMachine<States, Events> stateMachine;
  private GenericFCRUDService crudService;

  @Autowired
  public void setFCRUDService(GenericFCRUDService crudService) {
    this.crudService = crudService;
  }

  private AssetService assetService;
  private SubscriptionRepository subsRepo;

  @Autowired
  public SubscriptionService(AssetService assetService, UserService userRepo, SubscriptionRepository subsRepo) {
    this.assetService = assetService;
    this.subsRepo = subsRepo;
  }

  /**
   * Creates a new subscription for a given asset. The details pertaining to
   * each service being added to subscription are collected supplied.
   * 
   * @param assetId
   * @param serviceDetails
   * @return
   */

  public Subscription createSubscription(String assetId, Map<String, ServiceLevelData> serviceDetails) {
    if (serviceDetails == null || serviceDetails.size() < 1)
      throw new RuntimeException("Empty packages provided.");
    logger.debug("New subsscription for Asset {} is requested", assetId);
    this.assetService.getAssetById(assetId);
    Subscription newSubscription = new Subscription(assetId, serviceDetails);
    this.subsRepo.save(newSubscription);
    logger.debug("subscription created");
    return newSubscription;
  }

  /**
   * Creates the subscription request using the packages selected by user.
   * 
   * @param subscriptionId
   * @param pkgs
   */
  public void subscribe(String subscriptionId, List<BasePackage> pkgs) {
    if (pkgs == null || pkgs.isEmpty()) {
      logger.error("Invalid packages supplied");
      throw new RuntimeException("Empty package details are provided.");
    }
    logger.debug("Subscribing with the new packages");
    Subscription sb = getSubscription(subscriptionId);
    sb.subscribe(pkgs);
    this.subsRepo.save(sb);
  }

  /**
   * Rate individual service associated with the subscription; once all the
   * services are rated, a quote will be generated automatically and notify the
   * agent & user.
   * 
   * @param subscriptionId
   * @param serviceName
   * @param rating
   */
  @Secured(value = { "ROLE_ADMIN", "ROLE_TECHNICIAN" })
  public void rateServices(String subscriptionId, String serviceName, Rating rating) {
    logger.debug("Rate a service {} with {} for subscription {}", serviceName, rating, subscriptionId);
    Subscription sb = getSubscription(subscriptionId);
    boolean allRated = sb.rateAService(serviceName, rating);
    logger.debug("Are all the services are rated? {}", allRated);
    if (allRated)
      generateQuotation(sb);
    else
      subsRepo.save(sb);
  }

  /**
   * Rates bunch of services together. Once all services are rated, the system
   * will generate the quotation automatically & notify the respective users.
   * 
   * @param subscriptionId
   * @param ratings
   */
  @Secured(value = { "ROLE_ADMIN, ROLE_TECHNICIAN" })
  public void rateAllServices(String subscriptionId, Map<String, Rating> ratings) {
    if (ratings == null || ratings.size() == 0)
      throw new RuntimeException("User rating not provided.");
    Subscription sb = getSubscription(subscriptionId);
    boolean allRated = sb.rateAllServices(ratings);
    if (allRated)
      generateQuotation(sb);
    else
      subsRepo.save(sb);
  }

  /**
   * Generates a quotation for the given subscription
   * 
   * @param subscriptionId
   */
  public void generateQuotation(String subscriptionId) {
    Subscription sb = getSubscription(subscriptionId);
    generateQuotation(sb);
  }

  /**
   * Generates a new quotation for the given subscription using the applicable
   * coupons
   * 
   * @param subscriptionId
   * @param coupons
   */
  public void generateQuotation(String subscriptionId, List<Coupon> coupons) {
    Subscription sb = getSubscription(subscriptionId);
    if (coupons != null)
      sb.setCoupons(coupons);
    generateQuotation(sb);
  }

  public void generateQuotation(String subscriptionId, Coupon coupon) {
    Subscription sb = getSubscription(subscriptionId);
    if (coupon != null)
      sb.addCoupon(coupon);
    generateQuotation(sb);
  }

  public void renew(String subscriptionId, List<BasePackage> pkgs) {
    this.renew(subscriptionId, pkgs, null);
  }

  public void renew(String subscriptionId, List<BasePackage> pkgs, List<Coupon> coupons) {
    if (pkgs == null || pkgs.isEmpty())
      throw new RuntimeException("Empty package details are provided.");
    logger.debug("Renew the subscription {} ", subscriptionId);
    Subscription sb = getSubscription(subscriptionId);
    List<Tax> taxes = this.getTax();
    sb.renew(pkgs, taxes, coupons);
    this.subsRepo.save(sb);
  }

  private Subscription getSubscription(String subscriptionId) {
    Subscription sb = this.subsRepo.findOne(subscriptionId);
    if (sb == null)
      throw new EntityNotFoundException(subscriptionId, "", "Subscription");
    return sb;
  }

  private void generateQuotation(Subscription sb) {
    logger.debug("Generate a new quotation for the subscription {} " + sb.getId());
    List<Tax> taxes = getTax();
    sb.raiseQuote(taxes);
    this.subsRepo.save(sb);
    logger.debug("Successfully created a new quotation");
    notify(sb);
  }

  private List<Tax> getTax() {
    List<Tax> taxes = this.crudService.findAll(Tax.class);
    if (taxes == null || taxes.isEmpty())
      throw new RuntimeException("System Error. No applicable taxes found");
    return taxes;
  }

  private void notify(Subscription sb) {
    // TODO: schedule a notification on new quotation.

  }

  public String getStatus(String subscriptionId) {
    Subscription sb = getSubscription(subscriptionId);
    return sb.getState().name();
  }
}
