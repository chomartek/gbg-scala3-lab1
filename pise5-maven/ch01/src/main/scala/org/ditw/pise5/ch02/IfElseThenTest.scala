package org.ditw.pise5.ch02

object IfElseThenTest {
  def main(args: Array[String]): Unit = {
    def max(x: Int, y: Int) = if x > y then x else y
    println(max(15, 18))

    val op = Op(2)
    val op3 = op ++ 4
    println(op3)

    val op4 = op <= 0
    println(op4)
  }

  class Op(v: Int) {
    def ++(x: Int = 1): Op = Op(v+x)

    def <=(x: Int): Op = Op(x)

    override def toString: String = v.toString
  }
}
