package org.ditw.pise5.ch06

object Ch6Ex {
  implicit class IntRationalOps(i: Int) {
    def *(r: Rational): Rational = new Rational(i) * r
    def +(r: Rational): Rational = new Rational(i) + r
  }
  println(3 * Rational(3, 4))
  println(1 + Rational(3, 4))

  def main(args: Array[String]): Unit = {

  }
}
