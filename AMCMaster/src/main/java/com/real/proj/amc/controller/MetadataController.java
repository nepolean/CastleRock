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

import com.real.proj.amc.model.AssetType;
import com.real.proj.amc.model.FixedPricingScheme;
import com.real.proj.amc.model.FixedPricingScheme.FixedPrice;
import com.real.proj.amc.model.PackageScheme;
import com.real.proj.amc.model.PriceData;
import com.real.proj.amc.model.Rating;
import com.real.proj.amc.model.RatingBasedPricingScheme;
import com.real.proj.amc.model.RatingBasedPricingScheme.RatingBasedPrice;
import com.real.proj.amc.model.ServiceLevelData;
import com.real.proj.amc.model.SubscriptionService;

@RestController
public class MetadataController {

  @RequestMapping(path = "/meta/service/subscription", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SubscriptionService> getServiceMetadata() {
    List<AssetType> assetList = new ArrayList<AssetType>();
    assetList.add(AssetType.APARTMENT);
    assetList.add(AssetType.FLAT);
    assetList.add(AssetType.HOUSE);
    List<String> amenities = new ArrayList<String>();
    amenities.add("SOME AMENITY");
    SubscriptionService service = new SubscriptionService("", "", "", assetList, amenities);
    return new ResponseEntity<SubscriptionService>(service, HttpStatus.OK);
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
  public ResponseEntity<ServiceLevelData> getSLAData() {
    PackageScheme gold = PackageScheme.GOLD;
    ServiceLevelData sld = new ServiceLevelData(gold, 10);
    return new ResponseEntity<ServiceLevelData>(sld, HttpStatus.OK);
  }
}
