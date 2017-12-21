package com.subsede.amc.model.job;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.subsede.amc.catalog.model.BaseMasterEntity;
import com.subsede.amc.model.Location;

@Document(collection = "Agencies")
public class Agency extends BaseMasterEntity {

  @Id
  private String id;
  @NotBlank(message = "First name should not be empty")
  @Size(max = 31, message = "First name can have upto 31 characters")
  @Pattern(regexp = ValidPatterns.PATTERN_WITHOUT_SPECIAL_CHARACTERS_AND_DIGITS, message = "Agency name should not contain special characters")
  private String name;
  @NotBlank(message = "Address should not be empty")
  private Location address;

  public Agency(String name, Location address) {
    this.name = name;
    this.address = address;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Location getAddress() {
    return address;
  }

  public void setAddress(Location address) {
    this.address = address;
  }

  @Override
  public String getId() {
    return this.id;
  }
}
