package com.real.proj.amc.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RatingBasedMetadata extends ServiceMetadata {

  private static final String USER_INPUT = "RATING";
  private static final Logger logger = LoggerFactory.getLogger(RatingBasedMetadata.class);

  /* holds pricing & other service specific details */
  Map<Rating, ServiceData> data;

  public RatingBasedMetadata(Map<Rating, ServiceData> data) {
    if (data == null)
      throw new IllegalArgumentException("ServiceLevelData cannot be null.");
    this.data = data;
  }

  @Override
  public boolean isValid(DeliveryMethod deliveryMethod, List<String> errors) {
    if (errors == null)
      errors = new LinkedList<String>();
    errors.clear();
    for (Rating rating : Rating.values()) {
      ServiceData sld = data.get(rating);
      if (sld == null) {
        errors.add("ServiceLevelData for rating," + rating + ", is empty.");
        continue;
      }
      if (deliveryMethod != DeliveryMethod.TRANSACTIONAL && !(sld instanceof SubscriptionData)) {
        errors.add(String.format("Wrong service data type is passed. Expected {}", SubscriptionData.class.getName()));
      }

    }
    return errors.size() > 0;
  }

  public ServiceData getServiceData(UserInput<String, Object> input) {
    if (this.data == null)
      throw new IllegalStateException("The requested details are not available at the moment.");
    Rating rating = Rating.FIVE; // default rating
    if (Objects.isNull(input) || Objects.isNull(input.get(USER_INPUT))) {
      logger.error("Invalid argument passed. Expected a valid RATING; Assuming default");
    } else {
      rating = (Rating) input.get(USER_INPUT);
    }
    ServiceData sd = this.data.get(rating);
    return sd;
  }

}
