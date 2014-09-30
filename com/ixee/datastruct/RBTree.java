package com.ixee.datastruct;

import java.util.*;

public class RBTree<K extends Comparable<K>, V> extends
  BaseRBTree<
    K,
    RBNode<K, V>,
    RBTree<K, V>
  > {

  public RBTree(boolean manage) {
    super(manage);
  }

  public static void main(String[] args){
    RBTree<Integer, String> tree = new RBTree<Integer, String>(true);

    for(int i=0;i<10;i++){
      tree.insert(i, Integer.toString(i));
      tree.print();
    }
    System.out.println("Inserted 10 entries");

    System.out.println("1 maps to "+tree.get(1));
    tree.print();
    for(int i=1;i<10;i*=2){
      System.out.println("Removing "+i);
      tree.remove(i);
      tree.print();
    }

    for(int i = 100; i < 106; i++) {
      tree.insert(i, i + "");
      System.out.println("Inserted " + i);
      tree.print();
      System.out.println(tree.minNode());
    }
    System.out.println("Inserted 102");
    tree.print();
    System.out.println(tree.getMax());
    System.out.println(tree.getMin());
    System.out.println("node with key = 101 is " + tree.getNode(101));
    System.out.println("predecessor of 101 is " + tree.pred(tree.getNode(101)));
    System.out.println(" should be 100");
    System.out.println("successor of 102 is " + tree.succ(tree.getNode(102)));
    System.out.println(" should be 103");
  //  while(tree.getMin() != null){
  //    System.out.println("Removing "+tree.getMin());
  //    tree.removeMin();
  //    tree.print();
  //  }
  }

  public void remove(K key) {
    removeNode(getNode(key));
  }

  public void insert(K key, V value) {
    RBNode<K, V> result = insert(key);
    result.addValue(value);
  }

  public V removeMin(){
    RBNode<K, V> n = minNode();
    if(n != sentinel){
      remove(n.getKey());
    }
    return n.getObj();
  }
  public V getMin(){
    return minNode().getObj();
  }
  public V get(K key) {
    return getNode(key).getObj();
  }
  public V removeMax(){
    RBNode<K, V> n = maxNode();
    if(n != sentinel){
      remove(n.getKey());
    }
    return n.getObj();
  }
  public V getMax(){
    return maxNode().getObj();
  }

  public Iterator<K> keyIterator(){
    return new RBKeyIterator<K, RBNode<K, V>, RBTree<K, V>>(this);
  }
  public Iterator<V> valIterator(){
    return new RBValIterator<K, V>(this);
  }

  protected RBNode<K, V> newNode() {
    return new RBNode<K, V>();
  }
}
