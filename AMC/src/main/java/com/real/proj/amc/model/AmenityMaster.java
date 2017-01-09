package com.real.proj.amc.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AmenityMaster {
  
  @Id
  String id;
  String name;
  
  public AmenityMaster(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
