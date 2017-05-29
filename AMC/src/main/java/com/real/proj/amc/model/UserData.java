package com.real.proj.amc.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserData {

  private double measuredArea;
  private int tenure;
  private UserInput<String, Object> input;
  private Set<Product> selectedItems;

  public UserData(Rating rating, int tenure, double measuredArea) {
    this.measuredArea = measuredArea;
    this.tenure = tenure;
    this.setRating(rating);
  }

  public UserData(Product selectedItem, Rating rating, int tenure, double measuredArea) {
    selectedItem = Objects.requireNonNull(selectedItem, "The product data cannot be null");
    rating = Objects.requireNonNull(rating, "Rating cannot be null");
    this.measuredArea = measuredArea;
    this.tenure = tenure;
    this.setRating(rating);
    this.getSelectedItems().add(selectedItem);
  }

  public UserData(Set<Product> selectedItems, Rating rating, int tenure, double measuredArea) {
    this.measuredArea = measuredArea;
    this.tenure = tenure;
    this.setSelectedItems(selectedItems);
    this.setRating(rating);
  }

  public void addProduct(Product product) {
    product = Objects.requireNonNull(product, "The product data cannot be null");
    this.getSelectedItems().add(product);
  }

  public double getMeasuredArea() {
    return measuredArea;
  }

  public void setMeasuredArea(double measuredArea) {
    this.measuredArea = measuredArea;
  }

  public int getTenure() {
    return tenure;
  }

  public void setTenure(int tenure) {
    this.tenure = tenure;
  }

  public Rating getRating() {
    return (Rating) input.get(Rating.getKey());
  }

  public void setRating(Rating rating) {
    UserInput<String, Object> userInput = new UserInput<String, Object>();
    userInput.add(Rating.getKey(), rating);
    this.input = userInput;
  }

  public Set<Product> getSelectedItems() {
    selectedItems = (Objects.isNull(selectedItems) ? new HashSet<Product>() : selectedItems);
    return selectedItems;
  }

  public void setSelectedItems(Set<Product> selectedItems) {
    this.selectedItems = Objects.requireNonNull(selectedItems, "Choose at least one service");
  }

  public UserInput<String, Object> getInput() {
    return this.input;
  }

}