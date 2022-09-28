package org.ditw.pise5.ch01

import java.math.BigInteger

class MyBigInt(v: BigInteger) {
  def this(l: Long) = this(BigInteger.valueOf(l))

  def +(i: Int): MyBigInt = new MyBigInt(v.add(BigInteger.valueOf(i)))

  override def toString: String = v.toString()
}

object MyBigInt:
  def main(args: Array[String]): Unit = {
    val bigInt = new MyBigInt(1L)
    println(bigInt + 1)
  }