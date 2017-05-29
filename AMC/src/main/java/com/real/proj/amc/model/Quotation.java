package com.real.proj.amc.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.model.Action;
import org.statefulj.fsm.model.State;
import org.statefulj.fsm.model.impl.StateImpl;

import com.real.proj.user.model.User;

public class Quotation {

  static class QuotationAction implements Action<Quotation> {
    @Override
    public void execute(Quotation paramT, String paramString, Object... paramArrayOfObject) throws RetryException {
      // logger.info("Action on quote for {}",
      // paramT.createdFor.getFirstName());
      logger.info("Event name {}", paramString);
      // throw new RuntimeException("I cannot perform action");
      // logger.info("Parameters {}", paramArrayOfObject);
    }

  }

  private static Logger logger = LoggerFactory.getLogger(Quotation.class);

  /* States related to FSM */

  public static enum States {
    // @formatter:off
    INIT (new StateImpl<Quotation>("INIT")),
    QUOTE_GENERATED (new StateImpl<Quotation>("QUOTE_GENERATED")),
    QUOTE_ACCEPTED (new StateImpl<Quotation>("QUOTE_ACCEPTED")),
    QUOTE_REGENERATED (new StateImpl<Quotation>("QUOTE_REGENERATED")), 
    QUOTE_APPROVED (new StateImpl<Quotation>("QUOTE_APPROVED")), 
    PAYMENT_RECEIVED (new StateImpl<Quotation>("PAYMENT_RECVD", false)),
    END (new StateImpl<Quotation>("END", true));
    // @formatter:on

    State<Quotation> myState;

    States(State<Quotation> state) {
      this.myState = state;
    }

    public State<Quotation> get() {
      return this.myState;
    }

    public String toString() {
      return get().getName();
    }

  }
  /* Events related to FSM */

  public static enum Event {
    //@formatter:off
    GENERATE_QUOTE("GENERATE_QUOTE"), 
    ACCEPT_QUOTE("ACCEPT_QUOTE"), 
    REGENERATE_QUOTE("REGENERATE_QUOTE"), 
    APPROVE_QUOTE("APPROVE_QUOTE"), 
    PAYMENT_RECEIVED("PAYMENT_RECEIVED"), 
    END("END");
    //@formatter:on

    private String value;

    Event(String value) {
      this.value = value;
    }

  }

  /* Transitions */

  // @formatter:off
  static {
    States.INIT.get().addTransition(Event.GENERATE_QUOTE.name(), States.QUOTE_GENERATED.get(), new QuotationAction());
    States.QUOTE_GENERATED.get().addTransition(Event.ACCEPT_QUOTE.name(), States.QUOTE_ACCEPTED.get(), new QuotationAction());
    States.QUOTE_ACCEPTED.get().addTransition(Event.REGENERATE_QUOTE.name(), States.QUOTE_REGENERATED.get(), new QuotationAction());
    States.QUOTE_REGENERATED.get().addTransition(Event.ACCEPT_QUOTE.name(), States.QUOTE_APPROVED.get(), new QuotationAction());
    States.QUOTE_ACCEPTED.get().addTransition(Event.APPROVE_QUOTE.name(), States.QUOTE_APPROVED.get(), new QuotationAction());
    States.QUOTE_APPROVED.get().addTransition(Event.PAYMENT_RECEIVED.name(), States.PAYMENT_RECEIVED.get(), new QuotationAction());
    States.PAYMENT_RECEIVED.get().addTransition(Event.END.name(), States.END.get());
  }
  // @formatter:on

  /* This variable maintains the current state of the workflow */
  private String state;

  double amount;

  double taxAmount;

  double discount;

  double netAmount;

  Date createdOn;

  Date validUpto;

  private Location location;

  private User createdFor;

  private User createdBy;

  private boolean isNewUser;

  private UserData data;

  private Set<Tax> applicableTaxes;

  private Set<Coupon> applicableCoupons;

  private Set<TaxAmount> taxes = new LinkedHashSet<TaxAmount>(10);

  private Set<DiscountAmount> discounts = new LinkedHashSet<DiscountAmount>(10);

