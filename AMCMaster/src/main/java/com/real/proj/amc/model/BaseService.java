package com.real.proj.amc.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

public abstract class BaseService extends BaseMasterEntity implements Product {

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

  public void setSubscriptionServiceData(ServiceMetadata subcriptionData) {
    rejectIfDataIsNotValid(DeliveryMethod.SUBSCRIPTION, subcriptionData);
    addToTracker(DeliveryMethod.SUBSCRIPTION, subcriptionData, new Date());
    this.canSubscribe = true;
  }

  public void setOneTimeServiceData(ServiceMetadata oneTimeData) {
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

  protected abstract void rejectIfDataIsNotValid(DeliveryMethod delivery,
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

  public SubscriptionData getSubscriptionData(UserInput<String, Object> input) {
    if (logger.isDebugEnabled())
      logger.debug("subscription data requested with input {}", input);
    if (this.detailsTracker == null) {
      logger.error("The service {} is not ready", this.getName());
      throw new IllegalStateException("ServiceData has not been defined yet for this service");
    }
    UserInput<String, Object> userInput = Objects.requireNonNull(input,
        "No user input.");
    TimeLine<ServiceMetadata> timeline = this.detailsTracker.get(DeliveryMethod.SUBSCRIPTION);
    if (timeline == null) {
      logger.error("Subscription Data is null");
      throw new IllegalStateException("Subscription data is not set for this service");
    }
    SubscriptionMetadata subsData = (SubscriptionMetadata) timeline.getCurrentValue();
    if (subsData == null) {
      logger.error("The subscription data is null");
      throw new IllegalStateException("Subscription data is not set for this service");
    }
    return subsData.getSubscriptionData(userInput);
  }

  public SubscriptionData getSubscriptionData() {
    UserInput<String, Object> input = getDefaultInput();
    return this.getSubscriptionData(input);
  }

  public OneTimeData getOneTimeData(UserInput<String, Object> input) {
    if (logger.isDebugEnabled())
      logger.debug("OneTime data requested with input {}", input);
    if (this.detailsTracker == null) {
      logger.error("The service {} is not ready", this.getName());
      throw new IllegalStateException("OneTime data is not set for this service");
    }
    UserInput<String, Object> userInput = Objects.requireNonNull(input,
        "No user input.");
    TimeLine<ServiceMetadata> timeline = this.detailsTracker.get(DeliveryMethod.TRANSACTIONAL);
    if (timeline == null) {
      logger.error("OneTimeData is null");
      throw new IllegalStateException("OneTime data is not set for this service");
    }
    OneTimeMetadata oneTimeMetadata = (OneTimeMetadata) timeline.getCurrentValue();
    if (oneTimeMetadata == null) {
      logger.error("OneTime Metadata is null");
      throw new IllegalStateException("OneTime data is not set for this service");
    }
    return oneTimeMetadata.getOneTimeData(userInput);
  }

  public OneTimeData getOneTimeData() {
    UserInput<String, Object> input = getDefaultInput();
    return this.getOneTimeData(input);
  }

  protected abstract UserInput<String, Object> getDefaultInput();

  @Override
  public String getType() {
    return "SERVICE";
  }

  @Override
  public boolean canSubscribe() {
    return this.canSubscribe;
  }

  @Override
  public boolean canRequestOneTime() {
    return this.canRequestOneTime;
  }
}
