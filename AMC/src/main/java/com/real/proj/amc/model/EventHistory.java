package com.real.proj.amc.model;

import java.util.Date;

public class EventHistory implements Comparable<EventHistory> {

  private String message;
  private Date created;
  private String postedBy;
  private String role;
  private String comments;

  public EventHistory(String message, String postedBy, String role, String comments) {
    this.message = message;
    this.postedBy = postedBy;
    this.role = role;
    this.comments = comments;
    this.created = new Date();
  }

  @Override
  public int compareTo(EventHistory otherEvent) {
    return this.created.compareTo(otherEvent.created);
  }

  @Override
  public String toString() {
    return "EventHistory [message=" + message + ", created=" + created + ", postedBy=" + postedBy + "[" + role
        + "], comments=" + comments + "]";
  }

}
