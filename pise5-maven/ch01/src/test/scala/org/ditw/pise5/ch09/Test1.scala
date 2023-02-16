package org.ditw.pise5.ch09

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class Test1 extends AnyFunSuite with Matchers {
  test("A") {
    val set1 = Set("a")
    set1.contains("a") shouldBe true
  }
}
