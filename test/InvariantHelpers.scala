package com.ixee

import org.scalatest._

object Invariants {
  def apply[A](rules: A => Any) = new Invariants(rules)
}

class Invariants[A](rules: A => Any) {
  def apply(x: A) = rules(x)
}

trait InvariantHelpers[A] {
  this: FreeSpec =>

  def setup: A
  def checkedInvariants(specName: String)(f: A => Any)(implicit invariants: Invariants[A]) = {
    val instance = setup

    specName - {

      "works" in { f(instance) }

      "upholds invariants" - {
        invariants(instance)
      }
    }
  }
}
