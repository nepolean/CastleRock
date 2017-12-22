package com.subsede.amc.service;

import java.util.Map;

import com.subsede.amc.catalog.model.AMCPackage;
import com.subsede.amc.catalog.model.Coupon;
import com.subsede.amc.catalog.model.Rating;
import com.subsede.amc.catalog.model.Tax;
import com.subsede.user.model.user.User;

public interface IAMCAdminService {

  public Tax createTax(Tax tax);

  public Coupon createCoupon(Coupon coupon);

  public User createServiceProvider(User provider);

  public AMCPackage createSubscriptionPackage(AMCPackage pkg);

  public AMCPackage disablePackage(AMCPackage pkg);

  public AMCPackage enablePackage(AMCPackage pkg);

  public AMCPackage modifyPrice(AMCPackage pkg, Rating rating, double price);

  public AMCPackage addPrice(AMCPackage pkg, Map<Rating, Double> price);

  public void rateAsset(String assetID, Rating rating);

}
