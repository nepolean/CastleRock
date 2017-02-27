package com.real.proj.amc.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Categories")
public class Category extends BaseMasterEntity {
  private String name;
  private String description;

  public Category(String name, String description) {
    this.name = name;
    this.description = description;
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

  @Override
  public String toString() {
    return "Category [name=" + name + ", description=" + description + "]";
  }

}
