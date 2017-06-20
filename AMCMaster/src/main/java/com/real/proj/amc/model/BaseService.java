package com.real.proj.amc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

public abstract class BaseService extends BaseMasterEntity implements Service, Product, Serializable {

  private static final String TYPE = "SERVICE";
  private static Logger logger = LoggerFactory.getLogger(BaseService.class);
  @Id
  private String id;
  @NotNull
  protected Category category;
  @NotBlank
  protected String name;
  @NotBlank
  protected String description;

  private boolean canSubscribe;

  private boolean canRequestOneTime;

  protected Map<DeliveryMethod, TimeLine<ServiceMetadata>> detailsTracker;

  BaseService() {

  }

  public BaseService(Category category, String name, String description) {
    this.category = category;
    this.name = name;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public Category getCategory() {
    return this.category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public void setSubscriptionData(ServiceMetadata subcriptionData) {
    rejectIfDataIsNotValid(DeliveryMethod.SUBSCRIPTION, subcriptionData);
    addToTracker(DeliveryMethod.SUBSCRIPTION, subcriptionData, new Date());
    this.canSubscribe = true;
  }

  public void setOneTimeData(ServiceMetadata oneTimeData) {
    rejectIfDataIsNotValid(DeliveryMethod.TRANSACTIONAL, oneTimeData);
    addToTracker(DeliveryMethod.TRANSACTIONAL, oneTimeData, new Date());
    this.canRequestOneTime = true;
  }

  public void unsetSubscriptionServiceData() {
    this.detailsTracker.remove(DeliveryMethod.SUBSCRIPTION);
    this.canSubscribe = false;
  }

  public void unsetOneTimeServiceData() {
    this.detailsTracker.remove(DeliveryMethod.TRANSACTIONAL);
    this.canRequestOneTime = false;
  }

  public void updateSubscriptionData(ServiceMetadata subscriptionData, Date applicableFrom) {
    rejectIfDataIsNotValid(DeliveryMethod.SUBSCRIPTION, subscriptionData);
    addToTracker(DeliveryMethod.SUBSCRIPTION, subscriptionData, applicableFrom);
  }

  public void updateOneTimeData(ServiceMetadata oneTimeData, Date applicableFrom) {
    rejectIfDataIsNotValid(DeliveryMethod.TRANSACTIONAL, oneTimeData);
    addToTracker(DeliveryMethod.TRANSACTIONAL, oneTimeData, applicableFrom);

  }

  protected abstract void rejectIfDataIsNotValid(
      DeliveryMethod delivery,
      ServiceMetadata sld);

  private synchronized void addToTracker(DeliveryMethod dm, ServiceMetadata sld, Date applicableFrom) {
    if (this.detailsTracker == null)
      this.detailsTracker = new HashMap<DeliveryMethod, TimeLine<ServiceMetadata>>();
    TimeLine<ServiceMetadata> sd = this.detailsTracker.get(dm);
    if (sd == null)
      sd = new TimeLine<ServiceMetadata>();
    sd.addToHistory(sld, applicableFrom);
    this.detailsTracker.put(dm, sd);
  }

  public SubscriptionData fetchSubscriptionData(UserInput<String, Object> input) {
    if (logger.isDebugEnabled())
      logger.debug("subscription data requested with input {}", input);
    UserInput<String, Object> userInput = Objects.requireNonNull(input,
        "No user input.");
    SubscriptionMetadata subsData = (SubscriptionMetadata) doGetCurrentSubscriptionData(DeliveryMethod.SUBSCRIPTION);
    return subsData.getSubscriptionData(userInput);
  }

  private ServiceMetadata doGetCurrentSubscriptionData(DeliveryMethod deliveryMethod) {
    if (this.detailsTracker == null) {
      logger.error("The service {} is not ready", this.getName());
      throw new IllegalStateException("ServiceData has not been defined yet for this service");
    }
    TimeLine<ServiceMetadata> timeline = this.detailsTracker.get(deliveryMethod);
    if (timeline == null) {
      logger.error("Subscription Data is null");
      throw new IllegalStateException("Subscription data is not set for this service");
    }
    ServiceMetadata svcMetadata = timeline.getCurrentValue();
    if (svcMetadata == null) {
      logger.error("The subscription data is null");
      throw new IllegalStateException("Subscription data is not set for this service");
    }
    return svcMetadata;
  }

  public SubscriptionData fetchSubscriptionData() {
    UserInput<String, Object> input = getDefaultInput();
    try {
      return this.fetchSubscriptionData(input);
    } catch (Exception ex) {
    }
    return null;
  }

  public SubscriptionMetadata getSubscriptionServiceData() {
    try {
      return (SubscriptionMetadata) this.doGetCurrentSubscriptionData(DeliveryMethod.SUBSCRIPTION);
    } catch (Exception ex) {
    }
    return null;
  }

  public OneTimeMetadata getOneTimeServiceData() {
    try {
      return (RatingBasedOneTimeMetadata) this.doGetCurrentSubscriptionData(DeliveryMethod.TRANSACTIONAL);
    } catch (Exception ex) {

    }
    return null;
  }

  public OneTimeData fetchOneTimeData(UserInput<String, Object> input) {
    if (logger.isDebugEnabled())
      logger.debug("OneTime data requested with input {}", input);
    UserInput<String, Object> userInput = Objects.requireNonNull(input,
        "No user input.");
    OneTimeMetadata oneTimeMetadata = (OneTimeMetadata) this.doGetCurrentSubscriptionData(DeliveryMethod.TRANSACTIONAL);
    return oneTimeMetadata.getOneTimeData(userInput);
  }

  public OneTimeData fetchOneTimeData() {
    UserInput<String, Object> input = getDefaultInput();
    try {
      return this.fetchOneTimeData(input);
    } catch (Exception ex) {
    }
    return null;
  }

  protected abstract UserInput<String, Object> getDefaultInput();

  @Override
  public String getType() {
    return TYPE;
  }

  @Override
  public boolean canSubscribe() {
    return this.canSubscribe;
  }

  @Override
  public boolean canRequestOneTime() {
    return this.canRequestOneTime;
  }

  public boolean getCanSubscribe() {
    return canSubscribe;
  }

  public boolean getCanRequestOneTime() {
    return this.canRequestOneTime;
  }
}
