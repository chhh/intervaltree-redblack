package com.ixee.datastruct;

public class NilInterval<K extends Comparable<K>> implements Interval<K> {
  public static final Interval value = new NilInterval();

  private NilInterval() { }

  public boolean overlapsWith(Interval<K> other) {
    return false;
  }

  public String toString() {
    return "NilInterval";
  }

  public boolean equals(Interval<K> interval) {
    return false;
  }

  public Interval<K> union(Interval<K> other) {
    return other;
  }

  public boolean contains(K point) {
    return false;
  }
}
