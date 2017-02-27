package com.real.proj.amc.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Amenities")
public class Amenity {

  @Id
  String id;
  String name;

  public Amenity(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
