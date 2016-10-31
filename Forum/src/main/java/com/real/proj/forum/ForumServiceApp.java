package com.real.proj.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan (basePackages = "com.real.proj.forum.controller")
public class ForumServiceApp {
  
  public static void main(String[] args) {
    ConfigurableApplicationContext ctx = SpringApplication.run(ForumServiceApp.class, args);
    
    String[] beans = ctx.getBeanDefinitionNames();
    for (String bean : beans)
      System.out.println(bean);
  }

}
