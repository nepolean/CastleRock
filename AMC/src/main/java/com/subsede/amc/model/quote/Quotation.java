package com.subsede.amc.model.quote;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

import com.subsede.amc.catalog.model.BaseMasterEntity;
import com.subsede.amc.catalog.model.Coupon;
import com.subsede.amc.catalog.model.ISubscriptionPackage;
import com.subsede.amc.catalog.model.Service;
import com.subsede.amc.catalog.model.SubscriptionData;
import com.subsede.amc.catalog.model.Tax;
import com.subsede.amc.catalog.model.TenureBasedDiscount;
import com.subsede.amc.catalog.model.UserInput;
import com.subsede.amc.model.Asset;
import com.subsede.amc.model.EventHistory;
import com.subsede.amc.model.UserData;
import com.subsede.user.model.user.User;

@Document(collection = "Quotations")
@WithStateMachine
@Component
public class Quotation extends BaseMasterEntity {
  
  public interface UserView { }
  
  public interface AdminView extends UserView { }

  private static Logger logger = LoggerFactory.getLogger(Quotation.class);

  double amount;

  double taxAmount;

  double discount;

  double netAmount;

  Date validUpto;

  @DBRef(lazy = true)
  private Asset asset;

  private User createdFor;

  @DBRef(lazy = true)
  private User requestedBy;

  private UserData data;

  private List<Tax> applicableTaxes;

  private Set<Coupon> applicableCoupons;

  private Set<TaxAmount> taxes = new LinkedHashSet<TaxAmount>(10);

  private Set<DiscountAmount> discounts = new LinkedHashSet<DiscountAmount>(10);

  private List<EventHistory> history = new LinkedList<EventHistory>();

  private List<String> comments;

  private Set<ServiceAmount> serviceCosts = new LinkedHashSet<ServiceAmount>(10);

  private QStates currentState;

  private Set<ISubscriptionPackage> selectedItems;

  private boolean userAccepted;

  private boolean userRejected;

  private boolean quoteApproved;

  Quotation() {

  }

  public Quotation(User createdFor, User createdBy, Asset location) {
    this.createdFor = createdFor;
    this.requestedBy = createdBy;
    this.asset = location;
  }

  public Quotation(Asset asset, User agent) {
    this.createdFor = (User) asset.getOwner();
    this.requestedBy = agent;
    this.asset = asset;
  }

