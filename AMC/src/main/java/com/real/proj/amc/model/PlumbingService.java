package com.real.proj.amc.model;

import java.util.Date;
import java.util.Set;

public class PlumbingService extends AssetBasedService {

  private TimeLine<ServiceData> sla;

  public void setServiceLevelData(ServiceData data) {
    sla.addToHistory(data, new Date());
  }

  public void updateServiceLevelData(ServiceData data, Date validFrom) {
    sla.addToHistory(data, validFrom);
  }

  public ServiceData getServiceLevelData() {
    return sla.getCurrentValue();
  }

  public boolean isSetServiceLevelData() {
    return sla.size() == 0;
  }

  public Set<ServiceData> getSerivceLevelData() {
    return sla.asList();
  }

  // List<WaterTank> tank;
  // List<SewageLine> lines;

}