  private Set<Event> history = new HashSet<Event>();

  private List<String> comments;

  private boolean isAgent;

  private Set<ServiceAmount> serviceCosts = new LinkedHashSet<ServiceAmount>(10);

  public Quotation(User createdFor, User createdBy, Location location) {
    this.createdFor = createdFor;
    this.createdBy = createdBy;
    this.location = location;
    isNewUser = Objects.isNull(createdFor.getUserName());
    isAgent = createdBy.equals(createdFor);
  }

  public Quotation(Asset asset, User agent) {
    this.createdFor = asset.getOwner();
    this.createdBy = agent;
    this.location = asset.getLocation();
    isNewUser = false;
    isAgent = Objects.isNull(agent);
  }

  public Quotation(Quotation otherQuote) {

    this.createdFor = otherQuote.createdFor;
    this.createdBy = otherQuote.createdBy;
    this.location = otherQuote.location;
    this.data = otherQuote.data;
    this.applicableTaxes = otherQuote.applicableTaxes;
    this.applicableCoupons = otherQuote.applicableCoupons;

  }

  public void addProducts(Set<Product> products) {
    products = Objects.requireNonNull(products, "Product data cannot be null");
    StringBuilder sb = new StringBuilder();
    for (Product product : products)
      try {
        this.addProduct(product);
      } catch (IllegalArgumentException ex) {
        sb.append(ex.getMessage() + "\n");
      }
    sb.trimToSize();
    if (sb.length() > 0)
      throw new IllegalArgumentException("The following products are not included.\n" + sb.toString());
  }

  public void addProduct(Product product) {
    product = Objects.requireNonNull(product, "Product cannot be null");
    if (!product.canSubscribe()) {
      String msg = String.format("Product, {}, does not support subscription", product.getName());
      if (logger.isErrorEnabled())
        logger.error(msg);
      throw new IllegalArgumentException(msg);
    }
    getSelectedItems().add(product);
  }

  public Set<Product> getSelectedItems() {
    return data.getSelectedItems();
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

  public void setApplicableTaxes(Set<Tax> applicableTaxes) {
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
    this.createdOn = new Date();
    // valid for 30 days
    long duration = 30 * 24 * 60 * 60 * 100;
    this.validUpto = new Date(this.createdOn.getTime() + duration);
    logger.info("New quote generated. It will expire on {}", SimpleDateFormat.getDateInstance().format(validUpto));
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
    if (Objects.nonNull(applicableCoupons))
      for (Coupon cpn : applicableCoupons) {
        // cpn.applyDiscount(pkgs, totalAmount)
      }

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
    for (Product product : data.getSelectedItems()) {
      SubscriptionData subsData = product.getSubscriptionData(input);
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

  private double getDiscountPct(Product product) {
    double discountPct = 0.0;
    if (product.getType().equals(AMCPackage.TYPE)) {
      if (logger.isDebugEnabled())
        logger.debug("Calculating discount");
      AMCPackage pkg = (AMCPackage) product;
      TenureBasedDiscount disc = pkg.getTenureBasedDisc();
      if (logger.isDebugEnabled())
        logger.debug("{}", disc);
      if (Objects.nonNull(disc)) {
        discountPct = disc.getDiscountFor(this.data.getTenure());
        if (logger.isDebugEnabled())
          logger.debug("Requested tenure {}, Disc pct {}", this.data.getTenure(), discountPct);
      }
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
    return Objects.isNull(this.validUpto) ? false : System.currentTimeMillis() > this.validUpto.getTime();
  }

  @Override
  public String toString() {
    // return "Quotation [totalAmount=" + amount + ", taxAmount=" + taxAmount
    // +
    // ", discount=" + discount
    // + ", netAmount=" + netAmount + "]";

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

  public class ServiceAmount {

    private String name;
    private double serviceCost;

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

    public TaxAmount(String type, double taxAmt) {
      this.type = type;
      this.tax = taxAmt;
    }

    @Override
    public String toString() {
      return "@" + type + " = +" + tax + "";
    }

  }

  public UserData getUserData() {
    return this.data;
  }

  public String getState() {
    return this.state;
  }

}
