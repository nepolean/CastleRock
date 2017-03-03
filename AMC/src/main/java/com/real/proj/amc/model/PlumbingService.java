package com.real.proj.amc.model;

import java.util.Date;
import java.util.Set;

public class PlumbingService extends BasicService {

  private TimeLine<ServiceLevelData> sla;

  public void setServiceLevelData(ServiceLevelData data) {
    sla.addToHistory(data, new Date());
  }

  public void updateServiceLevelData(ServiceLevelData data, Date validFrom) {
    sla.addToHistory(data, validFrom);
  }

  public ServiceLevelData getServiceLevelData() {
    return sla.getCurrentValue();
  }

  public boolean isSetServiceLevelData() {
    return sla.size() == 0;
  }

  public Set<ServiceLevelData> getSerivceLevelData() {
    return sla.asList();
  }

  // List<WaterTank> tank;
  // List<SewageLine> lines;

}
