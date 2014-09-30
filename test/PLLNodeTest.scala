package com.ixee.datastruct

import com.ixee.IxeeHelpers

import org.scalatest._

import java.util.ArrayList

class PLLNodeTest extends FreeSpec with Matchers with IxeeHelpers {

  def dummyList = {
    val s: Seq[PLLNode[Integer]] = for (i <- 0 to 9) yield { new PLLNode[Integer]() }

    s.zipWithIndex map { case (node, idx) =>
      if (idx != 0) {
        node.setPrev(s(idx-1))
      }

      if (idx != s.length - 1) {
        node.setNext(s(idx + 1))
      }

      node.setValue(idx)

      node
    }

    s.head
  }

  "ixee.datastruct.PLLNode" - {
    "is fast to iterate through" - {
      timeAndTest("iterating through a list of 10", 100000) {
        var curr = dummyList

        while (curr.next != null) {
          val x = curr.value
          curr = curr.next
        }
      } { _ should be < 50L }
    }
  }
}
