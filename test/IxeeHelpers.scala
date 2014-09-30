package com.ixee

import com.ixee.datastruct.Counter

import org.scalatest._

trait IxeeHelpers {
  this: FreeSpec =>

  implicit class BetterComparable[K <: Comparable[K]](x: K) {
    def >(that: K) = x.compareTo(that) > 0
    def <(that: K) = x.compareTo(that) < 0
    def ==(that: K) = x.compareTo(that) == 0
  }

  def timeCountAndTest(desc: String, rounds: Int)(block: => Counter)(test: Counter => Long => Any): Unit = {
    timeAndTest(desc, rounds, "Counter ticks: " + block.value)(block)(test(block))
  }

  def time(rounds: Int)(block: => Any): Long = {
    def toMillis(nanos: Long) = nanos / 1000000L

    var i = 0

    while (i < rounds) {
      i = i + 1
      block
    }

    val startNanos = System.nanoTime

    i = 0

    while (i < rounds) {
      i = i + 1
      block
    }

    toMillis(System.nanoTime - startNanos)
  }

  def markup2(label: String, value: Any, padding: Int = 20): Unit = markup(s"${(label + ":").padTo(padding, ' ')}$value")

  def timeAndTest(desc: String, rounds: Int, extra: String = "")(block: => Any)(expectation: Long => Any): Unit = {
    val duration = time(rounds)(block)

    "is faster than the time limit" in {
      markup2("Description", desc)
      markup2("# Runs", rounds)
      markup2("Average time taken", duration * 1.0 / rounds)
      markup2("Total time taken", duration)
      if (extra.nonEmpty) {
        markup2("Extra", extra)
      }

      expectation(duration)
    }
  }
}
