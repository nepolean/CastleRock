package com.real.proj.amc.service;

import java.util.Map;

import com.real.proj.amc.model.Coupon;
import com.real.proj.amc.model.AMCPackage;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.Tax;
import com.real.proj.user.model.User;

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
