package com.real.proj.amc.model.asset;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.Document;

import com.real.proj.amc.model.BaseService;
import com.real.proj.amc.model.Category;
import com.real.proj.amc.model.DeliveryMethod;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.ServiceMetadata;
import com.real.proj.amc.model.ServiceType;
import com.real.proj.amc.model.UserInput;

@Document(collection = "Services")
public class AssetBasedService extends BaseService implements Serializable {

  /**
   * default serialVersionUID
   */
  private static final long serialVersionUID = 1L;

  private static final Logger logger = LoggerFactory.getLogger(AssetBasedService.class);
  // private static final String UOM = "SFT";

  private List<AssetType> applicableTo;
  private List<String> amenities;

  // This is only one kind of pricing which is 'Rating' based.
  // There are multiple options
  // 1 - Whether the price of this service is area based / qty based / any other
  // parameter?
  // 1 - Whether this service is available as AMC / OneTime / Both?
  // 3 - Whether the price of this service is fixed | rating based | any other
  // parameter?

  // Moreover we should extract the pricing details out.

  // Map<Rating, List<ServiceData>> pricing;

  AssetBasedService() {
    super();
    this.setCategory(Category.ASSET);
  }

  public AssetBasedService(
      String name,
      String description,
      List<AssetType> applicableTo,
      List<String> amenities,
      ServiceType serviceType) {
    super(Category.ASSET, name, description, serviceType);
    this.applicableTo = applicableTo;
    this.amenities = amenities;
  }

  public void setName(String name) {
    logger.info("Service Name -> {}", name);
    this.name = name;
  }

  public List<AssetType> getApplicableTo() {
    return applicableTo;
  }

  public void setApplicableTo(List<AssetType> applicableTo) {
    this.applicableTo = applicableTo;
  }

  public List<String> getAmenities() {
    return amenities;
  }

  public void setAmenities(List<String> amenities) {
    this.amenities = amenities;
  }

  protected UserInput<String, Object> getDefaultInput() {
    UserInput<String, Object> input = new UserInput<String, Object>();
    input.add("RATING", Rating.FIVE);
    return input;
  }

  protected void rejectIfDataIsNotValid(DeliveryMethod delivery, ServiceMetadata data) {
    Objects.requireNonNull(data, "Null value passed in for service metadata.");
    if (delivery == DeliveryMethod.SUBSCRIPTION && !(data instanceof RatingBasedSubscriptionData)) {
      if (logger.isErrorEnabled())
        logger.error("wrong data type {}", data);
      throw new IllegalArgumentException("Invalid service details have been passed.");
    } else if (delivery == DeliveryMethod.TRANSACTIONAL && !(data instanceof RatingBasedOneTimeData)) {
      if (logger.isErrorEnabled())
        logger.error("wrong data type {}", data);
      throw new IllegalArgumentException("Invalid service details have been passed.");
    }
    List<String> errorHolder = new LinkedList<String>();
    if (!data.isValid(errorHolder)) {
      String errorString = concat(errorHolder);
      throw new IllegalArgumentException(errorString);
    }
  }

  private String concat(List<String> errorHolder) {
    StringBuilder sb = new StringBuilder();
    for (String str : errorHolder)
      sb.append(str + "\\n");
    return sb.toString();
  }

  @Override
  public String toString() {
    return "AssetBasedService [applicableTo=" + applicableTo + ", amenities=" + amenities + ", category=" + category
        + ", name=" + name + ", description=" + description + "]";
  }

}
