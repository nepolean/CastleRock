package com.real.proj.amc.model.quote;

public class NewQuoteInput implements java.io.Serializable {

  /**
   * Default serial version UID
   */
  private static final long serialVersionUID = 1L;
  private String mobileNo;
  private String assetId;
  private String[] productID;

  public NewQuoteInput() {

  }

  public NewQuoteInput(String mobileNo, String assetId) {
    super();
    this.mobileNo = mobileNo;
    this.assetId = assetId;
  }

  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public String getAssetId() {
    return assetId;
  }

  public void setAssetId(String assetId) {
    this.assetId = assetId;
  }

  @Override
  public String toString() {
    return "NewQuoteInput [mobileNo=" + mobileNo + ", assetId=" + assetId + "]";
  }

  public String[] getProductID() {
    return productID;
  }

  public void setProductID(String[] productID) {
    this.productID = productID;
  }

}
