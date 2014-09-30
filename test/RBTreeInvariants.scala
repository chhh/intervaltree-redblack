package com.ixee.datastruct

import com.ixee.Invariants
import NodeColor._

import org.scalatest._

import scala.collection.JavaConverters._

trait RBTreeInvariants extends FreeSpec with Matchers {

  def buildRBInvariants[K <: Comparable[K], Node <: RBTreeNode[K, Node], Tree <: BaseRBTree[K, Node, Tree]] = Invariants[Tree] { tree =>

    def nodesOf(tree: Tree): Seq[Node] = {
      val ret = collection.mutable.Buffer[Node]()

      for (node <- tree.__debug_nodes.asScala) {
        ret += node
      }

      ret
    }

    /*
      "is roughly balanced" in {
        Roughly balanced means that the path from the root to the
          furthest leaf is no more than twice as long as the path
          from the root to the nearest leaf

        This is a property of the Red-Black tree structure.

        As long as the other properties are upheld, this will be true:
          if every red node has two black children, and every path
          from a node to its leaves contains the same number of black nodes,
          the longest path is an alternating series of red/black nodes, where
          the shortest path is just a path of black nodes, so from the root
          the shortest path is n, where the longest is 2n.
    */

    "all nodes are red or black" in { /* Is an enum, impossible to be wrong */ }

    "the root is always black" in {
      val root = tree.__debug_root

      root.getColor shouldBe BLACK
    }

    "all leaves are black" in {
      /*
        Leaves are the sentinel node, as long as the sentinel is black, they are black.
      */
      val sentinel = tree.__debug_sentinel

      sentinel.getColor shouldBe BLACK
    }

    /*
    "every node should have an overallRange from key to the max of it and children pairs" in {
      val nodes = nodesOf(tree)

      nodes foreach { node =>
        val pairEnds = node.getPairs.asScala.map(_._1.asInstanceOf[RealInterval[Integer]].end)
        val pairEndMax = pairEnds.headOption map { head =>
          (pairEnds.tail :\ head)( (a, b) => if (a > b) a else b)
        }
        val expectedMax = Seq(Option(node.left.max), Option(node.right.max), pairEndMax).flatten.max

        expectedMax shouldBe node.max
      }
    }
    */

    "every red node must have two black children" in {
      for (node <- nodesOf(tree)) {
        if (node.getColor == RED) {
          node.left.getColor shouldBe BLACK
          node.right.getColor shouldBe BLACK
        }
      }
    }

    "every path from a node to any of its descendant leaves contains the same number of black nodes" in {
      def path(child: Node, parent: Node): Seq[Node] = {
        val ret = collection.mutable.Buffer[Node]()
        var curr = child

        while (curr != parent) {
          ret += curr
          curr = curr.parent()
        }

        ret
      }

      val nodes = collection.mutable.Buffer[Node]()

      for (key <- tree.keyIterator.asScala) {
        nodes += tree.getNode(key)
      }

      val paths = nodes map { node =>
        val leaves = tree.__debug_leavesFrom(node).asScala
        val paths = leaves map { path(_, node)
        }
        val counts = paths map { _.count (_.getColor == BLACK) }
        counts.distinct.size == 1
      }

      paths.forall { _ == true } shouldBe true
    }
  }

}
