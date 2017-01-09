package com.real.proj.amc.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

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
import com.real.proj.controller.exception.DBException;
import com.real.proj.controller.exception.EntityNotFoundException;
import com.real.proj.controller.exception.SecurityPermissionException;

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
    try {
      return this.assetService.createAsset(asset, loggedInUser.getName());
    } catch (Exception ex) {
      this.handleException(ex);
      return null;
    }
  }

  @RequestMapping(path = "/asset/aptmt/metadata", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Apartment getApartmentMetadata() {
    return new Apartment();
  }

  @RequestMapping(path = { "/assets" }, method = { RequestMethod.GET }, produces = { MediaType.APPLICATION_JSON_VALUE })
  public List<Asset> getMyAssets(Principal loggedInUser) throws Exception {
    try {
      return this.assetService.getMyAssets(loggedInUser.getName());
    } catch (Exception ex) {
      this.handleException(ex);
      return null;
    }
  }
  
  @RequestMapping (path="/meta-data/flat", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Flat createFlat() {
    return new Flat();
  }

  private void handleException(Exception ex) throws Exception {
    String uuid = UUID.randomUUID().toString();
    logger.warn(uuid);
    if (logger.isErrorEnabled())
      logger.error("Error ", ex);
    if (ex instanceof EntityNotFoundException) {
      throw (EntityNotFoundException) ex;
    } else if (ex instanceof SecurityPermissionException) {
      throw ex;
    } else if (ex instanceof DBException) {
      ((DBException) ex).withUUID(uuid);
    } else if (ex instanceof IllegalStateException) {
      throw (IllegalStateException) ex;
    } else {
      throw new RuntimeException(uuid);
    }
  }
}