  public Quotation(Quotation otherQuote) {
    this.createdFor = otherQuote.createdFor;
    this.requestedBy = otherQuote.requestedBy;
    this.asset = otherQuote.asset;
    this.data = otherQuote.data;
    this.applicableTaxes = otherQuote.applicableTaxes;
    this.applicableCoupons = otherQuote.applicableCoupons;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void addProducts(Set<ISubscriptionPackage> products) {
    products = Objects.requireNonNull(products, "Product data cannot be null");
    StringBuilder sb = new StringBuilder();
    for (ISubscriptionPackage product : products)
      try {
        this.addProduct(product);
      } catch (IllegalArgumentException ex) {
        sb.append(ex.getMessage() + "\n");
      }
    sb.trimToSize();
    if (sb.length() > 0)
      throw new IllegalArgumentException("The following products are not included.\n" + sb.toString());
  }

  public void addProduct(ISubscriptionPackage product) {
    product = Objects.requireNonNull(product, "Product cannot be null");
/*    if (!product.canSubscribe()) {
      String msg = String.format("Product, {}, does not support subscription", product.getName());
      if (logger.isErrorEnabled())
        logger.error(msg);
      throw new IllegalArgumentException(msg);
    }*/
    getSelectedItems().add(product);
  }

  public void setUserData(UserData data) {
    boolean firstTime = Objects.isNull(this.data);
    this.data = Objects.requireNonNull(data, "User data cannot be null");
    if (!firstTime)
      this.createQuote();
  }

  public void modifyUserData(UserData data) {
    this.setUserData(data);
  }

  public void setApplicableTaxes(List<Tax> applicableTaxes) {
    this.applicableTaxes = applicableTaxes;
  }

  public void setCoupons(Set<Coupon> applicableCoupons) {
    this.applicableCoupons = applicableCoupons;
  }

  public void createQuote() {
    logger.info("New quote requested");
    
    resetQuote();
    calculateTotalPrice(data.getInput());
    calculateTax();
    applyCoupons();

    this.netAmount = this.amount + this.taxAmount - this.discount;

    if (logger.isDebugEnabled()) {
      logger.debug("Total Amount = {}", this.amount);
      logger.debug("Total Discnt = {}", this.discount);
      logger.debug("Total taxamt = {}", this.taxAmount);
      logger.debug("Total netamt = {}", this.netAmount);
    }
    
    this.createdOn = Calendar.getInstance().getTime();
    setExpiry();
    logger.info("New quote generated. It will expire on {}", SimpleDateFormat.getDateInstance().format(validUpto));
  }

  private void setExpiry() {
    Calendar today = Calendar.getInstance();
    today.add(Calendar.DAY_OF_MONTH, 30);
    this.validUpto = today.getTime();
  }

  private void resetQuote() {
    this.amount = 0.0;
    this.discount = 0.0;
    this.taxAmount = 0.0;
    this.netAmount = 0.0;
    this.taxes.clear();
    this.discounts.clear();
    this.serviceCosts.clear();
  }

  private void applyCoupons() {

  }

  private void calculateTax() {
    if (Objects.nonNull(this.taxes))
      this.taxes.clear();
    else
      logger.warn("Generating quote with out any taxes?");
    double totalTax = 0.0;
    for (Tax tax : applicableTaxes) {
      double taxPct = tax.getPercentage();
      double taxAmt = this.amount * (taxPct / 100);
      totalTax += taxAmt;
      TaxAmount taxObj = new TaxAmount(tax.getType(), taxAmt);
      this.taxes.add(taxObj);
    }
    this.taxAmount = totalTax;
  }

  private void calculateTotalPrice(UserInput<String, Object> input) {
    double totalAmount = 0.0;
    double totalDiscount = 0.0;
    for (ISubscriptionPackage product : getSelectedItems()) {
      SubscriptionData subsData = product.fetchSubscriptionData(input);
      if (logger.isDebugEnabled())
        logger.debug("Product {}, subscription data {} ", product.getName(), subsData);
      double currentAmount = subsData.getSubscriptionPrice() * this.data.getTenure() * this.data.getMeasuredArea();
      ServiceAmount cost = new ServiceAmount(product.getName(), currentAmount);
      this.serviceCosts.add(cost);
      double discountPct = getDiscountPct(product);
      double discountedAmount = currentAmount * (discountPct / 100);
      if (discountedAmount != 0) {
        DiscountAmount discAmount = new DiscountAmount("TenureBasedDiscount_" + product.getName(), discountedAmount);
        this.discounts.add(discAmount);
      }
      totalAmount += currentAmount;
      totalDiscount += discountedAmount;
    }
    this.amount = totalAmount;
    this.discount = totalDiscount;
  }

  private double getDiscountPct(ISubscriptionPackage product) {
    double discountPct = 0.0;
    if (logger.isDebugEnabled())
      logger.debug("Calculating discount");
    TenureBasedDiscount disc = product.getTenureBasedDisc();
    if (logger.isDebugEnabled())
      logger.debug("{}", disc);
    if (Objects.nonNull(disc)) {
      discountPct = disc.getDiscountFor(this.data.getTenure());
      if (logger.isDebugEnabled())
        logger.debug("Requested tenure {}, Disc pct {}", this.data.getTenure(), discountPct);
    }
    return discountPct;
  }

  public double getTotalAmount() {
    return amount;
  }

  public void setTotalAmount(double totalAmount) {
    this.amount = totalAmount;
  }

  public double getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(double taxAmount) {
    this.taxAmount = taxAmount;
  }

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public double getNetAmount() {
    return netAmount;
  }

  public void setNetAmount(double netAmount) {
    this.netAmount = netAmount;
  }

  public List<String> getComments() {
    return comments;
  }

  public void setComments(List<String> comments) {
    this.comments = comments;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public Date getValidUpto() {
    return validUpto;
  }

  public void setValidUpto(Date validUpto) {
    this.validUpto = validUpto;
  }

  public boolean hasExpired() {
    return Objects.isNull(this.validUpto) ? false
        : Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime().after(this.validUpto);
  }
  
  public boolean isUserAccepted() {
    return this.userAccepted;
  }
  
  public boolean isUserRejected() {
    return this.userRejected;
  }

  @Override
  public String toString() {
    StringBuilder template = new StringBuilder();
    template.append("\nDear " + this.createdFor.getFirstName() + ",\n");
    template.append(
        "  Thanks for showing interest in our services. Based on your selected services, hereunder is the quote for your perusal\n");
    template.append("Service Cost - \n");
    for (ServiceAmount svcAmt : this.serviceCosts)
      template.append("            " + svcAmt + "\n");
    if (this.discounts.size() > 0) {
      template.append("Discounts - \n");
      for (DiscountAmount disc : this.discounts)
        template.append("            " + disc + "\n");
    }
    if (this.taxes.size() > 0) {
      template.append("Taxes - \n");
      for (TaxAmount tax : this.taxes)
        template.append("            " + tax + "\n");
    }
    template.append("-------------------------------------------------\n");
    template.append("            Net Amount     =    " + this.netAmount + "\n");
    template.append("-------------------------------------------------\n");

    return template.toString();
  }

  public static class ServiceAmount {

    private String name;
    private double serviceCost;

    public ServiceAmount() {

    }

    public ServiceAmount(String name, double serviceCost) {
      this.name = name;
      this.serviceCost = serviceCost;
    }

    @Override
    public String toString() {
      return "@" + name + " = " + serviceCost + "";
    }

  }

  public static class DiscountAmount {

    private String name;
    private double discount;

    public DiscountAmount() {

    }

    public DiscountAmount(String name, double discount) {
      this.name = name;
      this.discount = discount;
    }

    @Override
    public String toString() {
      return "@" + name + " = -" + discount + "";
    }

  }

  public static class TaxAmount {

    private String type;
    private double tax;

    public TaxAmount() {

    }

    public TaxAmount(String type, double taxAmt) {
      this.type = type;
      this.tax = taxAmt;
    }

    @Override
    public String toString() {
      return "@" + type + " = +" + tax + "";
    }

  }

  public Set<ISubscriptionPackage> getSelectedItems() {
    selectedItems = (Objects.isNull(selectedItems) ? new HashSet<ISubscriptionPackage>() : selectedItems);
    return selectedItems;
  }

  public void setSelectedItems(Set<ISubscriptionPackage> selectedItems) {
    this.selectedItems = Objects.requireNonNull(selectedItems, "Choose at least one service");
  }
  
  public Set<Service> getSelectedServices() {
    Set<Service> services = Collections.emptySet();
    for (ISubscriptionPackage pkg : getSelectedItems()) {
      services.addAll(pkg.getServices());
    }
    return services;
  }

  public UserData getUserData() {
    return this.data;
  }

  public List<EventHistory> getHistory() {
    if (this.history == null)
      this.history = new LinkedList<EventHistory>();
    return history;
  }

  public void setState(QStates currentState) {
    this.currentState = currentState;
  }

  public QStates getState() {
    return this.currentState;
  }

  public void userAccepted() {
    this.userAccepted = true;
  }

  public void userRejected() {
    this.userRejected = true;
  }

  public void approveQuote() {
    this.quoteApproved = true;
  }

  public boolean hasBeenApproved() {
    return this.quoteApproved;
  }

  public User getCreatedFor() {
    return this.createdFor;
  }

  public void setCreatedFor(User createdFor) {
    this.createdFor = createdFor;
  }

  public Asset getAsset() {
    return this.asset;
  }

  public void setAsset(Asset asset) {
    this.asset = asset;
  }

  public User getRequestedBy() {
    return this.requestedBy;
  }

  public void setCreatedBy(User createdBy) {
    this.requestedBy = createdBy;
  }
}
