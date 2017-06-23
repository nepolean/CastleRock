package com.real.proj.amc.model;

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

import com.real.proj.amc.model.quote.Quotation;
import com.real.proj.user.model.User;

@Document(collection = "Subscriptions")
public class Subscription extends BaseMasterEntity {

  private static Logger logger = LoggerFactory.getLogger(Subscription.class);

  @Id
  String id;
  String quotation_id;
  @DBRef(lazy = true)
  private Asset asset;
  @DBRef
  private User owner;
  @DBRef(lazy = true)
  private User agent;
  private Date startDate;
  private Date validUpto;
  private UserData userData;
  private Set<Product> products;
  private List<Service> services = new LinkedList<Service>();

  public Subscription(Quotation quotation) {
    logger.info("creating new subscription from quotation {}", quotation.getId());
    this.quotation_id = quotation.getId();
    this.owner = quotation.getCreatedFor();
    this.asset = quotation.getAsset();
    this.agent = quotation.getCreatedBy();
    this.products = quotation.getSelectedItems();
    saveServices();
    this.userData = quotation.getUserData();
    this.startDate = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(startDate);
    cal.add(Calendar.MONTH, userData.getTenureInMonths());
    this.validUpto = cal.getTime();
    scheduleTasks();
    logger.info("created new subscription");
  }

  private void saveServices() {
    for (Product p : products) {
      if (p instanceof AMCPackage) {
        AMCPackage pkg = (AMCPackage) p;
        this.services.addAll(pkg.getServices());
      } else {
        this.services.add((BaseService) p);
      }
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

  public Set<Product> getProducts() {
    return products;
  }

  public void setProducts(Set<Product> products) {
    this.products = products;
  }

}
