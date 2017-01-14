package com.real.proj.amc.service;

import com.real.proj.amc.model.Coupon;
import com.real.proj.amc.model.Package;
import com.real.proj.amc.model.Tax;
import com.real.proj.user.model.User;

public interface IAMCAdminService {
  
  
  public Tax createTax(Tax tax);
  public Coupon createCoupon(Coupon coupon);
  public ServiceProvider createServiceProvider(User provider);
  public Package createSubscriptionPackage(Package pkg);
  

}
