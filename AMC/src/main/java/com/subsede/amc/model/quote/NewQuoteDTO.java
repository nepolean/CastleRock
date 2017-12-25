package com.subsede.amc.model.quote;

import java.util.List;

public class NewQuoteDTO implements java.io.Serializable {

  /**
   * Default serial version UID
   */
  private static final long serialVersionUID = 1L;
  private String userId;
  private String assetId;
  private List<String> packages;
  //private String[] serviceID;

  public NewQuoteDTO() {

  }

  public NewQuoteDTO(String userId, String assetId) {
    super();
    this.userId = userId;
    this.assetId = assetId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getAssetId() {
    return assetId;
  }

  public void setAssetId(String assetId) {
    this.assetId = assetId;
  }

  public List<String> getPackageID() {
    return packages;
  }

  public void setPackageID(List<String> packages) {
    this.packages = packages;
  }

/*  public String[] getServiceID() {
    return serviceID;
  }

  public void setServiceID(String[] serviceID) {
    this.serviceID = serviceID;
  }*/

  @Override
  public String toString() {
    return "NewQuoteDTO [mobileNo=" + userId + ", assetId=" + assetId + "]";
  }

}
