package com.real.proj.amc.controller;

import java.security.Principal;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.amc.model.MaintenanceService;

@RestController
public class AMCAdminController {

  
  public void defineAService(@Validated @RequestBody MaintenanceService service, Principal loggedInUser) {
    
  }
  
  public void defineAPackage(@RequestParam String packageName, Principal loggedInUser) {
    
  }
  
  public void addAServiceToPackage(@RequestParam String packageId, @RequestParam String sericeId, Principal loggedInUser) {
    
  }
  
  public void definePricingForAService(@PathVariable String service, @Validated @RequestBody String priceDetails, Principal loggedInUser) {
    
  }
}
