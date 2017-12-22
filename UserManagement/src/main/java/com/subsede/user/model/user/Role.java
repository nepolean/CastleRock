package com.subsede.user.model.user;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Roles")
public class Role {

  private String id;

  @NotNull
  private String name;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Role(String name) {
    this.name = name;
  }

}