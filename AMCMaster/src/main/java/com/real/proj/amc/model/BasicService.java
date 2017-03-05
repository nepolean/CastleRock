package com.real.proj.amc.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Services")
public abstract class BasicService extends BaseMasterEntity {

  @Id
  private String id;
  @NotNull
  String category;
  @NotBlank
  private String name;
  @NotBlank
  private String description;
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

  PricingStrategy pricingStrategy;

  public BasicService(String category, String name, String description, List<AssetType> applicableTo,
      List<String> amenities) {
    this.category = category;
    this.name = name;
    this.description = description;
    this.applicableTo = applicableTo;
    this.amenities = amenities;
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

  public double getPrice(UserInput<String, Object> input) {
    return this.pricingStrategy.getPrice(input);
  }

  public String getCategory() {
    return this.category;
  }

  public void setCategory(String category) {
    this.category = category;
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

  public PricingStrategy getPricingStrategy() {
    return pricingStrategy;
  }

  public void setPricingStrategy(PricingStrategy pricingStrategy) {
    this.pricingStrategy = pricingStrategy;
  }

  public void setId(String id) {
    this.id = id;
  }

}
