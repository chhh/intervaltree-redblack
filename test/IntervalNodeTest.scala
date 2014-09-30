package com.ixee.datastruct

import NodeColor._

import org.scalatest._

import scala.collection.JavaConverters._

class IntervalNodeTest extends FreeSpec with Matchers {

  def checkedInvariants[A](specName: String)(f: IntervalNode[Integer, String] => A) = {
    val node = new RealIntervalNode[Integer, String]()
    node.setRange(0, 5)

    specName - {

      "works" in { f(node) }

      "upholds invariants" - {

        "the node's range is [selfKey, max(left.maxKey, right.maxKey, selfMaxKey)]" in {
          val range = node.getRange().asInstanceOf[RealInterval[Integer]]

          val leftMax = Option(node.left.max)
          val rightMax = Option(node.right.max)

          var expectedMax = node.max

          leftMax map { value =>
            expectedMax = Math.max(value, expectedMax)
          }

          rightMax map { value =>
            expectedMax = Math.max(value, expectedMax)
          }

          range.end shouldBe expectedMax
          range.start shouldBe node.getKey()
        }

        /*
        "every red node must have two black children" in {
          for (key <- node.keyIterator.asScala) {
            val node = node.getNode(key)
            if (node.getColor == RED) {
              node.left.getColor shouldBe BLACK
              node.right.getColor shouldBe BLACK
            }
          }
        }
        */

      }
    }
  }

  "ixee.datastruct.IntervalNode" - {

    checkedInvariants("can have its interval re-assigned") { node =>
      node.setRange(1, 2)
    }

    checkedInvariants("can have a left child assigned") { node =>
      val newLeft = new RealIntervalNode[Integer, String]()
      newLeft.setRange(-1, 8)

      node.setLeft(newLeft)
    }

    checkedInvariants("can have a right child assigned") { node =>
      val newRight = new RealIntervalNode[Integer, String]()
      newRight.setRange(4, 6)

      node.setRight(newRight)
    }
  }
}
