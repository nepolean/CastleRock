package com.real.proj.amc.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;

public abstract class BaseService extends BaseMasterEntity {

  @Id
  private String id;
  @NotNull
  protected String category;
  @NotBlank
  protected String name;
  @NotBlank
  protected String description;
  protected Map<DeliveryMethod, TimeLine<ServiceMetadata>> detailsTracker;
  // protected PricingStrategy pricingStrategy;
  // protected DeliveryMethod deliveryMethod;

  public BaseService(String category, String name, String description) {
    this.category = category;
    this.name = name;
    this.description = description;
    // this.pricingStrategy = pricingStrategy;
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

  public String getCategory() {
    return this.category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  /*
   * public PricingStrategy getPricingStrategy() { return pricingStrategy; }
   * 
   * public void setPricingStrategy(PricingStrategy pricingStrategy) { if
   * (pricingStrategy == null) throw new
   * IllegalArgumentException("Null value passed in for price");
   * this.pricingStrategy = pricingStrategy; }
   */

  public void setId(String id) {
    this.id = id;
  }

  public void setServiceDetails(DeliveryMethod delivery, ServiceMetadata sld) {
    rejectIfDataIsNotValid(delivery, sld);
    addToTracker(delivery, sld, new Date());
  }

  public void updateServiceDetails(DeliveryMethod delivery, ServiceMetadata sld, Date applicableFrom) {
    rejectIfDataIsNotValid(delivery, sld);
    addToTracker(delivery, sld, applicableFrom);
  }

  public boolean doesSupportDeliveryMethod(DeliveryMethod dm) {
    if (this.detailsTracker == null)
      return false;
    return this.detailsTracker.containsKey(dm);
  }

  protected abstract void rejectIfDataIsNotValid(DeliveryMethod delivery, ServiceMetadata sld);

  synchronized void addToTracker(DeliveryMethod dm, ServiceMetadata sld, Date applicableFrom) {
    if (this.detailsTracker == null)
      this.detailsTracker = new HashMap<DeliveryMethod, TimeLine<ServiceMetadata>>();
    TimeLine<ServiceMetadata> sd = this.detailsTracker.get(dm);
    if (sd == null)
      sd = new TimeLine<ServiceMetadata>();
    sd.addToHistory(sld, applicableFrom);
    this.detailsTracker.put(dm, sd);
  }

  public ServiceData getServiceData(DeliveryMethod dm, UserInput<String, Object> input) {
    if (this.detailsTracker == null)
      throw new IllegalStateException("ServiceData has not been defined yet for this service");
    UserInput<String, Object> userInput = Objects.requireNonNull(input, "Invalid user input.");
    TimeLine<ServiceMetadata> timeline = this.detailsTracker.get(dm);
    ServiceMetadata sd = timeline.getCurrentValue();
    ServiceData data = sd.getServiceData(userInput);
    return data;
  }

}
