package com.real.proj.amc.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubscriptionBasedService extends BasicService {

  private Map<PackageScheme, ServiceLevelData> sla;

  public SubscriptionBasedService(String name, String description, List<AssetType> applicableTo,
      List<Amenity> amenities, PricingStrategy pricingStrategy) {
    super(name, description, applicableTo, amenities, pricingStrategy);
    sla = new HashMap<PackageScheme, ServiceLevelData>();
  }

  public void setServiceLevelData(ServiceLevelData data) {
    if (data == null)
      throw new IllegalArgumentException("Null data passed for name and/or data");
    if (data.validate())
      throw new IllegalArgumentException("Invalid service data is passed");
    this.sla.put(data.getName(), data);
  }

  public ServiceLevelData getServiceLevelData(PackageScheme scheme) {
    if (sla.get(scheme) == null)
      throw new IllegalArgumentException(String.format("Given scheme %s does not exist", scheme));
    return sla.get(scheme);
  }

  public boolean isSetServiceLevelData() {
    return sla.size() == PackageScheme.values().length;
  }

  public Set<PackageScheme> getApplicableSchemes() {
    return sla.keySet();
  }

}
