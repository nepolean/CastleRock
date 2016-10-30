package com.real.proj.notif.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.notif.model.EmailMessage;
import com.real.proj.notif.service.NotificationService;

@RestController
public class NotificationController {
  
  @RequestMapping(name="/notify/email", method=RequestMethod.POST, produces="application/json")
  public void sendEmail(@RequestBody EmailMessage email) {
    
    try {
      
      String to = email.getTo();
      List<String> ccList = email.getCcList();
      String subject = email.getSubject();
      String message = email.getMessage();
      
      NotificationService.sendEmail(to, ccList, subject, message);
      
    } catch (Exception e) {
      // TODO retry after some time? 
    }
    
  }

}
