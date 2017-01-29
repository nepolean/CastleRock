package com.real.proj.amc.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Tax extends BaseMasterEntity {

  @Id
  private String id;
  @NotBlank
  private String type;
  @Min(1)
  @Max(100)
  private double percentage;

  public Tax(String name, double percentage) {
    this.type = name;
    this.percentage = percentage;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return type;
  }

  public void setName(String name) {
    this.type = name;
  }

  public double getPercentage() {
    return percentage;
  }

  public void setPercentage(double percentage) {
    this.percentage = percentage;
  }

  public double calcTax(double netAmount) {
    return (netAmount * percentage) / 100;
  }

  @Override
  public String toString() {
    return "Tax [id=" + id + ", name=" + type + ", percentage=" + percentage + "]";
  }

}
