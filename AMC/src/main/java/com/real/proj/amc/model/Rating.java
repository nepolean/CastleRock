package com.real.proj.amc.model;

public class Rating {
  Star rating;
  int visitCount;

  Rating(Star rating, int frequency) {
    this.rating = rating;
    this.visitCount = frequency;
  }

  static enum Star {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);
    private int rating;

    Star(int rating) {
      this.rating = rating;
    }

    public int getRating() {
      return rating;
    }
  }

  public static void main(String[] args) {
    Star[] stars = Star.values();
    for (Star star : stars) {
      System.out.println(star + "    " + star.getRating());
    }
  }
}
