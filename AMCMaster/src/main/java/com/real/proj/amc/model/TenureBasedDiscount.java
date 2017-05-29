package com.real.proj.amc.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenureBasedDiscount {

  private static Logger logger = LoggerFactory.getLogger(TenureBasedDiscount.class);

  Map<Integer, Double> discData;

  public TenureBasedDiscount() {
    this.discData = new HashMap<Integer, Double>();
  }

  public void addDiscount(int tenure, double discPct) {
    this.discData.put(tenure, discPct);
  }

  public Set<Integer> getAllTenures() {
    if (discData == null)
      return null;
    return this.discData.keySet();
  }

  public Double getDiscountFor(int tenure) {
    return this.discData.get(tenure);
  }

  @Override
  public String toString() {
    return "TenureBasedDiscount [discData=" + discData + "]";
  }

}
