package com.ixee.datastruct

import com.ixee.IxeeHelpers

import org.scalatest._

class IntervalTest extends FreeSpec with Matchers with IxeeHelpers {

  "com.ixee.datastruct.RealInterval" - {
    "overlap detection" - {
      "can tell when overlapping with another interval" in new IntervalTest {
        interval.overlapsWith(new RealInterval[Integer](3, 7)) shouldBe true
      }

      "can detect when not overlapping with another interval" in new IntervalTest {
        val otherInterval = new RealInterval[Integer](interval.start - 4, interval.start - 1)
        interval.overlapsWith(otherInterval) shouldBe false
      }

      "can tell when contained in another interval" in new IntervalTest {
        interval.overlapsWith(new RealInterval[Integer](3, 17)) shouldBe true
      }

      "can tell when containing another interval" in new IntervalTest {
        interval.overlapsWith(new RealInterval[Integer](6, 7)) shouldBe true
      }
    }

    "equality" - {
      "is equal when the other interval has the same start and end" in new IntervalTest {
        val duplicate = new RealInterval[Integer](interval.start, interval.end)
        duplicate.equals(interval) shouldBe true
      }

      "is reflexive" in new IntervalTest {
        val duplicate = new RealInterval[Integer](interval.start, interval.end)
        duplicate.equals(interval) shouldBe interval.equals(duplicate)
      }
    }

    "hashCode" - {
      "is the same when equality is the same" in new IntervalTest {
        val duplicate = new RealInterval[Integer](interval.start, interval.end)
        duplicate.hashCode shouldBe interval.hashCode
      }
    }

    "union" - {
      "gives the minimum interval containing this and the other interval" in new IntervalTest {
        val duplicate = new RealInterval[Integer](interval.start - 6, interval.start - 4)
        val expected = new RealInterval[Integer](duplicate.start, interval.end)

        val union = interval.union(duplicate)

        union.equals(expected) shouldBe true
      }
    }

    "contains" - {
      "is true when the point == the start of this range" in new IntervalTest {
        interval.contains(interval.start) shouldBe true
      }

      "is true when the point ==  the end of this range" in new IntervalTest {
        interval.contains(interval.end) shouldBe true
      }

      "is true when the point is between the start and end" in new IntervalTest {
        interval.contains((interval.start + interval.end) / 2) shouldBe true
      }

      "is false when the point is outside the range" in new IntervalTest {
        interval.contains(interval.start - 1) shouldBe false
      }
    }
  }

  trait IntervalTest {
    val interval = new RealInterval[Integer](5, 10)
  }
}
