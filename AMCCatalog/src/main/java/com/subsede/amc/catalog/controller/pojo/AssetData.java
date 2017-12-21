package com.subsede.amc.catalog.controller.pojo;

public class AssetData {

  private String name;
  private String description;

  public AssetData(String name, String description) {
    super();
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
    return "AssetData [name=" + name + ", description=" + description + "]";
  }

}
