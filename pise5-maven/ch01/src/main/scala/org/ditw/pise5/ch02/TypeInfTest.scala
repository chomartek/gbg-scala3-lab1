package org.ditw.pise5.ch02

object TypeInfTest {
  def trace(v: Any): Unit = {
    println(s"v is of type: ${v.getClass.getSimpleName}")
  }

  def main(args: Array[String]): Unit = {
    val bi0 = 0
    trace(bi0)

    val bi1: BigInt = 0
    trace(bi1)
  }
}
