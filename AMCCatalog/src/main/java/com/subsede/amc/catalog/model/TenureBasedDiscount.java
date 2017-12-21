package com.subsede.amc.catalog.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenureBasedDiscount {

  private static Logger logger = LoggerFactory.getLogger(TenureBasedDiscount.class);

  private Map<Integer, Double> discData;

  public TenureBasedDiscount() {
    this.discData = new HashMap<Integer, Double>();
  }

  public void addDiscount(int tenure, double discPct) {
    this.discData.put(tenure, discPct);
  }

  public Set<Integer> fetchAllTenures() {
    if (discData == null)
      return null;
    return this.discData.keySet();
  }

  public Double getDiscountFor(int tenure) {
    return this.discData.get(tenure);
  }

  public void setTenureBasedDiscount(Map<Integer, Double> discData) {
    this.discData = discData;
  }

  public Map<Integer, Double> getTenureBasedDiscount() {
    return this.discData;
  }

  @Override
  public String toString() {
    return "TenureBasedDiscount [discData=" + discData + "]";
  }

}
