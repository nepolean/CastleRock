package com.real.proj.notif;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class NotificationServiceApp {

  public static void main(String[] args) {
    SpringApplication.run(NotificationServiceApp.class, args);
  }
  
}
