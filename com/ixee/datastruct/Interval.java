package com.ixee.datastruct;

public interface Interval<K extends Comparable<K>> {
  public boolean overlapsWith(Interval<K> other);
  public boolean equals(Interval<K> other);
  public Interval<K> union(Interval<K> other);
  public boolean contains(K point);
}
