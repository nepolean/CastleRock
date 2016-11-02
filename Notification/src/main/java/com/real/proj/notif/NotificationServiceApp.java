package com.real.proj.notif;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@SpringBootApplication
@ComponentScan
public class NotificationServiceApp {

  public static void main(String[] args) {
    SpringApplication.run(NotificationServiceApp.class, args);
  }
  
}
