package org.ditw.pise5.ch12

object Ch12_WR {

  // 12.1 Putting code in packages

  // 12.2 Concise access to related code: see ../misc/PackageTest1.scala

  // 12.3 Imports

  import java.lang.{Integer => JavaInt, _} // scala 2
  import java.lang.{Integer as JInt, *}    // scala 3

  // 12.4 Implicit imports

  //  import java.lang.* // everything in the java.lang package
  //  import scala.* // everything in the scala package
  //  import Predef.* // everything in the Predef object

  val set1 = Set(1, 2, 3)
  val set2 = collection.immutable.Set(1, 2, 3)
  val set3 = collection.mutable.Set(1, 2, 3)
//  set1 += 4
//  println(set1)
  set3 += 4
  println(set3)

  Predef
  val javaInt: JInt = Integer.valueOf(10)
  val long1: Long = javaInt.toLong // Integer => Int
  println(long1)

  // 12.5 Access modifiers
  //   private protected public
  // members
  class C1 {
    val desc = "public info" // public by default
    private val secret = "key"
    protected val notSoSensitive = "visible to subclasses"
    private def show(): Unit = {
      println(s"$desc: $notSoSensitive")
    }
  }

  class SubC1 extends C1 {
    def trace(): Unit = {
      println(s"Desc: $desc")
      println(s"inherited: $notSoSensitive")
      // println(s"not accessible: $secret")
    }
  }

  // classes
  object Scope1 {
    class SC1
    protected class SC2
    private class SC3
  }

  import Scope1._
  val sc1 = new SC1
//  val sc2 = new SC2
//  val sc3 = new SC3

  object Scope2 {
    class Scope2SC1

    // add scope to access modifiers
    protected[Ch12_WR] class Scope2SC2
    private[Ch12_WR] class Scope2SC3
  }
  import Scope2._

  val ssc1 = new Scope2SC1
  val ssc2 = new Scope2SC2
  val ssc3 = new Scope2SC3

  // constructor
  class C3 private (f1: Int, f2: Boolean) {
    override def toString: String = s"f1 = $f1, f2 = $f2"
  }
  //  val c31 = new C3(1, false)

  object C3 {
    def c3(f1: Int, f2: Boolean) = new C3(f1, f2)
  }

  val c3 = C3.c3(-20, false)
  println(s"c3: $c3")

//  case class C4 (f1: Int, f2: Boolean)
//
//  // val c4Anon = new C4(-20, true)
//  // https://medium.com/knoldus/case-class-inheritance-8b10d3124e4e
//  //   case to case inheritance is prohibited
//  // https://www.quora.com/Why-was-case-to-case-inheritance-removed-from-Scala
//  // https://nrinaudo.github.io/scala-best-practices/tricky_behaviours/final_case_classes.html
//  class C4Sub(override val f1: Int, override val f2: Boolean, f3: String)
//    extends C4(f1, f2) { // should not extend case classes
//    override def toString: String = s"C4Sub($f1, $f2, $f3)"
//
//    override def hashCode(): Int = f1.hashCode() + f2.hashCode() + f3.hashCode
//
//    override def equals(obj: Any): Boolean = obj match {
//      case c4s: C4Sub =>
//        f1 == c4s.f1 && f2 == c4s.f2
//      case _ => false
//    }
//  }
//  val c4sub = new C4Sub(-20, true, "hi")
//  val c4 = C4(-20, true)
//  println(c4sub)
//  println(s"c4 == c4sub ? ${c4 == c4sub}")
//  println(s"c4sub == c4 ? ${c4sub == c4}")

  // case class C4 protected (f1: Int, f2: Boolean)

  // a library modelling a family of Elems
//  object Lib {
//    abstract class Elem (val baseAttr: Int)
//
//    case class Elem1 (override val baseAttr: Int, exAttr1: String) extends Elem(baseAttr)
//    // case class Elem2 (override val baseAttr: Int, exAttr2: Boolean) extends Elem(baseAttr)
//
//    case class UpdatedElem2 (override val baseAttr: Int, exAttr2: Boolean) extends Elem(baseAttr)
//  }
//
//  import Lib._
//
//  // client code
//  val el1 = Elem1(12, "Elem1")
//  println(el1)
////  val el21 = Elem2(2, true)
////  println(el21)
//
//  // problem with this lib:
//  //   does come with a good interface for the client code
//  //   "program to implementations" instead of "program to interfaces"
//
//  val el22 = UpdatedElem2(2, true)
//  println(el22)

  object Lib {
    // 1. make constructor private to the Lib module
    abstract class Elem private[Lib](val baseAttr: Int)

    // 2. limit the visibility of the sub-classes inside the Lib itself
    private case class Elem1 private[Lib](override val baseAttr: Int, exAttr1: String) extends Elem(baseAttr)

    // private case class Elem2 private[Lib](override val baseAttr: Int, exAttr2: Boolean) extends Elem(baseAttr)
    private case class UpdatedElem2 private[Lib](override val baseAttr: Int, exAttr2: Boolean) extends Elem(baseAttr)

    // 3. provide factory method for instantiating instances
    def elem1(baseAttr: Int, exAttr1: String): Elem = Elem1(baseAttr, exAttr1)
    // def elem2(baseAttr: Int, exAttr2: Boolean): Elem = Elem2(baseAttr, exAttr2)
    def elem2(baseAttr: Int, exAttr2: Boolean): Elem = UpdatedElem2(baseAttr, exAttr2)
  }

  import Lib._

//   val el1 = new Elem(12) { }

  val el1 = elem1(12, "Elem1")
  val el2 = elem2(2, true)

  // val el2 = elem2(2, true)

  println(el1)
  println(el2)

  // 12.6 Top-level definitions
  //   package global utility functions

  // 12.7 Exports (Scala 3)
  //   forward methods to member fields
  case class IntOrStr(v1: Int, v2: String) {
    export v1.*
    export v2.toLowerCase
  }

  val is1 = IntOrStr(1, "This is TRUE")
  println(s"is1 > 10: ${is1 > 10}")
  println(s"is1.toLowerCase(): ${is1.toLowerCase()}")

  def main(args: Array[String]): Unit = {

  }
}
