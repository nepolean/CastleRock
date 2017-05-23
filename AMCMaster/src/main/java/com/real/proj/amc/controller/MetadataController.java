package com.real.proj.amc.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.amc.model.AMCPackage;
import com.real.proj.amc.model.AssetType;
import com.real.proj.amc.model.PackageScheme;
import com.real.proj.amc.model.PriceData;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.deleted.FixedPricingScheme;
import com.real.proj.amc.model.deleted.RatingBasedPricingScheme;
import com.real.proj.amc.model.deleted.SubscriptionService;
import com.real.proj.amc.model.deleted.FixedPricingScheme.FixedPrice;
import com.real.proj.amc.model.deleted.RatingBasedPricingScheme.RatingBasedPrice;
import com.real.proj.amc.model.ServiceData;

@RestController
public class MetadataController {

  @RequestMapping(path = "/meta/service/subscription", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SubscriptionService> getServiceMetadata() {
    SubscriptionService service = createSubscriptionService();
    return new ResponseEntity<SubscriptionService>(service, HttpStatus.OK);
  }

  private SubscriptionService createSubscriptionService() {
    List<AssetType> assetList = new ArrayList<AssetType>();
    assetList.add(AssetType.APARTMENT);
    assetList.add(AssetType.FLAT);
    assetList.add(AssetType.HOUSE);
    List<String> amenities = new ArrayList<String>();
    amenities.add("SOME AMENITY");
    SubscriptionService service = new SubscriptionService("Asset", "Some Service", "This is new service", assetList,
        amenities);
    service.setId("10000000000000001");
    return service;
  }

  @RequestMapping(path = "/meta/asset/types", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<AssetType>> getAssetTypes() {
    return new ResponseEntity<List<AssetType>>(Arrays.asList(AssetType.values()), HttpStatus.OK);
  }

  @RequestMapping(path = "/meta/service/price/schemes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, PriceData>> getPriceSchemes() {
    Map<String, PriceData> schemes = new TreeMap<String, PriceData>();
    schemes.put(FixedPricingScheme.NAME, new FixedPrice(0.0));
    Map<Rating, Double> prices = new TreeMap<Rating, Double>();
    Rating[] ratings = Rating.values();
    for (Rating rating : ratings) {
      prices.put(rating, 0.0);
    }
    schemes.put(RatingBasedPricingScheme.NAME, new RatingBasedPrice(prices));
    return new ResponseEntity<Map<String, PriceData>>(schemes, HttpStatus.OK);
  }

  @RequestMapping(path = "/meta/service/subsription/schemes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PackageScheme>> getSLA() {
    return new ResponseEntity<List<PackageScheme>>(Arrays.asList(PackageScheme.values()), HttpStatus.OK);
  }

  @RequestMapping(path = "/meta/service/subsription/schemes/data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ServiceData> getSLAData() {
    ServiceData sld = new ServiceData(PackageScheme.PLATINUM, (int) 10);
    return new ResponseEntity<ServiceData>((ServiceData) sld, HttpStatus.OK);
  }

  @RequestMapping(path = "/meta/package", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AMCPackage> getPackage() {
    List<SubscriptionService> services = new ArrayList<SubscriptionService>();
    services.add(this.createSubscriptionService());
    AMCPackage pkg = new AMCPackage("New Package", "This is new package", Long.valueOf(12), Double.valueOf(10.0),
        services);
    pkg.setId("1000000000000001");
    return new ResponseEntity<AMCPackage>(pkg, HttpStatus.OK);
  }

}
