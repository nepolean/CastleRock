package com.real.proj.amc.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Tax {
  
  @Id
  private String id;
  private String name;
  private double taxPct;

  public double calcTax(double netAmount) {
    return netAmount * taxPct/100;
  }

}
