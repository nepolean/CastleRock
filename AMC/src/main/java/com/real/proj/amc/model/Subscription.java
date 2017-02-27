package com.real.proj.amc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.statemachine.StateMachine;

@Document(collection = "Subscriptions")
public class Subscription extends BaseMasterEntity implements Cloneable {

  @Transient
  @Autowired
  StateMachine<States, Events> sm;

  @Id
  String id;

  String assetId;
  @Reference
  List<BasePackage> packages;
  @Reference
  Map<String, ServiceLevelData> services;
  Date startDate;
  Date validUpto;
  Quotation quotation;
  States currentState;
  SubscriptionStatus status;
  List<Subscription> history;
  List<Coupon> coupons;

  public Subscription() {

  }

  public Subscription(String assetId, Map<String, ServiceLevelData> services) {
    this.assetId = assetId;
    this.services = services;
    currentState = States.CREATED;
  }

  public void subscribe(List<BasePackage> packages) {
    this.packages = packages;
    this.currentState = States.SUBSCRIPTION_REQUESTED;
  }

  public String getId() {
    return id;
  }

  public boolean rateAService(String serviceName, Rating rating) {
    ServiceLevelData sd = this.services.get(serviceName);
    sd.setUserRating(rating);
    this.currentState = States.RATED;
    return areAllServicesRated();
  }

  public boolean rateAllServices(Map<String, Rating> serviceRatings) {
    serviceRatings.forEach((name, rating) -> {
      rateAService(name, rating);
    });
    return areAllServicesRated();
  }

  public void renew(List<BasePackage> pkgs, List<Tax> taxes, List<Coupon> coupons) {
    if (this.history == null)
      this.history = new ArrayList<Subscription>();
    Subscription old;
    old = (Subscription) this.clone();
    this.history.add(old);
    this.packages = pkgs;
    this.quotation = null;
    this.raiseQuote(taxes);
    this.currentState = States.RENEWED;
  }

  public Quotation raiseQuote(List<Tax> taxes) {
    double totalAmount = 0.0;
    double taxAmount = 0.0;
    double discount = 0.0;
    List<String> comments = new ArrayList<String>();
    for (BasePackage pkg : this.packages) {
      List<MaintenanceService> services = pkg.getServices();
      for (MaintenanceService service : services) {
        Rating userRating = this.services.get(service.getName()).getRating();
        double price = service.getPrice(userRating);
        totalAmount += price;
      }
    }
    if (coupons != null)
      try {
        for (Coupon coupon : this.coupons)
          discount += coupon.applyDiscount(this.packages, totalAmount);
      } catch (InvalidCouponException e) {
        comments.add(e.getMessage());
      }

    for (Tax tax : taxes) {
      // TODO : Should tax be calculated on total amount or discounted amount?
      taxAmount += tax.calcTax(totalAmount);
    }

    Quotation quote = new Quotation(totalAmount, taxAmount, discount);

    this.quotation = quote;
    this.currentState = States.QUOTATION_SENT;
    return quote;
  }

  public States getState() {
    return currentState;
  }

  public void setState(States state) {
    this.currentState = state;
  }

  public List<BasePackage> getsubscribedPackages() {
    return packages;
  }

  public void setCoupons(List<Coupon> coupons) {
    this.coupons = coupons;
  }

  public List<Coupon> getCoupons() {
    return this.coupons;
  }

  public void addCoupon(Coupon coupon) {
    if (this.coupons == null)
      this.coupons = new ArrayList<Coupon>();
    this.coupons.add(coupon);
  }
  /*
   * public void rateService(String serviceId, Rating rating) { Message<Events>
   * msg = MessageBuilder .withPayload(Events.rate) .setHeader("USER",
   * "user@email.com") .build(); // sm.sendEvent(msg); }
   * 
   * /* sm.getStateMachineAccessor().doWithAllRegions((context) -> {
   * context.resetStateMachine(new DefaultStateMachineContext<States,
   * Events>(state, null, null, null)); }); sm.start();
   *
   * public void mapToCurrentState(StateMachine<States, Events> sm) {
   * 
   * }
   */

  public boolean areAllServicesRated() {
    if (this.services == null)
      return false;
    Collection<ServiceLevelData> userServices = this.services.values();
    for (ServiceLevelData svc : userServices) {
      if (svc.getRating() == null)
        return false;
    }
    return true;
  }

  public Map<String, ServiceLevelData> getServices() {
    return this.services;
  }

  public Subscription clone() {
    Subscription sb = new Subscription();
    sb.assetId = this.assetId;
    sb.coupons = this.coupons;
    sb.createdBy = this.createdBy;
    sb.createdOn = this.createdOn;
    sb.currentState = this.currentState;
    sb.packages = this.packages;
    sb.currentState = this.currentState;
    sb.lastModified = this.lastModified;
    sb.modifiedBy = this.modifiedBy;
    sb.quotation = this.quotation;
    return sb;
  }
}
