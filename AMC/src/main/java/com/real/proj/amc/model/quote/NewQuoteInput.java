package com.real.proj.amc.model.quote;

public class NewQuoteInput implements java.io.Serializable {

  /**
   * Default serial version UID
   */
  private static final long serialVersionUID = 1L;
  private String userId;
  private String assetId;
  private String[] packageID;
  private String[] serviceID;

  public NewQuoteInput() {

  }

  public NewQuoteInput(String userId, String assetId) {
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

  public String[] getPackageID() {
    return packageID;
  }

  public void setPackageID(String[] packageID) {
    this.packageID = packageID;
  }

  public String[] getServiceID() {
    return serviceID;
  }

  public void setServiceID(String[] serviceID) {
    this.serviceID = serviceID;
  }

  @Override
  public String toString() {
    return "NewQuoteInput [mobileNo=" + userId + ", assetId=" + assetId + "]";
  }

}
