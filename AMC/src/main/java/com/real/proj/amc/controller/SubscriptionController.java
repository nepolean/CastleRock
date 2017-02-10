package com.real.proj.amc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.amc.model.Rating;
import com.real.proj.amc.service.SubscriptionService;

@RestController
public class SubscriptionController {

  private SubscriptionService service;

  @Autowired
  public void setSubscriptionService(SubscriptionService service) {
    this.service = service;
  }

  @RequestMapping(path = "/subscription/status", method = RequestMethod.GET)
  public String getStatus() {
    return service.getStatus("5899dec21c4c8f7e1e797ba2");
  }

  @RequestMapping(path = "/subscription/start", method = RequestMethod.GET)
  public void start() {
    // service.start();
  }

  @RequestMapping(path = "/subscription/rate", method = RequestMethod.POST)
  public void rate() {
    service.rate("5899dec21c4c8f7e1e797ba2", Rating.ONE);
  }

  @RequestMapping(path = "/subscription/pay", method = RequestMethod.POST)
  public void pay() {
    service.pay();
  }

  @RequestMapping(path = "/subscription/subscribe", method = RequestMethod.POST)
  public void subscribe() {
    service.subscribe();
  }
}
