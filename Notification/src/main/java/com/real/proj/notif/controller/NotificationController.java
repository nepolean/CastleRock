package com.real.proj.notif.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.notif.model.EmailMessage;
import com.real.proj.notif.model.SMSMessage;
import com.real.proj.notif.service.NotificationService;

@RestController
@ComponentScan
public class NotificationController {
  
  private static final Logger logger = LogManager.getLogger("NotificationController");
  
  @RequestMapping(name="/notify/email", method=RequestMethod.POST)
  public ResponseEntity sendEmail(@Valid @RequestBody EmailMessage email) {    
    try {      
      String to = email.getTo();
      List<String> ccList = email.getCcList();
      String subject = email.getSubject();
      String message = email.getMessage();      
      NotificationService.sendEmail(to, ccList, subject, message);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      // TODO retry after some time? 
      logger.error("Error while sending an email to " + email.getTo());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }    
  }
  

  @RequestMapping(name="/notify/sms", method=RequestMethod.POST)
  public ResponseEntity sendSMS(@Valid @RequestBody SMSMessage sms) {
    try {
      String mobile = sms.getMobileNo();
      String message = sms.getMessage();
      NotificationService.sendSMS(mobile, message);
      return ResponseEntity.ok("");
    }catch(Exception ex) {
      logger.error("Error while sending sms to " + sms.getMobileNo());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
