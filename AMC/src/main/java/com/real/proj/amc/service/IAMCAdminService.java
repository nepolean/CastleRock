package com.real.proj.amc.service;

import java.util.Map;

import com.real.proj.amc.model.Coupon;
import com.real.proj.amc.model.Package;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.Tax;
import com.real.proj.user.model.User;

public interface IAMCAdminService {

  public Tax createTax(Tax tax);

  public Coupon createCoupon(Coupon coupon);

  public User createServiceProvider(User provider);

  public Package createSubscriptionPackage(Package pkg);

  public Package disablePackage(Package pkg);

  public Package enablePackage(Package pkg);

  public Package modifyPrice(Package pkg, Rating rating, double price);

  public Package addPrice(Package pkg, Map<Rating, Double> price);

  public void rateAsset(String assetID, Rating rating);

}
