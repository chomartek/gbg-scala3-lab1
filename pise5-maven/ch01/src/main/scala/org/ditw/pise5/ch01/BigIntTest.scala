package org.ditw.pise5.ch01

extension (x: Int)
  def ++ = x + 1
  def increment = x + 1
object BigIntTest extends Serializable {
  def main(args: Array[String]): Unit = {
    println(fact(4))
    println(fact(40))
    println(fact { 100 })

    println(ff { 1 } { true })

    println(Map { 100 -> 100 })
    val l1 = List(11, 2, 4)
    val l2 = l1.sorted

    val ii = Int2(1)
    import scala.language.postfixOps
    println(ii ++)
    println(ii +: ii)
    println(11 ++)
    println(11 increment)

    println("abc" toUpperCase )
  }

  def ff(a: Int)(b: Boolean): Option[Int] =
    if (b) Some(a) else None

  def fact(bi: BigInt): BigInt = {
    if (bi == 0) {
      1
    } else {
      bi * fact(bi-1)
    }
  }

  case class Int2(i: Int) {
    def ++ : Int2 = Int2(i+1)
    def +:(dummy: Int2): Int2 = Int2(i+2)
  }

}
