package com.ixee.datastruct;

import java.util.Iterator;

public class RBValIterator<K extends Comparable<K>, V> implements Iterator<V>{
  RBTree<K, V> tree;
  RBNode<K, V> curr;
  public RBValIterator(RBTree<K, V> t){
    tree = t;
    curr = tree.minNode();
  }
  public V next(){
    V ret = curr.getObj();
    curr = tree.succ(curr);
    return ret;
  }
  public boolean hasNext(){
    return (curr != null && curr.getObj() != null);
  }
  public void remove(){
    //ugh. Not right now.
    throw new RuntimeException();
  }
}
