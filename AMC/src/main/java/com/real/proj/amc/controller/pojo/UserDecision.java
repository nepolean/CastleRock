package com.real.proj.amc.controller.pojo;

import javax.validation.constraints.NotNull;

public class UserDecision {

  @NotNull
  private String decision;
  @NotNull
  private String comments;

  public UserDecision() {

  }

  public UserDecision(String decision, String comments) {
    super();
    setDecision(decision);
    setComments(comments);
  }

  public String getDecision() {
    return decision;
  }

  public void setDecision(String decision) {
    if (!decision.matches("yes|no"))
      throw new IllegalArgumentException("Invalid decision selected. Choose yes/no instead.");
    this.decision = decision;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

}
