package com.real.proj.amc.model;

import java.util.List;
import java.util.Stack;

public class Quotation {

  double totalAmount;
  double taxAmount;
  double discount;
  double finalAmount;
  List<String> comments;

  public static void main(String[] args) {
    String input = "<h1>Had<h1>Public</h1515></h1>";
    Stack<Character> chars = new Stack<Character>();
    Stack<String> tags = new Stack<String>();
    Stack<String> text = new Stack<String>();
    char[] ch = input.toCharArray();
    for (char c : ch) {
      if (c == '>') {
        // System.out.println(chars.toString());
        String currTag = convertToString(chars);
        if (currTag != null && currTag.length() > 0) {
          if (tags.size() > 0) {
            String content = null;
            String prevTag = null;
            try{
              prevTag = tags.pop();
              content = text.pop();
            } catch(Exception ex){}
            if ()
            
          } else
            tags.push(currTag);
        }
      } else if (c == '<') {
        String str = convertToString(chars);
        if (str != null && str.length() > 0)
          text.push(str);
      } else {
        chars.push(c);
      }
    }
  }

  private static String convertToString(Stack<Character> chars) {
    if (chars.size() == 0)
      return null;
    char[] tagA = new char[chars.size()];
    for (int i = tagA.length - 1; i >= 0; i--) {
      tagA[i] = chars.pop();
    }
    String tag = new String(tagA);
    System.out.println(tag);
    return tag;
  }
}
