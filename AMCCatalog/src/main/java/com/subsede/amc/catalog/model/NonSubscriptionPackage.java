package com.subsede.amc.catalog.model;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class NonSubscriptionPackage extends BasePackages implements INonSubscriptionPackage {

  private static final Logger logger = LoggerFactory.getLogger(NonSubscriptionPackage.class);
  
  @DBRef
  private Service service;
  
  private Map<String, IPackageVariant> variants;
  
  private double discountPct = 0.0;
  
  public NonSubscriptionPackage(Category category, String name, String description) {
    super(category, name, description);
    type = this.getClass().getName();
    isActive = false;
  }
  
  public void setService(Service svc) {
    this.service = svc;
    this.variants = Collections.emptyMap();
  }
  
  public Service getService() {
    return this.service;
  }
  
  public void addVariant(PackageVariant variant) {
    if (logger.isDebugEnabled())
      logger.debug("Adding new package variant {}" , variant);
    if (Objects.isNull(variant))
      throw new IllegalArgumentException("Variant value should not be null");
    if (variants == null)
      variants = Collections.emptyMap();
    variants.put(variant.getName(), variant);
  }
  
  public void removeVariant(PackageVariant variant) {
    if (logger.isDebugEnabled())
      logger.debug("Removing package variant {}" , variant);
    if (Objects.nonNull(variants) && Objects.nonNull(variant))
      variants.remove(variant.getName());
  }
  
  
  @Override
  public Map<String, IPackageVariant> getVariants() {
    return variants;
  }

  public void setVariants(Map<String, IPackageVariant> variants) {
    this.variants = variants;
  }

  public double getDiscountPct() {
    return discountPct;
  }

  public void setDiscountPct(double discountPct) {
    this.discountPct = discountPct;
  }

  @Override
  public Optional<Price> getPrice(String variantName) {
    Price price = null;
    if (Objects.nonNull(variants)) {
      IPackageVariant variant = this.variants.get(variantName);
      double amt = variant.getPrice();
      double taxPct = this.service.getTax() != null ? this.service.getTax().getPercentage() : 0;
      price = new Price(amt, this.getDiscountPct(), taxPct);
    }
    return Optional.of(price);
  }

  public static class PackageVariant implements IPackageVariant {
    
    @NotNull
    @NotBlank (message = "Name should not be empty")
    private String name;
    
    @Range(min=1)
    private double price;
    
    @NotNull
    @NotBlank (message = "UOM should not be empty")
    private String uom;
    
    public PackageVariant(String name, double price, String uom) {
      this.name = name;
      this.price = price;
      this.uom = uom;
    }
    
        
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
    
    public double getPrice() {
      return price;
    }



    public void setPrice(double price) {
      this.price = price;
    }



    public String getUOM() {
      return uom;
    }



    public void setUom(String uom) {
      this.uom = uom;
    }



    public boolean equals(PackageVariant variant) {
      if (variant == null || variant.getName() == null)
        return false;
      return variant.getName().equalsIgnoreCase(name);
    }
    
  }

  @Override
  protected boolean validate(Service service) {
    service = Objects.requireNonNull(service, "Null value passed for service");
    if (!service.canRequestOneTime()) {
      String msg = String.format("The service, {}, does not support packaging model ", service.getName());
      if (logger.isErrorEnabled())
        logger.error(msg);
      throw new IllegalArgumentException(msg);
    }
    if (!service.getCategory().equals(this.category)) {
      String msg = String.format("The category does not match. Expected {}, provided {}", this.category,
          service.getCategory());
      if (logger.isErrorEnabled())
        logger.error(msg);
      throw new IllegalArgumentException(msg);
    }
    return true;  }
}
