package com.ixee.datastruct;

import java.util.Iterator;

public class RBKeyIterator<K extends Comparable<K>, Node extends RBTreeNode<K, Node>, Tree extends BaseRBTree<K, Node, Tree>> implements Iterator<K>{
  Tree tree;
  Node curr;
  public RBKeyIterator(Tree t){
    tree = t;
    curr = tree.minNode();
  }
  public K next(){
    K ret = curr.getKey();
    curr = tree.succ(curr);
    return ret;
  }
  public boolean hasNext(){
    //The last node is actually a sentinel, which by definiton has key and value set to null.
    //so test against that instead.
    return (curr != null && curr.getKey() != null);
  }
  public void remove(){
    //ugh. Not right now.
    throw new RuntimeException("Fill this in");
  }
  public void print(){
    System.out.println(curr);
  }
}
