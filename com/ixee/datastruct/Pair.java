package com.ixee.datastruct;

class Pair<T, U> extends PLLNode<Pair<T, U>> {
  T _1;
  U _2;

  public Pair(T t, U u) {
    _1 = t;
    _2 = u;
  }

  public T _1() { return _1; }
  public U _2() { return _2; }

  public String toString() {
    return "{1: " + _1 + ", 2: '" + _2 + "'}";
  }

  public void cloneAs(Pair<T, U> other) {
    _1 = other._1();
    _2 = other._2();
  }
}
