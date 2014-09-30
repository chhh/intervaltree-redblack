package com.ixee.datastruct;

public class RealInterval<K extends Comparable<K>> implements Interval<K> {
  public final K start;
  public final K end;

  public RealInterval(K start, K end) {
    this.start = start;
    this.end = end;
  }

  public boolean overlapsWith(Interval<K> other) {
    if (other instanceof NilInterval) return false;

    RealInterval<K> realOther = (RealInterval<K>)other;
    return start.compareTo(realOther.end) <= 0 &&
           end.compareTo(realOther.start) >= 0;
  }

  public String toString() {
    return "{" + start + ":" + end + "}";
  }

  public boolean equals(Interval<K> other) {
    if (other instanceof NilInterval) return false;

    RealInterval<K> realOther = (RealInterval<K>)other;
    return start.compareTo(realOther.start) == 0 &&
           end.compareTo(realOther.end) == 0;
  }

  public int hashCode() {
    return start.hashCode() ^ end.hashCode();
  }

  public Interval<K> union(Interval<K> other) {
    if(other instanceof NilInterval) return this;

    RealInterval<K> realOther = (RealInterval<K>)other;

    K min = start.compareTo(realOther.start) < 0 ? start : realOther.start;
    K max = end.compareTo(realOther.end) > 0 ? end : realOther.end;

    return new RealInterval(min, max);
  }

  public boolean contains(K point) {
    return start.compareTo(point) <= 0 && end.compareTo(point) >= 0;
  }
}
