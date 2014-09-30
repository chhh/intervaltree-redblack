package com.ixee.datastruct;

import java.util.Iterator;
import java.util.ArrayList;

/*
 * Fun fact, the easiest way to make the most unbalanced tree
 * is to insert 2^n values in ascending or decending order.
 *
 * To balance the tree, remove all evens in ascending/decending order.
 */

public abstract class BaseRBTree<
  K extends Comparable<K>,
  TreeNode extends RBTreeNode<K, TreeNode>,
  Tree extends BaseRBTree<K, TreeNode, Tree>
> {
  TreeNode sentinel;
  TreeNode root;
  boolean manageMemory = false;

  TreeNode unusedNodes = null;

  int storedNodes = 0;

  int size = 0;

  BaseRBTree(boolean manage){
    manageMemory = manage;
    sentinel = newNode().sentinel();
    root = sentinel;
  }

  protected abstract TreeNode newNode();

  private TreeNode procureNode(){
    if(unusedNodes == null){
      if(storedNodes > 0){
      }
      return newNode();
    }else{
      storedNodes--;
      TreeNode ret = (TreeNode)unusedNodes.pop();
      unusedNodes = (TreeNode)ret.next();
      ret.init();
      return ret;
    }
  }
  private void storeNode(TreeNode n){
    if(unusedNodes != null){
      unusedNodes.push(n);
    }
    storedNodes++;
    unusedNodes = n;
  }
  protected TreeNode minNode(){
    return root.leftmost();
  }
  protected TreeNode maxNode(){
    return root.rightmost();
  }

  protected TreeNode getNode(K key){
    TreeNode curr = root;

    int compare;

    while(curr != sentinel){
      compare = key.compareTo(curr.getKey());

      switch(compare){
        case 0:
          return curr;
        case -1:
          curr = curr.left();
          break;
        case 1:
          curr = curr.right();
          break;
      }
    }
    return curr;
  }
  protected TreeNode pred(TreeNode n){
    TreeNode prev = n;
    TreeNode curr = n.left();

    if(curr != sentinel){
      return curr.rightmost();
    }else{
      curr = n;
      while(curr != root && curr != curr.parent().right()){
        curr = curr.parent();
      }
      return curr.parent();
    }
  }
  protected TreeNode succ(TreeNode n){
    TreeNode prev = n;
    TreeNode curr = n.right();

    if(curr != sentinel){
      return curr.leftmost();
    }else{
      curr = n;
      while(curr != root && curr != curr.parent().left()){
        curr = curr.parent();
      }
      return curr.parent();
    }
  }

  protected void removeNode(TreeNode toRepl){
    if(toRepl == sentinel){ return; }

    size--;

    TreeNode x, y;
    if(toRepl.left() == sentinel || toRepl.right() == sentinel){
      y = toRepl;
    }else{
      y = pred(toRepl);
    }

    if(y.left() != sentinel){
      x = y.left();
    }else{
      x = y.right();
    }

    x.setParent(y.parent());

    if(y.parent() != sentinel){
      if(y == y.parent().left()){
        y.parent().setLeft(x);
      }else{
        y.parent().setRight(x);
      }
    }else{
      root = x;
    }
    if(y != toRepl){
      toRepl.replaceWith(y);
    }

    if(color(y) == NodeColor.BLACK){
      delete_fixup(x);
    }

    if(manageMemory){
      storeNode(y);
    }
  }

  private void delete_fixup_colors(TreeNode n, TreeNode niece) {
    n.sibling().setColor(n.parent().getColor());
    n.parent().setColor(NodeColor.BLACK);
    niece.setColor(NodeColor.BLACK);
  }

  private boolean bothChildrenBlack(TreeNode node) {
    return color(node.left()) == NodeColor.BLACK &&
           color(node.right()) == NodeColor.BLACK;
  }

  private TreeNode delete_fixup_leftChild_red_sibling(TreeNode n) {
    n.sibling().setColor(NodeColor.BLACK);
    n.parent().setColor(NodeColor.RED);
    rotate_left(n.parent());
    return n.sibling();
  }

  private TreeNode delete_fixup_rightChild_red_sibling(TreeNode n) {
    n.sibling().setColor(NodeColor.BLACK);
    n.parent().setColor(NodeColor.RED);
    rotate_right(n.parent());
    return n.sibling();
  }

  public void delete_fixup(TreeNode n){
    while(n != root && color(n) == NodeColor.BLACK){
      TreeNode w = n.sibling();
      if(n.isLeftChild()){
        if(color(w) == NodeColor.RED){
          w = delete_fixup_leftChild_red_sibling(n);
        }
        if(bothChildrenBlack(w)){
          w.setColor(NodeColor.RED);
          n = n.parent();
        }else{
          if(color(w.right()) == NodeColor.BLACK){
            w.left().setColor(NodeColor.BLACK);
            w.setColor(NodeColor.RED);
            rotate_right(w);
            w = n.sibling();
          }
          delete_fixup_colors(n, w.right());
          rotate_left(n.parent());
          n = root;
        }
      }else{
        if(color(w) == NodeColor.RED){
          w = delete_fixup_rightChild_red_sibling(n);
        }
        if(bothChildrenBlack(w)){
          w.setColor(NodeColor.RED);
          n = n.parent();
        }else{
          if(color(w.left()) == NodeColor.BLACK){
            w.right().setColor(NodeColor.BLACK);
            w.setColor(NodeColor.RED);
            rotate_left(w);
            w = n.sibling();
          }
          delete_fixup_colors(n, w.left());
          rotate_right(n.parent());
          n = root;
        }
      }

    }
    n.setColor(NodeColor.BLACK);
  }

  protected TreeNode insert(K key){
    TreeNode curr = root;
    TreeNode prev = root.parent();

    while (curr != sentinel && curr != prev) {
      prev = curr;
      curr = curr.followKey(key);
    }

    if (curr == prev && curr != sentinel) {
      return curr;
    } else {
      return insertChild(key, prev);
    }
  }

  private TreeNode insertChild(K key, TreeNode parent) {
    TreeNode toAdd = procureNode();
    makeInsertable(toAdd, key);
    toAdd.setParent(parent);

    if(root == sentinel){
      root = toAdd;
    }else{
      insertChild(toAdd, parent);
    }

    size++;

    insert_fixup(toAdd);

    return toAdd;
  }

  private void insertChild(TreeNode node, TreeNode parent) {
    int comparison = node.getKey().compareTo(parent.getKey());

    if (comparison < 0) {
      parent.setLeft(node);
    } else if (comparison > 0) {
      parent.setRight(node);
    } else {
      throw new IllegalStateException("Attempted insertion of node with duplicate key");
    }
  }

  private void makeInsertable(TreeNode node, K key) {
    node.setKey(key);
    node.setColor(NodeColor.RED);
    node.setLeft(sentinel);
    node.setRight(sentinel);
  }

  private void insert_fixup_recolor(TreeNode n) {
    insert_fixup_recolorParents(n);
    n.uncle().setColor(NodeColor.BLACK);
  }

  private TreeNode insert_fixup_redUncle(TreeNode n) {
    insert_fixup_recolor(n);
    return n.grandparent();
  }

  private TreeNode insert_fixup_leftUncle(TreeNode n) {
    if(color(n.uncle()) == NodeColor.RED){
      n = insert_fixup_redUncle(n);
    }else{
      if(n.isRightChild()){
        n = n.parent();
        rotate_left(n);
      }

      insert_fixup_recolorParents(n);
      rotate_right(n.grandparent());
    }

    return n;
  }

  private TreeNode insert_fixup_rightUncle(TreeNode n) {
    if(color(n.uncle()) == NodeColor.RED){
      n = insert_fixup_redUncle(n);
    }else{
      if(n.isLeftChild()){
        n = n.parent();
        rotate_right(n);
      }

      insert_fixup_recolorParents(n);
      rotate_left(n.grandparent());
    }

    return n;
  }

  private void insert_fixup_recolorParents(TreeNode n) {
    n.parent().setColor(NodeColor.BLACK);
    n.grandparent().setColor(NodeColor.RED);
  }

  public void insert_fixup(TreeNode n){
    while(n != root && color(n.parent()) == NodeColor.RED){
      if(n.parent().isLeftChild()){
        n = insert_fixup_leftUncle(n);
      }else{
        n = insert_fixup_rightUncle(n);
      }
    }
    root.setColor(NodeColor.BLACK);
  }

  public NodeColor color(TreeNode n){
    return n.getColor();
  }

  public void rotate_right(TreeNode n){
    TreeNode l = n.left();
    n.setLeft(l.right());
    if(l.right() != sentinel){
      l.right().setParent(n);
    }
    replaceNode(n, l);
    l.setRight(n);
  }

  private void rotate_left(TreeNode n){
    TreeNode r = n.right();
    n.setRight(r.left());
    if(r.left() != sentinel){
      r.left().setParent(n);
    }
    replaceNode(n, r);
    r.setLeft(n);
  }

  private void replaceNode(TreeNode old, TreeNode n){
    if (old.parent() == sentinel) {
      root = n;
    } else {
      move_up(old, n);
    }
    if (n != sentinel) {
      n.setParent(old.parent());
    }
    if (old != sentinel) {
      old.setParent(n);
    }
  }

  private void move_up(TreeNode newParent, TreeNode child) {
    if (newParent == newParent.parent().left()){
      newParent.parent().setLeft(child);
    }else{
      newParent.parent().setRight(child);
    }
  }

  public void print() {
    printHelper(root, 0);
  }

  private void printHelper(TreeNode n, int indent) {
    if(n == sentinel) {
      System.out.print("<empty tree>");
      return;
    }
    if(n.right() != sentinel){
      printHelper(n.right(), indent + 1);
    }
    for(int i = 0; i < indent*4; i++){
      System.out.print(" ");
    }
    System.out.println( (n.getColor() == NodeColor.BLACK) ? n.getKey() : "<"+n.getKey()+">");
    if(n.left() != sentinel) {
      printHelper(n.left(), indent + 1);
    }
  }
  public int size(){
    return size;
  }

  protected TreeNode __debug_root() {
    return root;
  }

  protected TreeNode __debug_sentinel() {
    return sentinel;
  }

  protected ArrayList<TreeNode> __debug_leavesFrom(TreeNode n) {
    ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
    if (n == sentinel) {
      nodes.add(n);
    } else {
      Iterable<TreeNode> children = new SubtreeIterator<K, TreeNode, Tree>((Tree)this, n);
      for (TreeNode node : children) {
        if (node != sentinel && node.isLeaf()) {
          nodes.add(node);
        }
      }
    }
    return nodes;
  }
  protected Iterator<TreeNode> __debug_nodes() {
    return new SubtreeIterator((Tree)this, root);
  }

  protected Iterator<K> keyIterator() {
    return new RBKeyIterator<K, TreeNode, Tree>((Tree)this);
  }
 /*
  protected Iterator<K> __debug_subtreeIterator(RBNode n) {
    return new SubtreeIterator(this, n);
  }
  */
}
