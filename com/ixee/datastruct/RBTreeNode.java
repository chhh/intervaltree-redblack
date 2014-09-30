package com.ixee.datastruct;

public abstract class RBTreeNode<K extends Comparable<K>, NODE extends RBTreeNode<K, NODE>> extends PLLNode<NODE> {
  protected K key;
  NODE parent, left, right;
  boolean sentinel = false;

  NodeColor col = NodeColor.RED;

  public RBTreeNode(){
    init();
  }

  protected NODE sentinel() {
    left = (NODE)this;
    right = (NODE)this;
    parent = (NODE)this;
    sentinel = true;
    col = NodeColor.BLACK;
    return (NODE)this;
  }

  protected abstract String stringifyValue();

  abstract void specificInit();

  abstract void replaceWith(NODE other);

  public void init(){
    key = null;
    parent = null;
    right = null;
    left = null;
    specificInit();
  }

  public NodeColor getColor(){
    return col;
  }
  public void setColor(NodeColor color){
    col = color;
  }
  public K getKey(){
    return key;
  }
  protected void setKey(K k){
    key = k;
  }
  public NODE parent(){
    return parent;
  }
  public void setParent(NODE p){
    parent = p;
  }
  public NODE left(){
    return left;
  }
  public NODE leftmost() {
    NODE curr = (NODE)this;
    NODE left = curr.left();

    while(left.left() != left) {
      curr = left;
      left = left.left();
    }

    return curr;
  }

  public NODE rightmost() {
    NODE curr = (NODE)this;
    NODE right = curr.right();

    while(right.right() != right) {
      curr = right;
      right = right.right();
    }

    return curr;
  }
  public void setLeft(NODE l){
    left = l;
  }
  public NODE right(){
    return right;
  }
  public void setRight(NODE r){
    right = r;
  }
  public NODE grandparent(){
    return parent().parent();
  }
  public NODE uncle(){
    NODE grand = grandparent();
    if(grand == null){
      return null;
    }else{
      return parent.sibling();
    }
  }
  public NODE sibling(){
    if(parent.left() == this){
      return parent.right();
    }else{
      return parent.left();
    }
  }

  public String toString() {
    return "node { "+ key+": "+stringifyValue()+" p="+(parent == null ? null : parent.getKey())+", gp="+( ( (parent == null) || (grandparent() == null) ) ? null : grandparent().getKey()) + " }";
  }

  protected boolean isSentinel() {
    return sentinel;
  }

  protected boolean isLeaf() {
    return left().isSentinel() && right().isSentinel();
  }

  protected int __debug_depth() {
    if (parent().isSentinel()) {
      return 0;
    } else {
      return parent.__debug_depth() + 1;
    }
  }

  public boolean parentOf(NODE node) {
    NODE curr = node.parent();
    while (!curr.isSentinel()) {
      if (curr == this) return true;
      curr = curr.parent();
    }
    return false;
  }

  protected boolean isLeftChild() {
    return this == parent().left();
  }

  protected boolean isRightChild() {
    return this == parent().right();
  }

  protected NODE followKey(K key) {
    int comparison = key.compareTo(this.key);
    
    if (comparison == 0) {
      return (NODE)this;
    } else if (comparison > 0) {
      return right;
    } else/*if (comparison < 0)*/{
      return left;
    }
  }
}
