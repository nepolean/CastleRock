package com.subsede.amc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.subsede.amc.catalog.model.AMCPackage;
import com.subsede.amc.catalog.model.asset.Amenity;
import com.subsede.amc.catalog.model.asset.AssetType;
import com.subsede.amc.model.Apartment;
import com.subsede.amc.model.Asset;
import com.subsede.amc.model.Location;
import com.subsede.amc.model.UOM;
import com.subsede.amc.repository.AssetRepository;
import com.subsede.user.model.Customer;
import com.subsede.user.model.user.User;
import com.subsede.user.model.user.UserRegistrationDTO;
import com.subsede.user.repository.user.UserRepository;
import com.subsede.user.services.user.UserService;
import com.subsede.util.controller.exception.EntityNotFoundException;

@Service
public class AssetService implements IAgentAssetService, IAssetService {

  @Autowired
  private AssetRepository assetRepository;
  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository<Customer> cRepository;
  @Autowired
  private PasswordEncoder pEncoder;

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.subsede.amc.service.IAssetService#createAsset(com.subsede.amc.model.
   * Asset, java.lang.String)
   */
  @Override
  public Asset createAsset(Asset asset) {
    return assetRepository.save(asset);
  }

  public Asset createApartment(String loggedInUser) {
    User authorizedUser = userService.findByUsername(loggedInUser);
    Asset newAsset = new Apartment("Springfield", new Location(), new Apartment.Details(100, 2, 1000.0, UOM.SFT));
    // newAsset.setCreatedBy(authorizedUser);
    Asset saved = assetRepository.save(newAsset);
    return saved;
  }


  public Asset onBoardAsset(Asset asset) {
    // TODO Auto-generated method stub
    return null;
  }

  public Asset addPackage(Asset asset, Package service) {
    // TODO Auto-generated method stub
    return null;
  }



  public Asset getAssetById(String assetId) {
    Asset asset = this.assetRepository.findOne(assetId);
    if (asset == null)
      throw new EntityNotFoundException();
    return asset;
  }


  /*
   * (non-Javadoc)
   * 
   * @see com.subsede.amc.service.IAssetService#addOwner(com.subsede.user.model.
   * Customer)
   */
  @Override
  public void addOwner(String assetId, UserRegistrationDTO owner) {
    Asset asset = getAsset(assetId);
    Customer customer = new Customer(
        owner.getUsername(),
        pEncoder.encode(owner.getPassword()),
        owner.getAddress());
    customer.setFirstName(owner.getFirstName());
    customer.setMiddleName(owner.getEmail());
    customer.setLastName(owner.getLastName());
    customer.setEmail(owner.getEmail());
    customer.setMobileNo(owner.getMobileNo());
    if(owner.isPrimary()) customer.setPrimary();
    customer.setAccountLocked(false);
    Customer savedObj = this.cRepository.save(customer);
    asset.addOwner(savedObj);
    this.assetRepository.save(asset);
  }

  public void deleteOwner(String assetId, String username) {
    Asset asset = this.getAsset(assetId);
    if (asset.getAssetOwner() != null)
      asset.getAssetOwner().removeIf(
          (Customer customer) -> customer.getUsername().equals(username));
    this.assetRepository.save(asset);
  }



  @Override
  public Page<Asset> getAssetsIOnBoarded(String username) {
    // TODO Auto-generated method stub
    return null;
  }
  private Asset getAsset(String assetId) {
    Asset asset = this.assetRepository.findOne(assetId);
    if (asset == null)
      throw new EntityNotFoundException(assetId, "Asset not found", "Asset");
    return asset;
  }

  @Override
  public Asset createSubAsset(Asset asset, String parentId) {
    Asset parent = this.getAsset(parentId);
    asset.setParent(parent);
    return this.assetRepository.save(parent);
  }

}
