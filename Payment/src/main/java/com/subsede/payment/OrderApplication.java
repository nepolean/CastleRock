package com.subsede.payment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.real.order.service", "com.real.order.controller"})
@EnableMongoRepositories({"com.real.order.repository"})
@EnableAutoConfiguration
public class OrderApplication {
  private static Logger logger = LoggerFactory.getLogger(OrderApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(OrderApplication.class, args);
  }
}
