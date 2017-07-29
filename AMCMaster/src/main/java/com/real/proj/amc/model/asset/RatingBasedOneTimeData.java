package com.real.proj.amc.model.asset;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.real.proj.amc.model.OneTimeData;
import com.real.proj.amc.model.OneTimeMetadata;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.UserInput;

public class RatingBasedOneTimeData extends OneTimeMetadata implements Serializable {

  private static final String USER_INPUT = "RATING";
  private static final Logger logger = LoggerFactory.getLogger(RatingBasedSubscriptionData.class);

  /* holds pricing & other service specific details */
  Map<Rating, OneTimeData> oneTimeData;

  public RatingBasedOneTimeData() {

  }

  public RatingBasedOneTimeData(Map<Rating, OneTimeData> data) {
    data = Objects.requireNonNull(data, "The data cannot be null");
    this.oneTimeData = data;
  }

  @Override
  public boolean isValid(List<String> errors) {
    if (errors == null)
      errors = new LinkedList<String>();
    errors.clear();
    for (Rating rating : Rating.values()) {
      OneTimeData sld = this.oneTimeData.get(rating);
      if (sld == null) {
        errors.add("ServiceLevelData for rating," + rating + ", is empty.");
        continue;
      }
    }
    return errors.size() == 0;
  }

  @Override
  public OneTimeData getOneTimeData(UserInput<String, Object> input) {
    if (this.oneTimeData == null)
      throw new IllegalStateException("The requested details are not available at the moment.");
    Rating rating = Rating.FIVE; // default rating
    if (Objects.isNull(input) || Objects.isNull(input.get(USER_INPUT))) {
      logger.error("Invalid argument passed. Expected a valid RATING; Assuming default");
    } else {
      rating = (Rating) input.get(USER_INPUT);
    }
    OneTimeData sd = this.oneTimeData.get(rating);
    return sd;
  }

  public void updateSubscriptionMetadata(Rating rating, OneTimeData data) {
    data = Objects.requireNonNull(data, "The data cannot be null");
    rating = Objects.requireNonNull(rating, "The rating cannot be null");
    this.oneTimeData.put(rating, data);
  }

  public Map<Rating, OneTimeData> getOneTimeData() {
    return this.oneTimeData;
  }

  public void setOneTimeData(Map<Rating, OneTimeData> oneTimeData) {
    this.oneTimeData = oneTimeData;
  }

}
