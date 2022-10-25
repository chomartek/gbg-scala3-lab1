package org.ditw.pise5.ch06

class Rational(n: Int, d: Int) {
  // println(s"Created: $n/$d")
  require(d != 0, "denominator cannot be 0")
  private val gcd = Rational.gcd(n.abs, d.abs)
  val numerator: Int = n / gcd
  val denominator: Int = d / gcd
  def add(that: Rational): Rational = {
    Rational(numerator * that.denominator + denominator * that.numerator, denominator * that.denominator)
  }

  def +(that: Rational): Rational = add(that)
  def +(that: Int): Rational = add(Rational(that))
  def *(that: Rational): Rational = Rational(numerator * that.numerator, denominator * that.denominator)
  override def toString: String = s"$numerator/$denominator"

  def lessThan(that: Rational): Boolean = {
    this.numerator * that.denominator < this.denominator * that.numerator
  }

  def this(n: Int) = {
    this(n, 1)
    println(s"denom = 1")
  }
}

object RationalV2 {
  implicit def int2Rational(i: Int): Rational = Rational(i)

  def main(args: Array[String]): Unit = {
    println(1 + Rational(2, 3))
  }
}

object Rational {
  extension (x: Int)
    def + (y: Rational) = Rational(x) + y
  private def gcd(a: Int, b: Int): Int = {
    if (b == 0) a
    else gcd(b, a%b)
  }
  def main1(args: Array[String]): Unit = {
    val r1 = Rational(3, 4)
    println(r1)
//    val r2 = Rational(5, 0)
    val r3 = Rational(4, 5)
    val r4 = r1.add(r3)
    println(r4)

    val r5 = Rational(2)
    println(r5.add(r1))
    println(Rational(4, 8))
    println(Rational(-6, 16))

    println(
      Rational(2, 3) + Rational(2, 3) * Rational(1, 3)
    )
    println(
      (Rational(2, 3) + Rational(2, 3)) * Rational(1, 3)
    )
    println(
      Rational(2, 3) + 1
    )
    println(
      1 + Rational(2, 3)
    )
  }
}