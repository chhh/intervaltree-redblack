package com.ixee.datastruct

import com.ixee.{InvariantHelpers, IxeeHelpers}

import NodeColor._

import org.scalatest._

import scala.collection.JavaConverters._
import reflect.runtime.universe._

class IntervalTreeTest extends FreeSpec with Matchers with IxeeHelpers with InvariantHelpers[IntervalTree[Integer, String]] with RBTreeInvariants {

  case class Foo(vals: Seq[(String, Seq[String])])

  def setup = new IntervalTree[Integer, String](true)
  def dummyTree = {
    val tree = setup

    tree.insert(4, 5, "4 5 value")
    tree.insert(1, 6, "1 6 value")
    tree.insert(9, 10, "9 10 value")
    tree.insert(2, 4, "2 4 value")
    tree.insert(4, 6, "4 6 value")
    tree.insert(-5, 6, "-5 6 value")

    for (i <- 400 to 500) tree.insert(i, i + 50, s"$i node vlue")

    tree
  }

  implicit val invariants = buildRBInvariants[Integer, IntervalNode[Integer, String], IntervalTree[Integer, String]]

  "ixee.datastruct.IntervalTree" - {

    checkedInvariants("counts content size") { tree =>
      tree.insert(1, 1, "first")

      tree.size() should be(1)

      tree.removeAt(1, 1)

      tree.size() should be(0)
    }

    checkedInvariants("can have nodes added") { tree =>
      val limit = 8
      for (i <- 1 to limit) {
        tree.insert(i, i, i + " value")
      }

      for (i <- 2 to limit by 2) {
        tree.removeAt(i, i)
      }

      tree.size shouldBe limit/2

      for (i <- (limit + 1) to (2*limit)) {
        tree.insert(i, i, i + " value")
      }

      tree.size shouldBe (limit/2 + limit)
    }

    /*
    checkedInvariants("retrieving by minimum") { tree =>
      for (i <- 1 to 10) tree.insert(i, i, i + " value")

      tree.getMin() should be ("1 value")
    }

    
    checkedInvariants("retrieving by maximum") { tree =>
      for (i <- 1 to 10) tree.insert(i, i, i + " value")

      tree.getMax() should be ("10 value")
    }*/

    checkedInvariants("returns values in a range") { tree =>
      for (i <- 1 to 10; j <- i to (i + 2)) tree.insert(i, j, s"${i} ${j} value")

      val result = tree.getAtOrBetween(4, 6)

      result.size shouldBe 6
    }
    

    "is fast to query exact interval matches" - {
      val tree = dummyTree

      val list = new java.util.ArrayList[String]()

      val duration = timeAndTest(".pairsAt(400, 450)", 10000000) {
        tree.getAt(1, 6, list)
        list.clear()
      } { _ should be < 300L }
    }

    "is fast to find intervals overlapping with a point" - {
      val tree = dummyTree

      val list = new java.util.ArrayList[String]()

      val duration = timeCountAndTest(".pairsContaining(3)", 10000000) {
        val counter = tree.__countedPairsContaining(3, list)
        list.clear()
        counter
      } { counter => time =>
        counter.value should be < 10
        time should be < 1500L
      }
    }

    "is fast to query for intervals bounded by a range" - {
      val tree = dummyTree

      val list = new java.util.ArrayList[String]()

      timeCountAndTest(".pairsBetween(1, 9)", 10000000) {
        val counter = tree.__countedPairsBetween(1, 9, list)
        list.clear()
        counter
      } { counter => time =>
        counter.value should be < 10
        time should be < 1500L
      }
    }
  }

}
