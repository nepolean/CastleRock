package com.real.proj.amc.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Packages")
public class AMCPackage extends BaseMasterEntity {

  @Id
  private String id;
  @NotBlank
  private String name;
  @NotBlank
  private String description;
  @NotNull
  private List<ServiceInfo> services;

  private long tenure; // in terms of no. of quarters

  private double discountPct;

  public AMCPackage(String name, String description, List<SubscriptionService> services) {
    this.name = name;
    this.description = description;
    convertAndAdd(services);
    isActive = false;
  }

  private void convertAndAdd(List<SubscriptionService> services) {
    services.forEach(service -> {
      ServiceInfo info = new ServiceInfo(service);
      this.services.add(info);
    });
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

  public List<ServiceInfo> getServices() {
    return services;
  }

  public void setServices(List<ServiceInfo> services) {
    this.services = services;
  }

  public void addService(SubscriptionService service) {
    if (this.services == null)
      this.services = new ArrayList<ServiceInfo>();
    this.services.add(new ServiceInfo(service));
  }

  public PackagePriceInfo getActualPrice(List<SubscriptionService> services, UserInput<String, Object> input,
      PackageScheme scheme) {
    // this is sigma of all services packaged hereunder.
    double actualPrice = getActualPriceFor(services, input, scheme);
    double discount = actualPrice * discountPct / 100;
    return new PackagePriceInfo(scheme, actualPrice, discount);
  }

  private double getActualPriceFor(List<SubscriptionService> services, UserInput<String, Object> input,
      PackageScheme scheme) {
    if (this.services == null)
      throw new IllegalStateException("The package is not built ready");
    double actualPrice = 0.0;
    for (SubscriptionService service : services) {
      double unitPrice = service.getPrice(input);
      ServiceLevelData sld = service.getServiceLevelData(scheme);
      int visitCount = sld.getVisits();
      actualPrice += unitPrice * tenure * visitCount;
    }
    return actualPrice;
  }

  public Set<PackagePriceInfo> getActualPriceForAllSchemes(List<SubscriptionService> services,
      UserInput<String, Object> input) {
    Set<PackagePriceInfo> variants = new HashSet<PackagePriceInfo>();
    PackageScheme[] schemes = PackageScheme.values();
    for (int i = 0; i < schemes.length; i++)
      variants.add(this.getActualPrice(services, input, schemes[i]));
    return variants;
  }

  static class ServiceInfo {
    private String serviceId;
    private String name;
    private String description;

    public ServiceInfo(BasicService service) {
      this.serviceId = service.getId();
      this.name = service.getName();
      this.description = service.getDescription();
    }

    public String getServiceId() {
      return serviceId;
    }

    public void setServiceId(String serviceId) {
      this.serviceId = serviceId;
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

  }
}
