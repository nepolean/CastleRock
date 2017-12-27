package com.subsede.amc.controller.asset;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.subsede.amc.controller.dto.AssetDTO;
import com.subsede.amc.model.Asset;
import com.subsede.amc.service.AssetService;
import com.subsede.user.model.Customer;
import com.subsede.user.model.user.UserRegistrationDTO;
import com.subsede.user.repository.user.UserRepository;
import com.subsede.user.services.user.UserService;
import com.subsede.util.SecurityHelper;
import com.subsede.util.controller.exception.EntityNotFoundException;

@RestController
@RequestMapping("/api/v1/agent")
@Secured("ROLE_AGENT")
public class AgentActionController {
  
  private static final Logger logger = LoggerFactory.getLogger(AgentActionController.class);
  
  private SecurityHelper secHelper;
  private AssetService assetService;
  private UserRepository<Customer> uRepository;
  private UserService uService;
  
  @Autowired
  public void setRepository(
       SecurityHelper secHelper
     , AssetService aService
     , UserService uService) {
    this.assetService = aService;
    this.uService = uService;
  }
      
  @PostMapping("/user/onboard")
  public ResponseEntity<Customer> onBoardNewUser(@Validated @RequestBody UserRegistrationDTO user) {
    logger.info("Onboarding new customer {} by agent {}", user.getUsername(), this.secHelper.getLoggedInUsername()  );
    return ResponseEntity.ok().body(this.assetService.newCustomer(user));
  }
  
  @PostMapping("/asset/onboard")
  public ResponseEntity<Asset> onBoardNewAsset(@Validated @RequestBody AssetDTO assetDTO) {
    logger.info("Onboarding new asset by agent {}", this.secHelper.getLoggedInUsername());
    Asset newAsset = prepareAsset(assetDTO);
    return ResponseEntity.ok(this.assetService.createAsset(newAsset));
  }

  @PostMapping("/asset/{id}/childasset/onboard")
  public ResponseEntity<Asset> onboardChildAsset(@Validated @RequestBody AssetDTO assetDTO, @PathVariable String parentId) {
    logger.info("Onboarding child asset by agent {}", this.secHelper.getLoggedInUsername());
    Asset newAsset = this.prepareAsset(assetDTO);
    return ResponseEntity.ok(this.assetService.createSubAsset(newAsset, parentId));
  }
  
  @PostMapping("/asset/{id}/quote")
  public void newQuotation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.getRequestDispatcher("/api/v1/quote").forward(request, response);
    return;
  }

  private Asset prepareAsset(AssetDTO assetDTO) {
    String username = assetDTO.getOwner();
    Customer customer = this.uRepository.findByUsername(username);
    if (customer == null)
      throw new EntityNotFoundException(username, "Customer");
    Asset newAsset = assetDTO.getAsset();
    customer.setPrimary();
    newAsset.addOwner(customer);
    return newAsset;
  }

}
