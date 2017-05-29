package com.real.proj.amc.model;

import java.util.Date;

public class Event implements Comparable<Event> {

  private String message;
  private Date created;
  private String user;

  public Event(String message, String postedBy) {
    this.message = message;
    this.created = new Date();
    this.user = postedBy;
  }

  @Override
  public int compareTo(Event otherEvent) {
    return this.created.compareTo(otherEvent.created);
  }

  @Override
  public String toString() {
    return "Event [message=" + message + ", created=" + created + ", user=" + user + "]";
  }

}
