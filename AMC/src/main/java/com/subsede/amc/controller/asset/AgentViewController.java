package com.subsede.amc.controller.asset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.subsede.amc.model.Asset;
import com.subsede.amc.repository.AssetRepository;
import com.subsede.user.model.user.SystemRoles;

@RestController
@RequestMapping("/api/v1/agent")
@Secured (SystemRoles.ROLE_AGENT)
public class AgentViewController {
  
  private static final Logger logger = LoggerFactory.getLogger(AgentViewController.class);
  private AssetRepository aRepository;
  
  @Autowired
  public void setRepository(AssetRepository aRepository) {
    this.aRepository = aRepository;
  }
  
  public Page<Asset> getMyAssets() {
    
  }
  

}
