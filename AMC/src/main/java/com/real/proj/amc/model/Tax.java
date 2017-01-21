package com.real.proj.amc.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Tax extends BaseMasterEntity {

  @Id
  private String id;
  private String name;
  private double percentage;

  public Tax(String name, double percentage) {
    this.name = name;
    this.percentage = percentage;
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
    return "Tax [id=" + id + ", name=" + name + ", percentage=" + percentage + "]";
  }

}
