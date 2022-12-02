package org.ditw.pise5.misc

class Rational(n: Int, d: Int) {
  // println(s"Created: $n/$d")
  require(d != 0, "denominator cannot be 0")
  private def _gcd(a: Int, b: Int): Int = {
    if (b == 0) a
    else _gcd(b, a%b)
  }
  private val gcd = _gcd(n.abs, d.abs)
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
    // println(s"denom = 1")
  }
}

object RationalTest {
  implicit def int2Rational(i: Int): Rational = Rational(i)

  def main(args: Array[String]): Unit = {
    println(1 + Rational(2, 3))
  }
}