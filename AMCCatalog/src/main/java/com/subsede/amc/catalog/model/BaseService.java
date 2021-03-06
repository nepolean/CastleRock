package com.subsede.amc.catalog.model;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.subsede.amc.catalog.model.asset.AssetBasedService;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AssetBasedService.class, name = "AssetService"),
    @JsonSubTypes.Type(value = GeneralService.class, name = "GenericService")
})
public class BaseService extends BaseMasterEntity implements Service {
  
  private static Logger logger = LoggerFactory.getLogger(BaseService.class);

  protected String type = "SERVICE";
  
  protected Category category;
  
  @NotNull
  @NotBlank(message = "Service name should not be empty.") 
  protected String name;
  
  @NotNull
  @NotBlank(message = "Description should not be empty.")
  protected String description;
  
  @NotNull(message = "Skills should not be null.")
  List<String> skills;
  
  @DBRef (lazy = false)
  Tax tax;

  private boolean canSubscribe;

  private boolean canRequestOneTime;

  protected Map<DeliveryMethod, TimeLine<ServiceMetadata>> detailsTracker;

  public BaseService() {

  }

  public BaseService(
      Category category,
      String name,
      String description,
      List<String> serviceType) {
    this.category = category;
    this.name = name;
    this.description = description;
    this.skills = serviceType;
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
    logger.info("Service Name -> {}", name);
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

  public void setCategory(String category) {
    this.category = Category.valueOf(category);
  }

  public List<String> getSkills() {
    return skills;
  }

  public void setSkills(List<String> serviceType) {
    this.skills = serviceType;
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

  protected void rejectIfDataIsNotValid(
      DeliveryMethod delivery,
      ServiceMetadata sld) { }

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
      return (OneTimeMetadata) this.doGetCurrentSubscriptionData(DeliveryMethod.TRANSACTIONAL);
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

  protected UserInput<String, Object> getDefaultInput() { return null;}

  @Override
  public String getType() {
    return type;
  }

  public boolean canSubscribe() {
    return this.canSubscribe;
  }

  public boolean canRequestOneTime() {
    return this.canRequestOneTime;
  }

  public boolean getCanSubscribe() {
    return canSubscribe;
  }

  public boolean getCanRequestOneTime() {
    return this.canRequestOneTime;
  }
  
  public void setTax(Tax tax) {
    this.tax = tax;
  }
  
  @Override
  public Tax getTax() {
    return this.tax;
  }

  public static void main(String[] args) {
    ServiceType[] sTypes = Category.ASSET.getServiceTypes();
    for (ServiceType type : sTypes)
      System.out.println(type);
  }
}
