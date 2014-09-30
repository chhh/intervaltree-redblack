package com.ixee.datastruct;

import java.util.Iterator;

public class SubtreeIterator<K extends Comparable<K>, TreeNode extends RBTreeNode<K, TreeNode>, Tree extends BaseRBTree<K, TreeNode, Tree>> implements Iterator<TreeNode>, Iterable<TreeNode> {
  Tree tree;
  TreeNode curr;
  TreeNode subtreeRoot;

  public SubtreeIterator(Tree tree, TreeNode node) {
    this.tree = tree;
    subtreeRoot = node;

    curr = subtreeRoot.leftmost();
  }

  public TreeNode next() {
    TreeNode ret = curr;

    curr = tree.succ(curr);

    return ret;
  }

  public boolean hasNext() {
    return !curr.isSentinel() && !curr.parentOf(subtreeRoot);
  }

  public void remove() {
    throw new RuntimeException("Fill this in");
  }

  public Iterator<TreeNode> iterator() { return this; }
}
