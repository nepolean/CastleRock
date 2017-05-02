package com.real.proj.amc.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Services")
public class AssetBasedService extends BaseService {

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

  public AssetBasedService(String category, String name, String description, List<AssetType> applicableTo,
      List<String> amenities, DeliveryMethod delivery) {
    super(category, name, description);
    this.applicableTo = applicableTo;
    this.amenities = amenities;
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

  protected void rejectIfDataIsNotValid(DeliveryMethod delivery, ServiceMetadata sld) {
    if (Objects.isNull(sld) || !(sld instanceof RatingBasedMetadata)) {
      if (logger.isErrorEnabled())
        logger.error("wrong data type {}", sld);
      throw new IllegalArgumentException("Invalid service details have been passed.");
    }
    List<String> errorHolder = new LinkedList<String>();
    if (!sld.isValid(delivery, errorHolder)) {
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

}
