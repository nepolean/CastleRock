package com.real.proj.amc.model;

import java.util.Date;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class TimeLine<T> {

  private static final int PADDING = 100;
  private Stack<History<T>> history = new Stack<History<T>>();

  public TimeLine() {

  }

  public void addToHistory(T obj, Date on) {
    validate(obj, on);
    if (history.size() > 0) {
      History<T> recentValue = history.pop();
      if (on.before(recentValue.getFrom()))
        throw new IllegalArgumentException("Given date conflicts with the existing date");
      recentValue.setTo(on);
      history.push(recentValue);
    }
    History<T> newValue = new History<T>(obj);
    newValue.setFrom(on);
    history.push(newValue);
  }

  private void validate(T obj, Date on) {
    if (obj == null || on == null)
      throw new IllegalArgumentException("Null data is passed");
    if (on.getTime() + PADDING < System.currentTimeMillis())
      throw new IllegalArgumentException("Given date falls in the past");
  }

  public T getCurrentValue() {
    if (history == null)
      return null;

    T value = null;
    for (History<T> t : history) {
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

  public T getValueForDate(Date myDate) {
    if (myDate == null)
      throw new IllegalArgumentException("Null date is passed");
    T value = null;
    for (History<T> t : this.history) {
      if (myDate.after(t.getFrom())) {
        value = t.getValue();
      }
    }
    return value;
  }

  public int size() {
    return this.history.size();
  }

  public Set<T> asList() {
    Set<T> output = new TreeSet<T>();
    history.forEach(value -> {
      output.add(value.getValue());
    });
    return output;
  }

  @Override
  public String toString() {
    return "History [history=" + history + "]";
  }

  static class History<T> {
    T value;
    Date from;
    Date to;

    History(T value) {
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

}
