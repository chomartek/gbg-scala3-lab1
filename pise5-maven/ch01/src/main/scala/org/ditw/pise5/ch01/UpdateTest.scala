package org.ditw.pise5.ch01

object UpdateTest {
  class Update(data: Array[String]) {

    def update(index: Int, v: String): Unit = {
      data(index) = v
    }

    def apply(index: Int): String = data(index)
  }

  def main(args: Array[String]): Unit = {
    val u1 = new Update(Array("a", "b"))

    u1(1) = "c"
    println(u1(1))
  }
}
