package com.subsede.amc.model.subscription;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.subsede.amc.catalog.model.AMCPackage;
import com.subsede.amc.catalog.model.BaseMasterEntity;
import com.subsede.amc.catalog.model.BaseService;
import com.subsede.amc.catalog.model.ISubscriptionPackage;
import com.subsede.amc.catalog.model.Product;
import com.subsede.amc.catalog.model.Service;
import com.subsede.amc.model.Asset;
import com.subsede.amc.model.UserData;
import com.subsede.amc.model.quote.Quotation;
import com.subsede.user.model.user.User;

@Document(collection = "Subscriptions")
public class Subscription extends BaseMasterEntity {

  private static Logger logger = LoggerFactory.getLogger(Subscription.class);

  String quotationId;
  @DBRef(lazy = true)
  private Asset asset;
  @DBRef
  private User owner;
  @DBRef(lazy = true)
  private User agent;
  private Date startDate;
  private Date validUpto;
  private UserData userData;
  private Set<ISubscriptionPackage> products;
  private List<Service> services = new LinkedList<Service>();

  public Subscription(Quotation quotation) {
    logger.info("creating new subscription from quotation {}", quotation.getId());
    this.quotationId = quotation.getId();
    this.owner = quotation.getCreatedFor();
    this.asset = quotation.getAsset();
    this.agent = quotation.getCreatedBy();
    this.products = quotation.getSelectedItems();
    saveServices();
    this.userData = quotation.getUserData();
    this.startDate = Calendar.getInstance().getTime();
    Calendar cal = Calendar.getInstance();
    cal.setTime(startDate);
    cal.add(Calendar.MONTH, userData.getTenureInMonths());
    this.validUpto = cal.getTime();
    logger.info("created new subscription");
  }

  private void saveServices() {
    for (ISubscriptionPackage pkg : products) {
      this.services.addAll(pkg.getServices());
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Asset getAsset() {
    return asset;
  }

  public void setAsset(Asset asset) {
    this.asset = asset;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public User getAgent() {
    return agent;
  }

  public void setAgent(User agent) {
    this.agent = agent;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getValidUpto() {
    return validUpto;
  }

  public void setValidUpto(Date validUpto) {
    this.validUpto = validUpto;
  }

  public UserData getUserData() {
    return userData;
  }

  public void setUserData(UserData userData) {
    this.userData = userData;
  }

  public Set<ISubscriptionPackage> getProducts() {
    return products;
  }

  public void setProducts(Set<ISubscriptionPackage> products) {
    this.products = products;
  }

}
