package org.ditw.pise5.ch01

object BigIntTest {
  def main(args: Array[String]): Unit = {
    println(fact(4))
    println(fact(40))
  }

  def fact(bi: BigInt): BigInt = {
    if (bi == 0) {
      1
    } else {
      bi * fact(bi-1)
    }
  }
}
