package com.real.proj.amc.model;

@Document
public class AssetSubscription {
  
  @Id
  private String id;
  private 
  UserAccount user;
  Asset assetDetails;
  Set<Package> packages;
  

}
