package com.ixee.datastruct;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class IntervalNode<K extends Comparable<K>, V> extends RBTreeNode<K, IntervalNode<K, V>> {
  protected Interval<K> range = NilInterval.value;
  protected Interval<K> overallRange = NilInterval.value;

  ArrayList<Pair<Interval<K>, V>> pairs;

  protected void setKey(K key) {
    super.setKey(key);
    range = new RealInterval<K>(key, key);
  }

  protected void replaceWith(IntervalNode<K, V> other) {
    setKey(other.getKey());
    setPairs(other.getPairs());
  }

  public int pairCount() {
    return pairs.size();
  }

  public boolean contains(K end, V value) {
    /*
     * end is passed so in the future when we order doubles
     * by end we can quickly find the thing matching value
     *
     * for now, just try finding a pair with _2 == value and _1.end == end
     */
    for (Pair<Interval<K>, V> pair : pairs) {
      if (pair._2() == value) {
        if (((RealInterval<K>)pair._1()).end == end) {
          return true;
        }
      }
    }

    return false;
  }

  public ArrayList<V> getForRangesContaining(K end, ArrayList<V> ret) {
    if (key.compareTo(end) > 0) return ret;

    for (int i = 0; i < pairs.size(); i++) {
      Pair<Interval<K>, V> pair = pairs.get(i);

      if (((RealInterval<K>)pair._1()).end.compareTo(end) > 0) {
        ret.add(pair._2());
      }
    }

    return ret;
  }

  public ArrayList<V> getForRangesBelow(K end) {
    return getForRangesBelow(end, new ArrayList<V>());
  }

  public ArrayList<V> getForRangesAtOrBelow(K end, ArrayList<V> ret) {
    if (key.compareTo(end) > 0) return ret;

    for (int i = 0; i < pairs.size(); i++) {
      Pair<Interval<K>, V> pair = pairs.get(i);

      if (((RealInterval<K>)pair._1()).end.compareTo(end) <= 0) {
        ret.add(pair._2());
      }
    }

    return ret;
  }

  public ArrayList<V> getForRangesBelow(K end, ArrayList<V> ret) {
    if (key.compareTo(end) > 0) return ret;

    for (int i = 0; i < pairs.size(); i++) {
      Pair<Interval<K>, V> pair = pairs.get(i);
      if (((RealInterval<K>)pair._1()).end.compareTo(end) < 0) {
        ret.add(pair._2());
      }
    }

    return ret;
  }

  public ArrayList<V> getEndingAt(K end, ArrayList<V> list) {
    for (int i = 0; i < pairs.size(); i++) {
      Pair<Interval<K>, V> pair = pairs.get(i);
      if (((RealInterval<K>)pair._1()).end.compareTo(end) == 0) {
        list.add(pair._2());
      }
    }

    return list;
  }

  public void remove(K high, V value) {
    Iterator<Pair<Interval<K>, V>> pairIterator = pairs.iterator();

    while(pairIterator.hasNext()) {
      Pair<Interval<K>, V> curr = pairIterator.next();

      RealInterval<K> interval = (RealInterval<K>)curr._1();

      if (interval.end.equals(high) && curr._2().equals(value)) pairIterator.remove();
    }
  }

  public void setLeft(IntervalNode<K, V> l) {
    left = l;
    recalcMax();
  }

  public void setRight(IntervalNode<K, V> r) {
    right = r;
    recalcMax();
  }

  public void removeAt(K end) {
    Iterator<Pair<Interval<K>, V>> pairIterator = pairs.iterator();

    while(pairIterator.hasNext()) {
      Pair<Interval<K>, V> curr = pairIterator.next();

      RealInterval<K> range = (RealInterval<K>)curr._1();

      if (range.end.compareTo(end) == 0) {
        pairIterator.remove();
      }
    }
  }

  public void removeBelow(K end) {
    Iterator<Pair<Interval<K>, V>> pairIterator = pairs.iterator();

    while(pairIterator.hasNext()) {
      Pair<Interval<K>, V> curr = pairIterator.next();

      RealInterval<K> range = (RealInterval<K>)curr._1();

      if (range.end.compareTo(end) < 0) {
        pairIterator.remove();
      }
    }
  }

  public void removeForRangesAtOrAbove(K end) {
    Iterator<Pair<Interval<K>, V>> pairIterator = pairs.iterator();

    while(pairIterator.hasNext()) {
      Pair<Interval<K>, V> curr = pairIterator.next();

      RealInterval<K> range = (RealInterval<K>)curr._1();

      if (range.end.compareTo(end) >= 0) {
        pairIterator.remove();
      }
    }
  }

  protected void addValue(K end, V value) {
    addPair(new Pair(new RealInterval(key, end), value));
  }

  protected void addPair(Pair<Interval<K>, V> pair) {
    pairs.add(pair);

    K pairMax = ((RealInterval<K>)pair._1()).end;

    if (overallRange instanceof NilInterval || pairMax.compareTo(((RealInterval<K>)overallRange).end) > 0) {
      range = new RealInterval<K>(key, pairMax);
      recalcMax();
    }
  }
  protected ArrayList<Pair<Interval<K>, V>> getPairs() { return pairs; }
  protected void setPairs(ArrayList<Pair<Interval<K>, V>> newPairs) {
    pairs.clear();
    pairs.addAll(newPairs);

    K max = key;

    for (Pair<Interval<K>, V> pair : pairs) {
      K pairMax = ((RealInterval<K>)pair._1()).end;
      if (pairMax.compareTo(max) > 0) {
        max = pairMax;
      }
    }

    range = new RealInterval<K>(key, max);
    recalcMax();
  }

  protected Interval<K> getRange() { return overallRange; }
  protected Interval<K> getSelfRange() { return range; }

  /*
   * Check this out..
   * Can we roll this and setObject into a single
   * setValues?
   */

  public void setRange(Interval<K> newRange) {
    if (newRange instanceof NilInterval) throw new IllegalStateException("Set range to nil range, invalid");

    RealInterval<K> realRange = (RealInterval<K>)newRange;
    setRange(realRange.start, realRange.end);
  }

  public void setRange(K start, K end) {
    range = new RealInterval<K>(start, end);
    overallRange = range;
    key = start;
    recalcMax();
  }

  protected K max() {
    if (overallRange instanceof NilInterval) return null;

    return ((RealInterval<K>)overallRange).end;
  }
  protected void recalcMax() {
    if (sentinel) return;

    K subtreeMax = ((RealInterval<K>)range).end;

    if(left.max() != null) {
      subtreeMax = subtreeMax.compareTo(left.max()) > 0 ? subtreeMax : left.max();
    }

    if(right.max() != null) {
      subtreeMax = subtreeMax.compareTo(right.max()) > 0 ? subtreeMax : right.max();
    }

    overallRange = new RealInterval<K>(key, subtreeMax);

    if (parent != null) parent.recalcMax();
  }

  protected String stringifyValue() {
    String ret = "[";

    for (Pair<Interval<K>, V> pair : pairs) {
      ret = ret + pair.toString() + ", ";
    }

    ret = ret + "]";

    return ret;
  }
}
