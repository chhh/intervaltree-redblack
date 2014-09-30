package com.ixee.datastruct;

import java.util.Iterator;
import java.util.ArrayList;

/*
 * Fun fact, the easiest way to make the most unbalanced tree
 * is to insert 2^n values in ascending or decending order.
 *
 * To balance the tree, remove all evens in ascending/decending order.
 */

public class IntervalTree<K extends Comparable<K>, V> extends
  BaseRBTree<
    K,
    IntervalNode<K, V>,
    IntervalTree<K, V>
  > {

  ArrayList<K> keyList = new ArrayList<K>();

  public IntervalTree(boolean manage){
    super(manage);
  }

  protected IntervalNode<K, V> newNode() {
    return new RealIntervalNode<K, V>();
  }

  protected boolean contains(K start, K end, V value) {
    return getNode(start).contains(end, value);
  }

  public ArrayList<V> getForRangeContaining(K key) {
    return getForRangesContaining(key, new ArrayList<V>(), new Counter());
  }

  public Counter __countedPairsContaining(K key, ArrayList<V> ret) {
    Counter c = new Counter();
    getForRangesContaining(key, ret, c);
    return c;
  }

  public ArrayList<V> getForRangesContaining(K key, ArrayList<V> ret, Counter c) {
    return getForRangesContaining(root, key, ret, c);
  }

  private ArrayList<V> getForRangesContaining(IntervalNode<K, V> node, K key, ArrayList<V> ret, Counter c) {
    c.count();

    if (node.getRange().contains(key)) {
      ret = node.getForRangesContaining(key, ret);
    }

    if (node.left() != sentinel && node.left().max().compareTo(key) >= 0) {
      getForRangesContaining(node.left(), key, ret, c);
    }

    if (node.right() != sentinel && node.right().getKey().compareTo(key) <= 0) {
      getForRangesContaining(node.right(), key, ret, c);
    }

    return ret;
  }

  public void removeContaining(K key) {
    keyList.clear();
    removeContaining(root, key, keyList);
    removeKeys(keyList);
  }

  private void removeContaining(IntervalNode<K, V> node, K key, ArrayList<K> ret) {
    if (node.getRange().contains(key)) {
      node.removeForRangesAtOrAbove(key);
      if (node.pairCount() == 0) {
        ret.add(node.getKey());
      }
    }

    if (node.left() != sentinel && node.left().max().compareTo(key) >= 0) {
      removeContaining(node.left(), key, ret);
    }

    if (node.right() != sentinel && node.right().getKey().compareTo(key) <= 0) {
      removeContaining(node.right(), key, ret);
    }
  }

  public ArrayList<V> getAt(K low, K high) {
    return getAt(low, high, new ArrayList<V>());
  }

  public void removeAt(K low, K high) {
    IntervalNode<K, V> n = getNode(low);

    n.removeAt(high);

    if (n.pairCount() == 0) {
      removeNode(n);
    }
  }

  public ArrayList<V> getAt(K low, K high, ArrayList<V> list) {
    IntervalNode<K, V> node = getNode(low);

    return node.getEndingAt(high, list);
  }

  public ArrayList<V> getAtOrBetween(K low, K high) {
    return getAtOrBetween(root, new RealInterval<K>(low, high), new ArrayList<V>(), new Counter());
  }

  public Counter __countedGetAtOrBetween(K low, K high, ArrayList<V> ret) {
    Counter c = new Counter();
    getAtOrBetween(root, new RealInterval<K>(low, high), ret, c);
    return c;
  }

  public ArrayList<V> getAtOrBetween(IntervalNode<K, V> n, RealInterval<K> range, ArrayList<V> inPairs, Counter c) {
    if (n.left() != sentinel) inPairs = getAtOrBetween(n.left(), range, inPairs, c);

    if (n.getKey().compareTo(range.start) >= 0) {
      inPairs = n.getForRangesAtOrBelow(range.end, inPairs);
      c.count();
    }

    if (n.right() != sentinel) inPairs = getAtOrBetween(n.right(), range, inPairs, c);

    return inPairs;
  }

  public ArrayList<V> getBetween(K low, K high) {
    return getBetween(low, high, new ArrayList<V>());
  }

  public ArrayList<V> getBetween(K low, K high, ArrayList<V> list) {
    return getBetween(root, low, high, list, new Counter());
  }

  public Counter __countedPairsBetween(K low, K high, ArrayList<V> list) {
    Counter c = new Counter();
    getBetween(root, low, high, list, c);
    return c;
  }

  private ArrayList<V> getBetween(IntervalNode<K, V> n, K low, K high, ArrayList<V> inPairs, Counter c) {
    if (n == sentinel) return inPairs;

    if (n.left() != sentinel && n.getKey().compareTo(low) >= 0) inPairs = getBetween(n.left(), low, high, inPairs, c);

    if (n.getKey().compareTo(low) > 0) {
      inPairs = n.getForRangesBelow(high, inPairs);
      c.count();
    }

    if (n.right() != sentinel && n.getKey().compareTo(high) <= 0) inPairs = getBetween(n.right(), low, high, inPairs, c);

    return inPairs;
  }

  public void removeBetween(K low, K high) {
    keyList.clear();
    removeBetween(root, low, high, keyList);
    removeKeys(keyList);
  }
 
  private void removeBetween(IntervalNode<K, V> n, K low, K high, ArrayList<K> removeBuffer) {
    if (n == sentinel) return;

    if (n.left() != sentinel && n.getKey().compareTo(low) >= 0) removeBetween(n, low, high, removeBuffer);

    if (n.getKey().compareTo(low) > 0) {
      n.removeBelow(low);
      if (n.pairCount() == 0) {
        removeBuffer.add(n.getKey());
      }
    }

    if (n.right() != sentinel && n.getKey().compareTo(high) <= 0) removeBetween(n, low, high, removeBuffer);
  }

  public void remove(K low, K high, V value) {
    IntervalNode<K, V> node = getNode(low);

    node.remove(high, value);

    if (node.pairCount() == 0) {
      removeNode(node);
    }
  }

  private void removeKeys(ArrayList<K> keys) {
    for (int i = 0; i < keys.size(); i++) {
      removeNode(getNode(keys.get(i)));
    }
  }

  public void insert(K start, K end, V value) {
    IntervalNode<K, V> inserted = insert(start);
    inserted.addValue(end, value);
  }

}

