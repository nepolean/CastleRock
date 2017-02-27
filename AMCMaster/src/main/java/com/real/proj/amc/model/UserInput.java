package com.real.proj.amc.model;

import java.util.HashMap;
import java.util.Map;

public class UserInput<K, E> {

  Map<K, E> arguments;

  public E get(K argument) {
    if (arguments == null)
      return null;
    return arguments.get(argument);
  }

  public void add(K key, E value) {
    if (this.arguments == null)
      arguments = new HashMap<K, E>();
    this.arguments.put(key, value);
  }

}
