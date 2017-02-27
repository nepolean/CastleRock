package com.real.proj.amc.model;

import java.util.Date;
import java.util.Stack;

public class History<T> {

  private static final int PADDING = 100;
  Stack<TimeLine<T>> history = new Stack<TimeLine<T>>();

  public History() {

  }

  public void addToHistory(T obj, Date on) {
    if (obj == null || on == null)
      throw new IllegalArgumentException("Null data is passed");
    if (on.getTime() + PADDING < System.currentTimeMillis())
      throw new IllegalArgumentException("Given date falls in the past");
    if (history.size() > 0) {
      TimeLine<T> recentValue = history.pop();
      if (on.before(recentValue.getFrom()))
        throw new IllegalArgumentException("Given date conflicts with the existing date");
      recentValue.setTo(on);
      history.push(recentValue);
    }
    TimeLine<T> newValue = new TimeLine<T>(obj);
    newValue.setFrom(on);
    history.push(newValue);
  }

  public T getCurrentValue() {
    if (history == null)
      return null;

    T value = null;
    for (TimeLine<T> t : history) {
      // valid range = >from && <to
      long now = System.currentTimeMillis();
      long to = t.getTo() == null ? Long.MAX_VALUE : t.getTo().getTime();
      if (now >= t.getFrom().getTime() && now < to) {
        value = t.getValue();
        break;
      }
    }
    return value;
  }

  @Override
  public String toString() {
    return "History [history=" + history + "]";
  }

  static class TimeLine<T> {
    T value;
    Date from;
    Date to;

    TimeLine(T value) {
      this.value = value;
    }

    public T getValue() {
      return value;
    }

    public void setValue(T value) {
      this.value = value;
    }

    public Date getFrom() {
      return from;
    }

    public void setFrom(Date from) {
      this.from = from;
    }

    public Date getTo() {
      return to;
    }

    public void setTo(Date to) {
      this.to = to;
    }

    @Override
    public String toString() {
      return "TimeLine [value=" + value + ", from=" + from + ", to=" + to + "]";
    }

  }

  public T getValueForDate(Date myDate) {
    if (myDate == null)
      throw new IllegalArgumentException("Null date is passed");
    T value = null;
    for (TimeLine<T> t : this.history) {
      if (myDate.after(t.getFrom())) {
        value = t.getValue();
      }
    }
    return value;
  }

}
