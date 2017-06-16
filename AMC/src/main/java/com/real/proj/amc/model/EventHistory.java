package com.real.proj.amc.model;

import java.util.Date;

public class EventHistory implements Comparable<EventHistory> {

  private String message;
  private Date created;
  private String postedBy;

  public EventHistory(String message, String postedBy) {
    this.message = message;
    this.postedBy = postedBy;
    this.created = new Date();
  }

  @Override
  public int compareTo(EventHistory otherEvent) {
    return this.created.compareTo(otherEvent.created);
  }

  @Override
  public String toString() {
    return "Event [message=" + message + ", created=" + created + ", postedBy=" + postedBy + "]";
  }

}
