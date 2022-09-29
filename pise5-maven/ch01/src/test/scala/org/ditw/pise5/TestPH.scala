package org.ditw.pise5

import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TestPH extends AnyFunSuite with Matchers {
  test("A") {
    Set("a") shouldBe Set("a")
  }

  test("eventually") {
    val xs = 1 to 3
    val it = xs.iterator
    eventually { it.next() shouldBe 3 }
  }
}
