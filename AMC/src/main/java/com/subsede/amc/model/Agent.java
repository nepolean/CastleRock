package com.subsede.amc.model;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.subsede.amc.model.quote.Quotation;
import com.subsede.user.model.user.User;

public class Agent extends User {

  /**
   * default serial version UID
   */
  private static final long serialVersionUID = 1L;
  @DBRef
  List<Quotation> myQuotations;
  @DBRef
  List<Asset> myAssets;

  Agent() {

  }

  public List<Quotation> getMyQuotations() {
    if (myQuotations == null)
      myQuotations = new LinkedList<Quotation>();
    return myQuotations;
  }

  public void setMyQuotations(List<Quotation> myQuotations) {
    this.myQuotations = myQuotations;
  }

  public void addQuotation(Quotation quotation) {
    getMyQuotations().add(quotation);
  }

  public List<Asset> getMyAssets() {
    if (myAssets == null)
      this.myAssets = new LinkedList<Asset>();
    return myAssets;
  }

  public void setMyAssets(List<Asset> myAssets) {
    this.myAssets = myAssets;
  }

  public void addAsset(Asset asset) {
    getMyAssets().add(asset);
  }
}
