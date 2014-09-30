package com.ixee.datastruct;

public class PLLNode<T> {
  PLLNode<T> next;
  PLLNode<T> prev;
  T value;

  public PLLNode<T> pop(){
    return this;
  }
  public void push(PLLNode<T> n){
    n.setNext(this);
  }
  public void setNext(PLLNode<T> n){
    next = n;
  }
  public void setPrev(PLLNode<T> p){
    prev = p;
  }
  public PLLNode<T> next(){
    return next;
  }
  public T value() {
    return value;
  }
  public void setValue(T newValue) {
    value = newValue;
  }
}

