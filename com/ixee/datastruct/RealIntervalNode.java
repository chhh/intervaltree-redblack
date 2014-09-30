package com.ixee.datastruct;

import java.util.ArrayList;
import java.util.Iterator;

public class RealIntervalNode<K extends Comparable<K>, V> extends IntervalNode<K, V> {

  public RealIntervalNode(){
  }

  protected void specificInit(){
    range = NilInterval.value;
    overallRange = NilInterval.value;
    /*
    if (NilIntervalNode.value == null) {
      //this is to hack around .value not being initialized in time...
      //this is kinda broke as hell.
      NilIntervalNode.value = new NilIntervalNode();
    }
    */
    right = NilIntervalNode.value;
    left = NilIntervalNode.value;
    if (pairs == null) {
      //because when constructing new RealIntervalNode, init is called, which
      // calls specificInit
      //
      // before pairs is set to non-null.
      pairs = new ArrayList<Pair<Interval<K>, V>>();
    } else {
      pairs.clear();
    }
  }
}
