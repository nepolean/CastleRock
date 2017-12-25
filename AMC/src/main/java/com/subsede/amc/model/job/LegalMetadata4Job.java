package com.subsede.amc.model.job;

import java.util.List;

public class LegalMetadata4Job extends BaseMetadata implements ServiceMetadata {

  List<String> documents;

  public LegalMetadata4Job(List<String> documents) {
    this.documents = documents;
  }

  public List<String> getDocuments() {
    return this.documents;
  }

}
