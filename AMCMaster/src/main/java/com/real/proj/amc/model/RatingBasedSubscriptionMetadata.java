package com.real.proj.amc.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RatingBasedSubscriptionMetadata extends SubscriptionMetadata implements Serializable {

  private static final Logger logger = LoggerFactory.getLogger(RatingBasedSubscriptionMetadata.class);

  /* holds pricing & other service specific details */
  Map<Rating, SubscriptionData> subscriptionData;

  RatingBasedSubscriptionMetadata() {

  }

  public RatingBasedSubscriptionMetadata(Map<Rating, SubscriptionData> data) {
    data = Objects.requireNonNull(data, "SubscriptionData cannot be null");
    this.subscriptionData = data;
  }

  public void updateSubscriptionMetadata(Rating rating, SubscriptionData data) {
    rating = Objects.requireNonNull(rating, "Rating cannot be null");
    data = Objects.requireNonNull(data, "SubscriptionData cannot be null");
    this.subscriptionData.put(rating, data);
  }

  @Override
  public boolean isValid(List<String> errors) {
    if (errors == null)
      errors = new LinkedList<String>();
    errors.clear();
    for (Rating rating : Rating.values()) {
      SubscriptionData sld = this.subscriptionData.get(rating);
      if (sld == null) {
        errors.add("ServiceLevelData for rating," + rating + ", is empty.");
        continue;
      }
    }
    return errors.size() == 0;
  }

  @Override
  public SubscriptionData getSubscriptionData(UserInput<String, Object> input) {
    if (this.subscriptionData == null)
      throw new IllegalStateException("The requested details are not available at the moment.");
    Rating rating = Rating.FIVE; // default rating
    if (Objects.isNull(input) || Objects.isNull(input.get(Rating.getKey()))) {
      logger.error("Invalid argument passed. Expected a valid RATING; Assuming default");
    } else {
      logger.info("User Input {} ", input);
      rating = (Rating) input.get(Rating.getKey());
    }
    SubscriptionData sd = this.subscriptionData.get(rating);
    return sd;
  }

  public Map<Rating, SubscriptionData> getSubscriptionData() {
    return this.subscriptionData;
  }

  public void setSubscriptionData(Map<Rating, SubscriptionData> subscriptionData) {
    this.subscriptionData = subscriptionData;
  }
}
