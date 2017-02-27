package com.real.proj.amc.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Services")
public class MaintenanceService extends BaseMasterEntity {

  @Id
  private String id;
  @NotNull
  Category category;
  @NotBlank
  private String name;
  @NotBlank
  private String description;
  private List<AssetType> applicableTo;
  private List<Amenity> amenities;
  private boolean isActive;

  // This is only one kind of pricing which is 'Rating' based.
  // There are multiple options
  // 1 - Whether the price of this service is area based / qty based / any other
  // parameter?
  // 1 - Whether this service is available as AMC / OneTime / Both?
  // 3 - Whether the price of this service is fixed | rating based | any other
  // parameter?

  // Moreover we should extract the pricing details out.

  // Map<Rating, List<ServiceData>> pricing;

  DeliveryModel deliveryModel;

  PricingStrategy pricingStrategy;

  public MaintenanceService(String name, String description, List<AssetType> applicableTo, List<Amenity> amenities,
      DeliveryModel deliveryModel, PricingStrategy pricingStrategy) {
    this.name = name;
    this.description = description;
    this.applicableTo = applicableTo;
    this.amenities = amenities;
    this.deliveryModel = deliveryModel;
    this.pricingStrategy = pricingStrategy;
    this.isActive = false;
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

  public double getPrice(UserInput input) {
    return this.pricingStrategy.getPrice(input);
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public List<AssetType> getApplicableTo() {
    return applicableTo;
  }

  public void setApplicableTo(List<AssetType> applicableTo) {
    this.applicableTo = applicableTo;
  }

  public List<Amenity> getAmenities() {
    return amenities;
  }

  public void setAmenities(List<Amenity> amenities) {
    this.amenities = amenities;
  }

  public DeliveryModel getDeliveryModel() {
    return deliveryModel;
  }

  public void setDeliveryModel(DeliveryModel deliveryModel) {
    this.deliveryModel = deliveryModel;
  }

  public PricingStrategy getPricingStrategy() {
    return pricingStrategy;
  }

  public void setPricingStrategy(PricingStrategy pricingStrategy) {
    this.pricingStrategy = pricingStrategy;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * public double getPrice(Rating rating, Date givenDate) { ServiceData sd =
   * querySDByDate(rating, givenDate); return sd.getPrice(); }
   * 
   * public int getVisitCount(Rating rating) { return getVisitCount(rating,
   * null); }
   * 
   * public int getVisitCount(Rating rating, Date givenDate) { if (givenDate ==
   * null) givenDate = new Date(); ServiceData sd = querySDByDate(rating,
   * givenDate); return sd.getVisits(); }
   * 
   * 
   * public void addPricing(Rating rating, double price, int visits) {
   * this.addPricing(rating, price, visits, null); }
   * 
   * public void addPricing(Rating rating, double price, int visits, Date
   * validFrom) { if (validFrom == null) validFrom = new Date(); if
   * (this.pricing == null) this.pricing = new HashMap<Rating,
   * List<ServiceData>>(); List<ServiceData> sdList = this.pricing.get(rating);
   * if (sdList == null) sdList = new ArrayList<ServiceData>(); ServiceData
   * lastVal = null; for (ServiceData data : sdList) { if (data.getValidTo() ==
   * null) { lastVal = data; break; } } if (lastVal != null) {
   * lastVal.setValidTo(validFrom); sdList.remove(lastVal); sdList.add(lastVal);
   * } ServiceData newVal = new ServiceData(visits, price, validFrom);
   * sdList.add(newVal); this.pricing.put(rating, sdList); }
   * 
   * private ServiceData querySDByDate(Rating rating, Date givenDate) { if
   * (pricing == null) throw new RuntimeException("Price is not yet defined for
   * the service " + this.name); List<ServiceData> sdList =
   * this.pricing.get(rating); if (sdList == null) throw new
   * RuntimeException("Price is not yet defined for the service " + this.name +
   * " and rating " + rating); ServiceData data = null; for (ServiceData sd :
   * sdList) { if (sd.isInRange(givenDate)) { data = sd; break; } } if (data ==
   * null) throw new RuntimeException("Price is not available for the given
   * date"); return data; }
   * 
   * private Date adjust(Date validFrom) { Calendar cal =
   * Calendar.getInstance(TimeZone.getTimeZone("UTC")); cal.setTime(validFrom);
   * cal.set(Calendar.HOUR, 00); cal.set(Calendar.MINUTE, 00);
   * cal.set(Calendar.SECOND, 00); cal.set(Calendar.MILLISECOND, 000);
   * cal.set(Calendar.AM_PM, Calendar.AM); return cal.getTime(); }
   * 
   * private Date dayBefore(Date validFrom) { Calendar cal =
   * Calendar.getInstance(TimeZone.getTimeZone("UTC")); cal.setTime(validFrom);
   * cal.add(Calendar.DAY_OF_MONTH, -1); cal.set(Calendar.HOUR_OF_DAY, 23);
   * cal.set(Calendar.MINUTE, 59); cal.set(Calendar.SECOND, 59);
   * cal.set(Calendar.MILLISECOND, 999); return cal.getTime(); }
   **/
}
