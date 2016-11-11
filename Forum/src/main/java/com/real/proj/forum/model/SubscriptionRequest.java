package com.real.proj.forum.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SubscriptionRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  String id;

  User subscriber;
  SubscriptionState request_state;
  String comment;
  Date created_date;
  Date action_date;

  public SubscriptionRequest(User subscriber) {
    this.subscriber = subscriber;
    created_date = new Date();
  }

  public boolean isAccepted() {
    return this.request_state == SubscriptionState.ACCEPTED;
  }

  public void acceptRequest(String comment) {
    this.request_state = SubscriptionState.ACCEPTED;
    this.comment = (comment == null) ? "" : comment;
    action_date = new Date();
  }

  public void rejectRequest(String comment) {
    this.request_state = SubscriptionState.REJECTED;
    this.comment = (comment == null) ? "" : comment;
    action_date = new Date();
  }

}
