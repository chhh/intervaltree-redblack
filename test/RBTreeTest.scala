package com.ixee.datastruct

import NodeColor._

import com.ixee.{InvariantHelpers, IxeeHelpers}

import org.scalatest._

import scala.collection.JavaConverters._

class RBTreeTest extends FreeSpec with Matchers with IxeeHelpers with InvariantHelpers[RBTree[Integer, String]] with RBTreeInvariants {

  implicit val invariants = buildRBInvariants[Integer, RBNode[Integer, String], RBTree[Integer, String]]

  def setup = new RBTree[Integer, String](true)

  def dummyTree = {
    val tree = setup

    tree.insert(1, "1 value")
    tree.insert(-2, "-2 value")
    tree.insert(-1, "-1 value")
    tree.insert(-6, "-6 value")
    tree.insert(5, "5 value")

    for (i <- -9 to 50 by 3) tree.insert(i, s"$i value")

    tree
  }

  "ixee.datastruct.RBTree" - {

    checkedInvariants("counts content size") { tree =>
      tree.insert(1, "first")

      tree.size() should be(1)

      tree.remove(1)

      tree.size() should be(0)
    }

    checkedInvariants("can have nodes added") { tree =>
      val limit = 100
      for (i <- 1 to limit) {
        tree.insert(i, i + " value")
      }

      for (i <- 2 to limit by 2) {
        tree.remove(i)
      }

      tree.size shouldBe limit/2

      for (i <- (limit + 1) to (2*limit)) {
        tree.insert(i, i + " value")
      }

      tree.size shouldBe (limit/2 + limit)
    }

    checkedInvariants("retrieving by minimum") { tree =>
      for (i <- 1 to 10) tree.insert(i, i + " value")

      tree.getMin() should be ("1 value")
    }

    checkedInvariants("retrieving by maximum") { tree =>
      for (i <- 1 to 10) tree.insert(i, i + " value")

      tree.getMax() should be ("10 value")
    }

    "is fast to query for a key" - {
      val tree = dummyTree

      timeAndTest(".get(3)", 10000000) {
        val result = tree.get(3)
      } { _ should be < 350L }
    }

    "is fast to insert a set of key/value pairs" - {
      timeAndTest(s"construct dummy tree of size ${dummyTree.size}", 100000) {
        dummyTree
      } { _ should be < 750L }
    }
  }

}
