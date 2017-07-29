package com.real.proj.amc.controller;

import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ServiceJobAdminController {

  @RequestMapping(produces = { "application/pdf" })
  public ResponseEntity<byte[]> testPDFDOwnload() {
    byte[] response = "Hello, I will be saved as pdf on client device.".getBytes();
    response = Base64.getEncoder().encode(response);
    System.out.println(new String(response));
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("application/pdf"));
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=myfile.pdf");
    return new ResponseEntity<byte[]>(response, headers, HttpStatus.OK);
  }

}
