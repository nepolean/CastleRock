package com.subsede.amc.catalog.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * @author murali
 *         This interfaces provides an unique abstraction over service and
 *         package.
 *
 */
@Document(collection = "Products")
public interface Product {

  public abstract String getName();

  public abstract String getType();

  public abstract Category getCategory();

  public abstract String getId();

}
