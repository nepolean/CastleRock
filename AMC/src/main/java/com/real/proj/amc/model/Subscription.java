package com.real.proj.amc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.statemachine.StateMachine;

@Document(collection = "Subscriptions")
public class Subscription extends BaseMasterEntity implements Cloneable {

  private static Logger logger = LoggerFactory.getLogger(Subscription.class);

  @Transient
  @Autowired
  StateMachine<States, Events> sm;

  @Id
  String id;
  String assetId;
  String userId;
  Date startDate;
  Date validUpto;
  @Reference
  Quotation quotation;
  SubscriptionStatus status;
  @Reference
  List<Subscription> history;
  @Reference
  List<Coupon> coupons;

  private List<Product> products;

  private States currentState;

  private int tenure;

  public Subscription(String userId, String assetId, List<Product> products, int tenure) {
    this.userId = Objects.requireNonNull(userId, "UserId cannot be null");
    this.assetId = Objects.requireNonNull(assetId, "Asset id cannot be null");
    this.products = validateAndSet(products);
    this.tenure = tenure;
    currentState = States.SUBSCRIPTION_REQUESTED;
  }

  private List<Product> validateAndSet(List<Product> products) {
    products = Objects.requireNonNull(products, "Product data cannot be null");
    for (Product product : products) {
      if (!product.canSubscribe()) {
        if (logger.isErrorEnabled())
          logger.error("The product {} does not support subscription model", product.getName());
        throw new IllegalArgumentException(String.format("The product {} does not support subscription model", product.getName());
      }
    }    
    return products;
  }

  public String getId() {
    return id;
  }

  public Quotation raiseQuote(List<Tax> taxes, List<Coupon> coupons,
      UserInput input) {
    double totalAmount = 0.0;
    double taxAmount = 0.0;
    double discount = 0.0;
    List<String> comments = new ArrayList<String>();
    // TODO: Need to think about how the pricing model works?
    this.pkg.getActualPrice(services, input, PackageScheme.GOLD);
    if (coupons != null)
      try {
        for (Coupon coupon : this.coupons)
          discount += coupon.applyDiscount(this.pkg, totalAmount);
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

  public void renew(AMCPackage pkg, List<SubscriptionService> services, List<Tax> taxes, List<Coupon> coupons,
      UserInput input) {
    if (this.history == null)
      this.history = new ArrayList<Subscription>();
    Subscription old;
    old = (Subscription) this.clone();
    this.history.add(old);
    this.pkg = pkg;
    this.quotation = null;
    this.raiseQuote(services, taxes, coupons, input);
    this.currentState = States.RENEWED;
  }

  public States getState() {
    return currentState;
  }

  public void setState(States state) {
    this.currentState = state;
  }

  public AMCPackage getsubscribedPackage() {
    return pkg;
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
    Collection<ServiceData> userServices = this.services.values();
    for (ServiceData svc : userServices) {
      if (svc.getRating() == null)
        return false;
    }
    return true;
  }

  public Map<String, ServiceData> getServices() {
    return this.services;
  }

  public Subscription clone() {
    Subscription sb = new Subscription();
    sb.assetId = this.assetId;
    sb.coupons = this.coupons;
    sb.createdBy = this.createdBy;
    sb.createdOn = this.createdOn;
    sb.currentState = this.currentState;
    sb.pkg = this.pkg;
    sb.currentState = this.currentState;
    sb.lastModified = this.lastModified;
    sb.modifiedBy = this.modifiedBy;
    sb.quotation = this.quotation;
    return sb;
  }
}
