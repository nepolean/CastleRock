package com.subsede.amc.catalog.model;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

public abstract class BasePackages extends BaseMasterEntity {

  @Transient
  private final static Logger logger = LoggerFactory.getLogger(BasePackages.class);

  @Indexed
  protected String TYPE = null;

  @NotNull(message = "Name should not be null")
  @NotBlank(message = "Name should not be empty")
  protected String name;
  @NotNull(message = "Descirption should not be null")
  @NotBlank(message = "Description should not be empty")
  protected String description;
  @NotNull(message = "Category should not be null")
  protected Category category;
  @DBRef
  protected Set<Service> services;

  public BasePackages() {
    super();
  }

  public BasePackages(Category category, String name, String description) {
    this.category = category;
    this.name = name;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public void setCategory(String category) {
    try {
      this.category = Category.valueOf(category);
    } catch (Exception ex) {
      throw new IllegalArgumentException("Invalid category specified.");
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void removeService(Service service) {
    if (logger.isDebugEnabled())
      logger.debug("Removing the service {} from the package {}", service, name);
    if (this.services != null)
      this.services.remove(service);
  }

  public String getType() {
    return TYPE;
  }

  
  public Category getCategory() {
    return this.category;
  }

}