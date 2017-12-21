package com.subsede.notification.util;

import java.util.List;

public class ValidationHelper {

  public static boolean isEmpty(List<String> to) {
    return null == to || 0 == to.size();
  }

  public static boolean isEmpty(String message) {
    return null == message || 0 == message.length();
  }

  public static String sanitize(String input) {
    return (null == input) ? "" : input;
  }

}
