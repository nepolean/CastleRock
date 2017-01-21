package com.real.proj.amc.controller;

import java.security.Principal;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.amc.model.Apartment;
import com.real.proj.amc.model.Asset;
import com.real.proj.amc.model.Flat;
import com.real.proj.amc.service.AssetService;

@RestController
public class AssetController {

  private static final Logger logger = LogManager.getLogger(AssetController.class);

  private AssetService assetService;

  @Autowired
  public void setAssetService(AssetService assetService) {
    this.assetService = assetService;
  }

  @RequestMapping(path = { "/asset/create" }, method = { RequestMethod.POST }, produces = {
      "application/json" }, consumes = { "application/json" })
  public Asset createProperty(@Validated @RequestBody Asset asset, Principal loggedInUser) throws Exception {
    return this.assetService.createAsset(asset, loggedInUser.getName());

  }

  @RequestMapping(path = "/meta-data/aptmt", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Apartment getApartmentMetadata() {
    return new Apartment();
  }

  @RequestMapping(path = { "/assets" }, method = { RequestMethod.GET }, produces = { MediaType.APPLICATION_JSON_VALUE })
  public List<Asset> getMyAssets(Principal loggedInUser) throws Exception {
    return this.assetService.getMyAssets(loggedInUser.getName());

  }

  @RequestMapping(path = "/meta-data/flat", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Flat createFlat() {
    return new Flat();
  }

}