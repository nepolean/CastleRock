package com.real.proj.amc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

import com.real.proj.amc.service.ServiceData;

@Document(collection = "Subscriptions")
public class Subscription extends BaseMasterEntity implements Cloneable {

  private static Logger logger = LoggerFactory.getLogger(Subscription.class);

  @Id
  String id;
  String assetId;
  String userId;
  Date startDate;
  Date validUpto;

  SubscriptionStatus status;
  @Reference
  List<Subscription> history;
  @Reference
  List<Coupon> coupons;

  private String currentState;

  private UserData userData;

  public Subscription(String userId, String assetId, UserData data) {
    this.userId = Objects.requireNonNull(userId, "UserId cannot be null");
    this.assetId = Objects.requireNonNull(assetId, "Asset id cannot be null");
    this.userData = Objects.requireNonNull(data, "UserData cannot be null");
  }

  public Subscription(String assetId2, Map<String, ServiceData> serviceDetails) {
    // TODO Auto-generated constructor stub
  }

  private List<Product> validateAndSet(List<Product> products) {
    products = Objects.requireNonNull(products, "Product data cannot be null");
    for (Product product : products) {
      if (!product.canSubscribe()) {
        if (logger.isErrorEnabled())
          logger.error("The product {} does not support subscription model", product.getName());
        throw new IllegalArgumentException(
            String.format("The product {} does not support subscription model", product.getName()));
      }
    }
    return products;
  }

  public String getId() {
    return id;
  }

  public void renew(Set<Product> services, List<Tax> taxes, List<Coupon> coupons) {
    if (this.history == null)
      this.history = new ArrayList<Subscription>();
    Subscription old = (Subscription) this.clone();
    this.history.add(old);
  }

  public String getState() {
    return currentState;
  }

  public Set<Product> getsubscribedProducts() {
    return this.userData.getSelectedItems();
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

  public Subscription clone() {
    Subscription sb = new Subscription(userId, assetId, userData);
    // sb.assetId = this.assetId;
    sb.coupons = this.coupons;
    sb.createdBy = this.createdBy;
    sb.createdOn = this.createdOn;
    sb.currentState = this.currentState;
    sb.currentState = this.currentState;
    sb.lastModified = this.lastModified;
    sb.modifiedBy = this.modifiedBy;
    return sb;
  }
}
