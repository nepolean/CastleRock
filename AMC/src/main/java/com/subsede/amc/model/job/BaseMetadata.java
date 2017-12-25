package com.subsede.amc.model.job;

public class BaseMetadata {
  
  String uniqueId;
  String source;

  public BaseMetadata() {
  }
  public BaseMetadata(String source, String uniqueId) {
    this.source = source;
    this.uniqueId = uniqueId;
  }
  
  public void setSource(String source) {
    this.source = source;
  }

  public String getSource() {
    return this.source;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public String getUniqueId() {
    return this.uniqueId;
  }

}
