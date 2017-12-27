package com.subsede.amc.model;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.subsede.amc.catalog.model.BaseMasterEntity;
import com.subsede.amc.catalog.model.INonSubscriptionPackage;
import com.subsede.amc.catalog.model.NonSubscriptionPackage.PackageVariant;
import com.subsede.amc.catalog.model.Price;
import com.subsede.amc.catalog.model.Service;
import com.subsede.amc.model.job.ServiceMetadata;
import com.subsede.user.model.Customer;

@Document(collection = "NonSubscriptionOrders")
public class NonSubscriptionOrder extends BaseMasterEntity {  

  @DBRef
  private Customer customer;
  private Set<SelectedPackage> packages;
  private PackageVariant variant;
  private double totalAmount;
  private double totalDiscount;
  private double totalTax;
  private OrderStatus status;
  private Date orderDate;
  private String paymentId;
  private ServiceMetadata userData;
  
  public NonSubscriptionOrder(Customer customer) {
    this.customer = customer;
    status = OrderStatus.REQUESTED;
    orderDate = Calendar.getInstance().getTime();
  }

  public void addPackage(INonSubscriptionPackage selectedPkg, INonSubscriptionPackage.IPackageVariant variant) {
    if (Objects.isNull(selectedPkg) || Objects.isNull(variant))
      throw new IllegalArgumentException("Package and Variant should not be null");
    this.packages = Collections.emptySet();
    SelectedPackage selection = new SelectedPackage(selectedPkg, variant);
    this.packages.add(selection);
  }
  
  public Set<Service> getServices() {
    Set<Service> services = Collections.emptySet();
    if (Objects.nonNull(packages))
      for(SelectedPackage pkg : packages)
        services.add(pkg.getPackage().getService());
    return services;
  }
  
  public void confirmOrder() {
    if (Objects.isNull(packages))
      throw new IllegalArgumentException("Atleast one package must be added to the order");
    for(SelectedPackage pkg : packages) {
      totalAmount += pkg.amount;
      totalDiscount += pkg.getDiscount();
      totalTax += pkg.getTax();
    }
    status = OrderStatus.CONFIRMED;
  }
  
  public void setPaymentId(String paymentRequestId) {
    this.paymentId = paymentRequestId;
    status = OrderStatus.PAYMENT_INITIATED;
  }
  
  public void awaitPayment() {
    status = OrderStatus.PAYMENT_AWAITING;
  }
  
  public void paymentCompleted() {
    status = OrderStatus.PAID;
  }

  public void provisioned() {
    status = OrderStatus.COMPLETED;
  }
  
  public String getId() {
    return this.id;
  }
  
    
  public Customer getCustomer() {
    return customer;
  }

  public Set<SelectedPackage> getPackages() {
    return packages;
  }

  public PackageVariant getVariant() {
    return variant;
  }

  public double getTotalAmount() {
    return totalAmount;
  }

  public double getTotalDiscount() {
    return totalDiscount;
  }

  public double getTotalTax() {
    return totalTax;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public String getPaymentId() {
    return paymentId;
  }

  class SelectedPackage {
    
    double amount;
    double discount;
    double tax;
    
    INonSubscriptionPackage pkg;
    INonSubscriptionPackage.IPackageVariant variant;
    
    public SelectedPackage(INonSubscriptionPackage pkg, INonSubscriptionPackage.IPackageVariant variant) {
      this.pkg = pkg;
      this.variant = variant;
      Optional<Price> price = pkg.getPrice(variant.getName());
      amount = price.get().getAmount();
      discount = price.get().getDiscount();
      tax = price.get().getTax();
    }
    
    public INonSubscriptionPackage getPackage() {
      return this.pkg;
    }
    public INonSubscriptionPackage.IPackageVariant getVariant() {
      return variant;
    }
    public void setVariant(PackageVariant variant) {
      this.variant = variant;
    }
    public double getAmount() {
      return amount;
    }

    public double getDiscount() {
      return amount * discount/100;
    }
    public double getTax() {
      return (amount) * ((100 - discount)/100) * tax/100;
    }
    
  }



  public void setUserData(ServiceMetadata userData) {
    this.userData = userData;
  }
  
  public ServiceMetadata getUserData() {
    if (this.userData != null){
      this.userData.setSource(this.getClass().getName());
      this.userData.setUniqueId(this.getId());
    }
    return this.userData;
  }

}
