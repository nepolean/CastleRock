// There is a major change introduced today (30-APR-2017). Basically, got rid of PackageScheme.
package com.real.proj.amc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.real.proj.amc.repository.ServiceRepository;

@Document(collection = "Packages")
public class AMCPackage extends BaseMasterEntity {

  private final static Logger logger = LoggerFactory.getLogger(AMCPackage.class);

  ServiceRepository repository;

  @Autowired
  public void setServiceRepository(ServiceRepository repository) {
    this.repository = repository;
  }

  @Id
  private String id;
  @NotBlank
  private String name;
  @NotBlank
  private String description;
  @NotNull
  private List<ServiceInfo> serviceInfo;

  private long tenure; // in terms of no. of quarters

  private double discountPct;

  private DeliveryMethod dm;

  public AMCPackage(String name, String description, Long tenure, Double discPct) {
    this.name = name;
    this.description = description;
    this.dm = DeliveryMethod.SUBSCRIPTION;
    this.tenure = tenure;
    this.discountPct = discPct;
    isActive = false;
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

  public void addServices(List<BaseService> services) {
    services = Objects.requireNonNull(services, "No services data provided.");
    services.forEach(service -> {
      this.addService(service);
    });
  }

  public List<ServiceInfo> getServices() {
    return serviceInfo;
  }

  public void setServices(List<ServiceInfo> services) {
    this.serviceInfo = services;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<String> getServiceInfo() {
    if (this.serviceInfo == null)
      return null;
    List<String> ids = new ArrayList<String>(this.serviceInfo.size());
    for (ServiceInfo svc : this.serviceInfo)
      ids.add(svc.getServiceId());
    return ids;
  }

  public void setServiceInfo(List<ServiceInfo> serviceInfo) {
    this.serviceInfo = serviceInfo;
  }

  public long getTenure() {
    return tenure;
  }

  public void setTenure(long tenure) {
    this.tenure = tenure;
  }

  public double getDiscountPct() {
    return discountPct;
  }

  public void setDiscountPct(double discountPct) {
    this.discountPct = discountPct;
  }

  public void addService(BaseService service) {
    service = Objects.requireNonNull(service, "Null value passed for service");
    if (!service.canSubscribe()) {
      String msg = String.format("The service, {}, does not support subscription model ", service.getName());
      logger.error(msg);
      throw new IllegalArgumentException(msg);
    }
    if (this.serviceInfo == null)
      this.serviceInfo = new ArrayList<ServiceInfo>();
    this.serviceInfo.add(new ServiceInfo(service));
  }

  public PackagePriceInfo getActualPrice(UserInput<String, Object> input) {
    logger.info("getActualPrice -> {}", input);
    // this is sigma of all services defined under this package.
    List<BaseService> services = this.loadServiceData();
    double actualPrice = getActualPriceFor(services, input);
    double discount = actualPrice * discountPct / 100;
    return new PackagePriceInfo(actualPrice, discount);
  }

  private List<BaseService> loadServiceData() {
    this.repository.findAll(this.getServiceInfo());
    return null;
  }

  private double getActualPriceFor(List<BaseService> services, UserInput<String, Object> input) {
    // PackageScheme scheme) {
    if (this.serviceInfo == null)
      throw new IllegalStateException("The package is not built ready");
    double actualPrice = 0.0;
    for (BaseService service : services) {
      // boolean askingForBasePrice = (input != null &&
      // input.get(this.getClass().getName()) != null);
      SubscriptionData sld = service.getSubscriptionData(input);
      if (sld == null) {
        if (logger.isErrorEnabled())
          logger.error("The service {} is not built ready", service.getName());
        throw new IllegalStateException("This package cannot be used at this time");
      }
      double unitPrice = sld.getSubscriptionPrice();
      actualPrice += unitPrice * tenure;
    }
    return actualPrice;
  }

  /*
   * public Map<PackageScheme, PackagePriceInfo>
   * getActualPriceForAllSchemes(List<SubscriptionService> services,
   * UserInput<String, Object> input) { Map<PackageScheme, PackagePriceInfo>
   * variants = new HashMap<PackageScheme, PackagePriceInfo>(); PackageScheme[]
   * schemes = PackageScheme.values(); for (int i = 0; i < schemes.length; i++)
   * variants.put(schemes[i], this.getActualPrice(services, input, schemes[i]));
   * return variants; }
   * 
   * public Map<PackageScheme, PackagePriceInfo>
   * getStartingPriceForAllSchemes(List<SubscriptionService> services) {
   * UserInput<String, Object> input = new UserInput<String, Object>();
   * input.add(this.getClass().getName(), Boolean.valueOf(true)); // an
   * indicator // to let the // downstream // methods know // that this // call
   * is made // internally. return this.getActualPriceForAllSchemes(services,
   * null); }
   */

  static class ServiceInfo {
    private String serviceId;
    private String name;
    private String description;

    public ServiceInfo(BaseService service) {
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
