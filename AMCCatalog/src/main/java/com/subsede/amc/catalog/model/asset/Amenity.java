package com.subsede.amc.catalog.model.asset;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.subsede.amc.catalog.model.BaseMasterEntity;

@Document(collection = "Amenities")
public class Amenity extends BaseMasterEntity {

  @Id
  String id;
  String name;

  public Amenity(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String toString() {
    return "Amenity [id=" + id + ", name=" + name + "]";
  }

}
