// There is a major change introduced today (30-APR-2017). Basically, got rid of PackageScheme.
package com.real.proj.amc.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Packages")
public class AMCPackage extends BaseMasterEntity implements Product {

  @Transient
  private final static Logger logger = LoggerFactory.getLogger(AMCPackage.class);

  public static final String TYPE = "PACKAGE";

  @Id
  private String id;
  @NotBlank
  private String name;
  @NotBlank
  private String description;
  // @NotNull
  // private List<ServiceInfo> serviceInfo;
  @Reference
  @DBRef
  private List<Product> services;

  private TenureBasedDiscount tenureBasedDisc;

  // private DeliveryMethod dm;

  private Category category;

  public AMCPackage(Category category, String name, String description) {
    this.category = category;
    this.name = name;
    this.description = description;
    // this.dm = DeliveryMethod.SUBSCRIPTION;
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

  public void addServices(List<Service> services) {
    if (logger.isDebugEnabled())
      logger.info("Add services to package");
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

  public List<Product> getServices() {
    return services;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public void addService(Product service) {
    if (logger.isInfoEnabled())
      logger.info("Adding new service to the package");
    service = validate(service);
    if (this.services == null)
      this.services = new LinkedList<Product>();
    this.services.add(service);
  }

  private Product validate(Product service) {
    service = Objects.requireNonNull(service, "Null value passed for service");
    if (!service.canSubscribe()) {
      String msg = String.format("The service, {}, does not support subscription model ", service.getName());
      if (logger.isErrorEnabled())
        logger.error(msg);
      throw new IllegalArgumentException(msg);
    }
    if (!service.getCategory().equals(this.category)) {
      String msg = String.format("The category does not match. Expected category {}, provided {}", this.category,
          service.getCategory());
      if (logger.isErrorEnabled())
        logger.error(msg);
      throw new IllegalArgumentException(msg);
    }
    return service;
  }

  public SubscriptionData getActualPrice(UserInput<String, Object> input) {
    logger.info("getActualPrice -> {}", input);
    // this is sigma of all services defined under this package.
    Iterable<Product> services = this.loadServiceData();
    if (logger.isDebugEnabled())
      logger.debug("Loaded service details from db");
    return getActualPriceFor(services, input);
  }

  private Iterable<Product> loadServiceData() {
    // Iterable<BaseService> services =
    // this.repository.findAll(this.getServiceInfo());
    return services;
  }

  private SubscriptionData getActualPriceFor(Iterable<Product> services, UserInput<String, Object> input) {
    // PackageScheme scheme) {
    if (this.services == null) {
      if (logger.isErrorEnabled())
        logger.error("service info is null");
      throw new IllegalStateException("The package is not built ready");
    }
    double actualPrice = 0.0;
    int visitCount = 0;
    for (Product service : services) {
      SubscriptionData subsData = service.fetchSubscriptionData(input);
      if (subsData == null) {
        if (logger.isErrorEnabled())
          logger.error("The service {} is not built ready", service.getName());
        throw new IllegalStateException("This package cannot be used at this time");
      }
      double unitPrice = subsData.getSubscriptionPrice();
      actualPrice += unitPrice;
      visitCount += subsData.getVisitCount();
    }

    return new SubscriptionData(actualPrice, visitCount);
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
   * 
   * 
   * static class ServiceInfo implements Serializable {
   * 
   * /**
   * Default serial uid
   *
   * private static final long serialVersionUID = 1L;
   * private String serviceId;
   * private String name;
   * private String description;
   * 
   * public ServiceInfo() {
   * 
   * }
   * 
   * public ServiceInfo(BaseService service) {
   * this.serviceId = service.getId();
   * this.name = service.getName();
   * this.description = service.getDescription();
   * }
   * 
   * public String getServiceId() {
   * return serviceId;
   * }
   * 
   * public void setServiceId(String serviceId) {
   * this.serviceId = serviceId;
   * }
   * 
   * public String getName() {
   * return name;
   * }
   * 
   * public void setName(String name) {
   * this.name = name;
   * }
   * 
   * public String getDescription() {
   * return description;
   * }
   * 
   * public void setDescription(String description) {
   * this.description = description;
   * }
   * 
   * }
   */

  @Override
  public String getType() {
    return TYPE;
  }

  @Override
  public boolean canSubscribe() {
    return true;
  }

  @Override
  public boolean canRequestOneTime() {
    return false;
  }

  @Override
  public SubscriptionData fetchSubscriptionData() {
    return this.fetchSubscriptionData(null);
  }

  @Override
  public OneTimeData fetchOneTimeData() {
    return null;
  }

  @Override
  public SubscriptionData fetchSubscriptionData(UserInput<String, Object> input) {
    return this.getActualPrice(input);
  }

  @Override
  public OneTimeData fetchOneTimeData(UserInput<String, Object> input) {
    return null;
  }

  public TenureBasedDiscount getTenureBasedDisc() {
    return tenureBasedDisc;
  }

  public void setTenureBasedDisc(TenureBasedDiscount tenureBasedDisc) {
    this.tenureBasedDisc = tenureBasedDisc;
  }

  @Override
  public Category getCategory() {
    return this.getCategory();
  }

}
