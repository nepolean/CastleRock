package com.real.proj.amc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import com.real.proj.amc.model.Coupon;
import com.real.proj.amc.model.Events;
import com.real.proj.amc.model.Quotation;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.ServiceLevelData;
import com.real.proj.amc.model.States;
import com.real.proj.amc.model.Subscription;
import com.real.proj.amc.model.Tax;
import com.real.proj.user.service.UserService;

@Service
public class SubscriptionService {

  @Autowired
  StateMachine<States, Events> stateMachine;
  private GenericFCRUDService crudService;

  @Autowired
  public void setFCRUDService(GenericFCRUDService crudService) {
    this.crudService = crudService;
  }

  private AssetService assetService;
  private UserService userRepo;
  private SubscriptionRepository subsRepo;

  @Autowired
  public SubscriptionService(AssetService assetService, UserService userRepo, SubscriptionRepository subsRepo) {
    this.assetService = assetService;
    this.userRepo = userRepo;
    this.subsRepo = subsRepo;
  }

  public void createSubscription(String assetId, Map<String, ServiceLevelData> packages, String loggedInUser) {
    if (packages == null || packages.size() < 1)
      throw new RuntimeException("Empty packages provided.");
    this.assetService.getAssetById(assetId);
    // User authUser = this.userRepo.getUser(loggedInUser);
    Subscription newSubscription = new Subscription(assetId, packages);
    this.subsRepo.save(newSubscription);
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
  @Secured(value = { "ROLE_ADMIN" })
  public void rateServices(String subscriptionId, String serviceName, Rating rating) {
    Subscription sb = subsRepo.findOne(subscriptionId);
    boolean allRated = sb.rateAService(serviceName, rating);
    if (allRated)
      generateQuotation(sb);
  }

  public void rateAllServices(String subscriptionId, Map<String, Rating> ratings) {
    if (ratings == null || ratings.size() == 0)
      throw new RuntimeException("User rating not provided.");
    Subscription sb = subsRepo.findOne(subscriptionId);
    boolean allRated = sb.rateAllServices(ratings);
    if (allRated)
      generateQuotation(sb);
  }

  public void generateQuotation(String subscriptionId) {
    Subscription sb = subsRepo.findOne(subscriptionId);
    generateQuotation(sb);
  }

  public void generateQuotation(String subscriptionId, List<Coupon> coupons) {
    Subscription sb = subsRepo.findOne(subscriptionId);
    if (coupons != null)
      sb.setCoupons(coupons);
    generateQuotation(sb);
  }

  public void generateQuotation(String subscriptionId, Coupon coupon) {
    Subscription sb = subsRepo.findOne(subscriptionId);
    if (coupon != null)
      sb.addCoupon(coupon);
    generateQuotation(sb);
  }

  private void generateQuotation(Subscription sb) {
    List<Tax> taxes = this.crudService.findAll(Tax.class);
    if (taxes == null)
      throw new RuntimeException("System Error. No applicable taxes found");
    Quotation quote = sb.raiseQuote(taxes);
    this.subsRepo.save(sb);
    notify(sb);
  }

  public void pay() {
  }

  public void subscribe(String subscriptionId) {
  }

  public String getStatus(String id) {
    Subscription sb = subsRepo.findOne(id);
    return sb.getStatus().name();
  }
}
