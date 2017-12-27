package com.subsede.amc.service;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.subsede.amc.model.Apartment;
import com.subsede.amc.model.Asset;
import com.subsede.amc.model.Location;
import com.subsede.amc.model.UOM;
import com.subsede.amc.repository.AssetRepository;
import com.subsede.user.model.Customer;
import com.subsede.user.model.user.Role;
import com.subsede.user.model.user.UserRegistrationDTO;
import com.subsede.user.repository.user.RoleRepository;
import com.subsede.user.repository.user.UserRepository;
import com.subsede.util.controller.exception.EntityNotFoundException;

@Service
public class AssetService implements IAgentAssetService, IAssetService {

  private static Logger logger = LoggerFactory.getLogger(AssetService.class);
  
  @Autowired
  private AssetRepository assetRepository;
  
  @Autowired
  private UserRepository<Customer> cRepository;
  
  @Autowired
  private RoleRepository rRepository;
  
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

  public List<Customer> getCommunity(String assetId) {
    logger.info("Get community details for asset {}", assetId);
    Asset asset = this.getAsset(assetId);
    if (!asset.isCommunityBased())
      throw new IllegalArgumentException("The specified asset is not community based asset");
    String parent = asset.getParent();
    List<Asset> allChild = this.assetRepository.findByParendId(parent);
    List<Customer> community = Collections.emptyList();
    for (Asset child : allChild)
      community.addAll(child.getAssetOwner());
    return community;
  }
  
  public List<Customer> getCommitte(String assetId) {
    logger.info("Get committe details for asset {}", assetId);
    Asset asset = this.getAsset(assetId);
    if (!asset.isCommunityBased())
      throw new IllegalArgumentException("The specified asset is not community based asset");
    String parentId = asset.getParent();
    Asset parent = this.getAsset(parentId);
    return parent.getAssetOwner();
  }
  
  public Asset createSampleApartment(String loggedInUser) {
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
    Customer savedObj = newCustomer(owner);
    asset.addOwner(savedObj);
    this.assetRepository.save(asset);
  }

  public Customer newCustomer(UserRegistrationDTO owner) {
    Role role = this.rRepository.findByName("CUSTOMER");
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
    customer.addRole(role);
    Customer savedObj = this.cRepository.save(customer);
    return savedObj;
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
    asset.setParent(parent.getId());
    return this.assetRepository.save(parent);
  }

}
