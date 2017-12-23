package com.subsede.amc.controller.asset;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.subsede.amc.model.Apartment;
import com.subsede.amc.model.Asset;
import com.subsede.amc.model.Flat;
import com.subsede.amc.repository.AssetRepository;
import com.subsede.amc.service.AssetService;
import com.subsede.amc.service.IAssetService;
import com.subsede.user.model.user.UserRegistrationDTO;
import com.subsede.util.SecurityHelper;
import com.subsede.util.controller.FileController;
import com.subsede.util.controller.exception.EntityNotFoundException;

@RestController
@RequestMapping("/api/v1/asset")
public class AssetController extends FileController {

  private static final Logger logger = LoggerFactory.getLogger(AssetController.class);

  private AssetService assetService;
  private AssetRepository assetRepository;
  private SecurityHelper secHelper;

  @Autowired
  public void setAssetService(
      AssetService assetService,
      AssetRepository aRepository,
      SecurityHelper secHelper) {
    this.assetService = assetService;
    this.assetRepository = aRepository;
    this.secHelper = secHelper;
  }

  @RequestMapping(path="", method=RequestMethod.POST)
  public Asset newAsset(@Validated @RequestBody Asset asset, Principal loggedInUser) throws Exception {
    logger.info("New Asset created by {}", loggedInUser);
    return this.assetService.createAsset(asset);
  }
  
  @RequestMapping(path="/{id}/sub", method=RequestMethod.POST)
  public Asset newChildAsset(@PathVariable String parentId, @Validated @RequestBody Asset asset, Principal loggedInUser) throws Exception {
    logger.info("New Asset created by {}", loggedInUser.getName());
    return this.assetService.createSubAsset(asset, parentId);
  }

  @RequestMapping(path="/{id}/profile/image", method=RequestMethod.POST)
  public ResponseEntity<String> uploadProfileImage(@PathVariable String id, @RequestParam("path") MultipartFile path) {
    Asset asset = getAsset(id);
    String file = this.handleUploadFile(path);
    asset.setBackgroundImage(file);
    this.assetRepository.save(asset);
    return ResponseEntity.ok().body("The background image has been set");
  }

  @RequestMapping(path="/{id}/pic", method=RequestMethod.POST)  
  public ResponseEntity<String> uploadPic(@PathVariable String id, @RequestParam("path") MultipartFile path) {
    Asset asset = getAsset(id);
    if (asset.getImagePaths().size() == 3)
      throw new IllegalArgumentException("No more than 3 images are allowed.");
    String file = this.handleUploadFile(path);
    asset.getImagePaths().add(file);
    this.assetRepository.save(asset);
    return ResponseEntity.accepted().body("The image has been uploaded successfully");
  }

  @PostMapping("/{id}/owner")
  public ResponseEntity<String> addOwner(@PathVariable String id, @RequestBody UserRegistrationDTO owner) {
    logger.info("Adding owner to asset with id {}", id);
    this.assetService.addOwner(id, owner);
    return ResponseEntity.ok().body("Owner added successfully");
  }
  
  @DeleteMapping("/{id}/owner")
  public ResponseEntity<String> removeOwner(@PathVariable String id, @RequestBody String username) {
    logger.info("Deleting owner {} from asset with id {}", username, id);
    this.assetService.deleteOwner(id, username);
    return ResponseEntity.ok().body("Owner deleted successfully");
  }

  @GetMapping("")
  public ResponseEntity<Page<Asset>> getMyAssets(Pageable page, Principal loggedInUser) throws Exception {
    return this.fetchAssetsByRole(page);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Asset> getAsset(@PathVariable String id, Principal loggedInUser) {
    return fetchAssetByRole(id);
  }

  @RequestMapping(path = "/meta-data/flat", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Flat createFlat() {
    return Flat.getMetadata();
  }

  @RequestMapping(path = "/meta-data/aptmt", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Apartment getApartmentMetadata() {
    return Apartment.getMetadata();
  }

  private Asset getAsset(String id) {
    Asset asset = this.assetRepository.findOne(id);
    if (asset == null)
      throw new EntityNotFoundException(id, "Not Found", "Asset");
    return asset;
  }

  private ResponseEntity<Asset> fetchAssetByRole(String id) {
    Asset asset;
    String username = this.secHelper.getLoggedInUsername();
    String role = this.secHelper.getLoggedInUserRole();
    if (role.equals("ROLE_AGENT"))
      asset = this.assetRepository.findByIdAndCreatedBy(id, username);
    else if (role.equals("ROLE_USER"))
      asset = this.assetRepository.findByIdAndAssetOwner(id, username);
    else if (role.equals("ROLE_ADMIN"))
      asset = this.assetRepository.findOne(id);
    else
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    return ResponseEntity.ok(asset);
  }

  private ResponseEntity<Page<Asset>> fetchAssetsByRole(Pageable page) {
    Page<Asset> asset;
    String username = this.secHelper.getLoggedInUser().getUsername();
    String role = this.secHelper.getLoggedInUserRole();
    if (role.equals("ROLE_AGENT"))
      asset = this.assetRepository.findByCreatedBy(username, page);
    else if (role.equals("ROLE_USER"))
      asset = this.assetRepository.findByAssetOwner(username, page);
    else if (role.equals("ROLE_ADMIN"))
      asset = this.assetRepository.findAll(page);
    else
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

    return ResponseEntity.ok(asset);
  }

}