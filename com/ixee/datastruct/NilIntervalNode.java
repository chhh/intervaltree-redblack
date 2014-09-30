package com.ixee.datastruct;

public class NilIntervalNode<K extends Comparable<K>, V> extends IntervalNode<K, V> {

  public static NilIntervalNode value = new NilIntervalNode();

  protected NilIntervalNode() {
  }

  public NodeColor getColor(){
    return NodeColor.BLACK;
  }
  public void setColor(NodeColor color){
    throw new IllegalStateException("Can't set values on NilIntervalNode");
  }
  public K getKey(){
    return null;
  }
  
  protected void specificInit() {
  }

  protected K max() {
    return null;
  }
  protected Interval<K> getRange() {
    return NilInterval.value;
  }

  protected Interval<K> getSelfRange() {
    return NilInterval.value;
  }

  /*
   * Check this out..
   * Can we roll this and setObject into a single
   * setValues?
   */

  public void setRange(Interval<K> newRange) {
    throw new IllegalStateException("Can't set values on NilIntervalNode");
  }

  public void setRange(K start, K end) {
    throw new IllegalStateException("Can't set values on NilIntervalNode");
  }
  public V getObj(){
    return null;
  }
  public void setObj(V o){
    throw new IllegalStateException("Can't set values on NilIntervalNode");
  }
  public IntervalNode<K, V> parent(){
    return this;
  }
  public void setParent(IntervalNode<K, V> p){
    throw new IllegalStateException("Can't set values on NilIntervalNode");
  }
  public IntervalNode<K, V> left(){
    return this;
  }
  public IntervalNode<K, V> leftmost() {
    return this;
  }

  protected void recalcMax() {

  }

  public IntervalNode<K, V> rightmost() {
    return this;
  }
  public void setLeft(IntervalNode<K, V> l){
    throw new IllegalStateException("Can't set values on NilIntervalNode");
  }
  public IntervalNode<K, V> right(){
    return this;
  }
  public void setRight(IntervalNode<K, V> r){
    throw new IllegalStateException("Can't set values on NilIntervalNode");
  }
  public IntervalNode<K, V> grandparent(){
    return this;
  }
  public IntervalNode<K, V> uncle(){
    return this;
  }
  public IntervalNode<K, V> sibling(){
    return this;
  }
  public String toString(){
    return "NilIntervalNode";
  }

  protected boolean isSentinel() {
    return true;
  }

  protected boolean isLeaf() {
    return left().isSentinel() && right().isSentinel();
  }

  protected int __debug_depth() {
    return -1;
  }

  public boolean parentOf(IntervalNode node) {
    return false;
  }

  protected boolean isLeftChild() {
    return false;
  }

  protected boolean isRightChild() {
    return false;
  }

  protected IntervalNode<K, V> followKey(K key) {
    return this;
  }
}
