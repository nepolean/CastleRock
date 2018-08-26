package com.subsede.amc.catalog.model;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

public abstract class BasePackages extends BaseMasterEntity implements Product {

  @Transient
  private final static Logger logger = LoggerFactory.getLogger(BasePackages.class);

  @Indexed
  protected String type = null;

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
    return type;
  }

  
  public Category getCategory() {
    return this.category;
  }

  public void addServices(List<Service> services) {
    if (logger.isDebugEnabled())
      logger.debug("Add services to package");
    services = Objects.requireNonNull(services, "No services data provided.");
    StringBuilder failedToAdd = new StringBuilder();
    services.forEach(service -> {
      try {
        this.addService(service);
      } catch (IllegalArgumentException ex) {
        failedToAdd.append(service.getName() + ", reason:" + ex.getMessage() + "\n");
      } catch (NullPointerException ex) {
        logger.warn("Ignoring null service");
      }
    });
    failedToAdd.trimToSize();
    if (failedToAdd.length() > 0)
      throw new IllegalArgumentException("The following services have failed to add. \n" + failedToAdd.toString());
  }

  public void setServices(Set<Service> services) {
    this.services = services;
  }

  public Set<Service> getServices() {
    return this.services;
  }

  /**
   * public List<String> getServiceInfo() {
   * if (this.serviceInfo == null)
   * return null;
   * List<String> ids = new ArrayList<String>(this.serviceInfo.size());
   * for (ServiceInfo svc : this.serviceInfo)
   * ids.add(svc.getServiceId());
   * return ids;
   * }
   * 
   * public void setServiceInfo(List<ServiceInfo> serviceInfo) {
   * this.serviceInfo = serviceInfo;
   * }
   */
  public void addService(Service service) {
    if (logger.isInfoEnabled())
      logger.info("Adding a service to the package {}", service, name);
    validate(service);
    if (this.services == null)
      this.services = new HashSet<Service>();
    this.services.add(service);
  }
  
  protected abstract boolean validate(Service service);

}