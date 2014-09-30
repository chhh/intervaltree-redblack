package com.ixee.datastruct;

public class RBNode<K extends Comparable<K>, V> extends RBTreeNode<K, RBNode<K, V>> {
  protected V obj;

  protected void specificInit() {
    obj = null;
  }
  public V getObj(){
    return obj;
  }
  public void setObj(V o){
    obj = o;
  }
  protected String stringifyValue() {
    return obj.toString();
  }

  protected void replaceWith(RBNode<K, V> other) {
    setObj(other.getObj());
    setKey(other.getKey());
  }

  public void addValue(V value) {
    obj = value;
  }
}
