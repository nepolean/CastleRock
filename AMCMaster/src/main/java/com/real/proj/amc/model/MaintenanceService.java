package com.real.proj.amc.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Services")
public class MaintenanceService extends BaseMasterEntity {

  @Id
  private String id;
  @NotBlank
  private String name;
  @NotBlank
  private String description;
  private List<AssetType> applicableTo;
  private boolean isActive;

  Map<Rating, List<ServiceData>> pricing;

  public MaintenanceService(String name, String description, List<AssetType> applicableTo) {
    this.name = name;
    this.description = description;
    this.applicableTo = applicableTo;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public double getPrice(Rating rating) {
    return getPrice(rating, new Date());
  }

  public double getPrice(Rating rating, Date givenDate) {
    ServiceData sd = querySDByDate(rating, givenDate);
    return sd.getPrice();
  }

  public int getVisitCount(Rating rating) {
    return getVisitCount(rating, null);
  }

  public int getVisitCount(Rating rating, Date givenDate) {
    if (givenDate == null)
      givenDate = new Date();
    ServiceData sd = querySDByDate(rating, givenDate);
    return sd.getVisits();
  }

  public void addPricing(Rating rating, double price, int visits) {
    this.addPricing(rating, price, visits, null);
  }

  public void addPricing(Rating rating, double price, int visits, Date validFrom) {
    if (validFrom == null)
      validFrom = new Date();
    if (this.pricing == null)
      this.pricing = new HashMap<Rating, List<ServiceData>>();
    List<ServiceData> sdList = this.pricing.get(rating);
    if (sdList == null)
      sdList = new ArrayList<ServiceData>();
    ServiceData lastVal = null;
    for (ServiceData data : sdList) {
      if (data.getValidTo() == null) {
        lastVal = data;
        break;
      }
    }
    if (lastVal != null) {
      lastVal.setValidTo(validFrom);
      sdList.remove(lastVal);
      sdList.add(lastVal);
    }
    ServiceData newVal = new ServiceData(visits, price, validFrom);
    sdList.add(newVal);
    this.pricing.put(rating, sdList);
  }

  private ServiceData querySDByDate(Rating rating, Date givenDate) {
    if (pricing == null)
      throw new RuntimeException("Price is not yet defined for the service " + this.name);
    List<ServiceData> sdList = this.pricing.get(rating);
    if (sdList == null)
      throw new RuntimeException("Price is not yet defined for the service " + this.name + " and rating " + rating);
    ServiceData data = null;
    for (ServiceData sd : sdList) {
      if (sd.isInRange(givenDate)) {
        data = sd;
        break;
      }
    }
    if (data == null)
      throw new RuntimeException("Price is not available for the given date");
    return data;
  }

  private Date adjust(Date validFrom) {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    cal.setTime(validFrom);
    cal.set(Calendar.HOUR, 00);
    cal.set(Calendar.MINUTE, 00);
    cal.set(Calendar.SECOND, 00);
    cal.set(Calendar.MILLISECOND, 000);
    cal.set(Calendar.AM_PM, Calendar.AM);
    return cal.getTime();
  }

  private Date dayBefore(Date validFrom) {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    cal.setTime(validFrom);
    cal.add(Calendar.DAY_OF_MONTH, -1);
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 999);
    return cal.getTime();
  }

  static class ServiceMetadata {

  }

  static class Metadata {

  }

  public static void main(String[] args) {
    Date dt = new MaintenanceService(null, null, null).dayBefore(new Date());
    System.out.println(dt.toGMTString());
    dt = new MaintenanceService(null, null, null).adjust(new Date());
    System.out.println(dt.toGMTString());
  }
}
