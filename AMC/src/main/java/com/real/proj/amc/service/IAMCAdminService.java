package com.real.proj.amc.service;

import java.util.Map;

import com.real.proj.amc.model.Coupon;
import com.real.proj.amc.model.BasePackage;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.Tax;
import com.real.proj.user.model.User;

public interface IAMCAdminService {

  public Tax createTax(Tax tax);

  public Coupon createCoupon(Coupon coupon);

  public User createServiceProvider(User provider);

  public BasePackage createSubscriptionPackage(BasePackage pkg);

  public BasePackage disablePackage(BasePackage pkg);

  public BasePackage enablePackage(BasePackage pkg);

  public BasePackage modifyPrice(BasePackage pkg, Rating rating, double price);

  public BasePackage addPrice(BasePackage pkg, Map<Rating, Double> price);

  public void rateAsset(String assetID, Rating rating);

}
