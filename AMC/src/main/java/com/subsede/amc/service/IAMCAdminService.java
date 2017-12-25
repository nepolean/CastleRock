package com.subsede.amc.service;

import java.util.Map;

import com.subsede.amc.catalog.model.BasePackages;
import com.subsede.amc.catalog.model.Coupon;
import com.subsede.amc.catalog.model.Rating;
import com.subsede.amc.catalog.model.Tax;
import com.subsede.user.model.user.User;

public interface IAMCAdminService {

  public Tax createTax(Tax tax);

  public Coupon createCoupon(Coupon coupon);

  public User createServiceProvider(User provider);

  public BasePackages createSubscriptionPackage(BasePackages pkg);

  public BasePackages disablePackage(BasePackages pkg);

  public BasePackages enablePackage(BasePackages pkg);

  public BasePackages modifyPrice(BasePackages pkg, Rating rating, double price);

  public BasePackages addPrice(BasePackages pkg, Map<Rating, Double> price);

  public void rateAsset(String assetID, Rating rating);

}
