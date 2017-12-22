package com.subsede.amc.model.job;

import java.util.List;

import com.subsede.amc.catalog.model.ServiceType;
import com.subsede.user.model.user.User;

public class LegalJob extends AbstractJob {

  private List<String> documents;

  public LegalJob(String name, List<String> documents, ServiceType serviceType, String sourceType, String sourceId,
      User customer) {
    super(name, serviceType, sourceType, sourceId, customer);
    this.documents = documents;
  }

  public List<String> getDocuments() {
    return documents;
  }

  public void setDocuments(List<String> documents) {
    this.documents = documents;
  }

}
