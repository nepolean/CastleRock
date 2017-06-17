package com.real.proj.amc.model.quote;

public class NewQuoteInput implements java.io.Serializable {

  /**
   * Default serial version UID
   */
  private static final long serialVersionUID = 1L;
  private String userId;
  private String assetId;
  private String[] productID;

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

  @Override
  public String toString() {
    return "NewQuoteInput [mobileNo=" + userId + ", assetId=" + assetId + "]";
  }

  public String[] getProductID() {
    return productID;
  }

  public void setProductID(String[] productID) {
    this.productID = productID;
  }

}
