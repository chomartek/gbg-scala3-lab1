package org.ditw.pise5.misc

object SingletonTypeTest {
  type Dim = Singleton & Int
  case class Matrix[A <: Dim, B <: Dim](n: A, m: B) {
    def *[C <: Dim](other: Matrix[B, C]): Matrix[A, C] = Matrix(n, other.m)
  }

  val m1 = Matrix(2, 4)
  val m2 = Matrix(4, 3)
  println(m1*m2)

//  println(m2*m1)

  object Obj1 { }
  def check(v: Any) = v match {
//    case _ : Obj1.type => println("Obj1")
    case Obj1 => println("Obj1")
    case _ => println("Others")
  }
  check(Obj1)

  def fun1(): Obj1.type = Obj1

  class A {
    def fun1: this.type = {
      println("fun1")
      this
    }
  }
  class B extends A {
    def fun2: this.type = {
      println("fun2")
      this
    }
  }

  val b = new B
  b.fun1.fun2

  def main(args: Array[String]): Unit = {

  }
}
