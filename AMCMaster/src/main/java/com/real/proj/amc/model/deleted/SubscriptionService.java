package com.real.proj.amc.model.deleted;

import java.util.List;

import com.real.proj.amc.model.AssetBasedService;
import com.real.proj.amc.model.AssetType;

public class SubscriptionService extends AssetBasedService {

  // private ServiceType type = ServiceType.SUBSCRIPTION;
  // private Map<PackageScheme, ServiceLevelData> sla;

  public SubscriptionService(String category, String name, String description, List<AssetType> applicableTo,
      List<String> amenities) {
    super(category, name, description, applicableTo, amenities);
    // sla = new HashMap<PackageScheme, ServiceLevelData>();
  }

  /*
   * public void setServiceLevelData(Set<ServiceLevelData> sla) { if (sla ==
   * null) throw new
   * IllegalArgumentException("Null data passed for name and/or data"); final
   * Set<ServiceLevelData> rejectedData = new HashSet<ServiceLevelData>();
   * sla.forEach(data -> { try { this.addServiceLevelData(data); } catch
   * (IllegalArgumentException ex) { rejectedData.add(data); } }); if
   * (rejectedData.size() > 0) throw new
   * IllegalArgumentException(String.format("The following data is rejected %s",
   * rejectedData)); }
   */

  /*
   * public ServiceLevelData getServiceLevelData(PackageScheme scheme) { if
   * (sla.get(scheme) == null) throw new
   * IllegalArgumentException(String.format("Given scheme %s does not exist",
   * scheme)); return sla.get(scheme); }
   * 
   * public boolean isSetServiceLevelData() { return sla.size() ==
   * PackageScheme.values().length; }
   * 
   * public Set<PackageScheme> getApplicableSchemes() { return sla.keySet(); }
   * 
   * public void addServiceLevelData(ServiceLevelData data) { if (data == null
   * || !data.validate()) throw new
   * IllegalArgumentException("Invalid service data is passed");
   * this.sla.put(data.getScheme(), data); }
   */
}
