package org.ditw.pise5.ch13

import scala.math.{E, Pi}
import scala.reflect.{ClassTag, classTag}

trait Expr
case class Var(name: String) extends Expr
case class Num(number: Double) extends Expr
case class UnOp(operator: String, arg: Expr) extends Expr
case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

object Ch13_WR {

  // 13.1 a simple example
  //  case class
  //   1. factory method -- no 'new' keyword needed
  //   2. param list in the constructor becomes member field directly
  //   3. toString
  //   4. copy method
  val var1 = Var("v1")
  println(var1.name)
  println(var1)
  val var2 = var1.copy(name = "v2")
  println(var2)

  //  pattern matching
  //  example: simplifying an expressoin
  //    1. - (- expr) = expr
  //    2. expr + 0 = expr
  //    3. expr * 1 = expr
  //    4. ...
  def simplifyExpr(expr: Expr): Expr = expr match {
    case UnOp("-", UnOp("-", expr2)) =>
      expr2
    case BinOp("+", expr2, Num(0)) =>
      expr2
    case BinOp("*", expr2, Num(1)) =>
      expr2
    case _ => expr
  }

  val var11 = simplifyExpr(
    UnOp("-", UnOp("-", var1))
  )
  println(s"var1 == var11: ${var1.eq(var11)}")

  //  match vs switch
  //   1. match is an expression which returns a value
  //   2. no fall through
  //   3. MatchError if none of the cases matches

  // variable
  //val constant = E
  val constant = 1.0
  constant match {
    case Pi =>
      println("constant Pi")
    case E =>
      println("constant E")
    case pi =>
      println(s"Non-constant: $pi")
    //    case Pii =>
    //      println(s"Non-constant: $Pii")
  }

  // constructor pattern
//  // sequence pattern
//  val getFirst = (seq: Seq[Int]) => seq match {
//    case Seq(first, _) =>
//      Some(first)
//    case _ => None
//  }
//
//  val seq1 = Seq(11, 2)
//  println(s"getFirst($seq1): ${getFirst(seq1)}")
//  val seq0 = Seq[Int]()
//  println(s"getFirst($seq0): ${getFirst(seq0)}")
//
//
//  val seq2 = Seq(11, 2, 3)
//  println(s"getFirst($seq2): ${getFirst(seq2)}")
//  val getFirst2 = (seq: Seq[Int]) => seq match {
//    case Seq(first, _*) =>
//      Some(first)
//    case _ => None
//  }
//  println(s"getFirst2($seq1): ${getFirst2(seq1)}")
//  println(s"getFirst2($seq2): ${getFirst2(seq2)}")
//  println(s"getFirst2($seq0): ${getFirst2(seq0)}")

  // tuple pattern ...
  // type pattern
//  val v1Trace = (v: Any) => v match {
//    case s: String =>
//      s"String type, value: $s"
//    case i: Int =>
//      s"Int type: value: $i"
//    case l: List[_] =>
//      s"List type: value: $l"
//    case _ =>
//      s"Unknown type: ${v.getClass.getTypeName}, value: $v"
//  }
//  println(v1Trace(12))
//  println(v1Trace("12"))
//  println(v1Trace(List(false)))
//  println(v1Trace(1 -> 2))
//
//  val v1TraceErasure = (v: Any) => v match {
//    case s: String =>
//      s"String type, value: $s"
//    case i: Int =>
//      s"Int type: value: $i"
//    case li: List[Int] =>
//      s"List[Int] type: value: $li"
//    case ls: List[String] =>
//      s"List[String] type: value: $ls"
//    case _ =>
//      s"Unknown type: ${v.getClass.getTypeName}, value: $v"
//  }
//  println(v1TraceErasure(List(12)))
//  println(v1TraceErasure(List("a", "b")))
//  // type erasure
//  def v1TraceErasureList[T : ClassTag](v: List[T]): String = {
//    val tag = implicitly[ClassTag[T]]
//    val typeName = tag.runtimeClass.getTypeName
//    typeName match {
//      case "int" =>
//        s"List[Int]: $v"
//      case "long" =>
//        s"List[Long]: $v"
//      case "java.lang.String" =>
//        s"List[String]: $v"
//      case _ =>
//        s"Unknown type: $typeName, $v"
//    }
//  }
//
//  println(v1TraceErasureList(List(12)))
//  println(v1TraceErasureList(List(1L)))
//  println(v1TraceErasureList(List("a", "b")))
//  println(v1TraceErasureList(List(false)))

  // variable binding
//  val addExpr = BinOp("+", Var("x"), Var("y"))
//  val leftExpr = addExpr match {
//    case BinOp(_, le @ _, _) =>
//      le
//  }
//  println(s"Left expression: $leftExpr")

  // 13.3 pattern guard
//  def simplifyExpr2(expr: Expr): Expr = expr match {
//    case BinOp("+", x, y) if x == y =>
//      BinOp("*", x, Num(2))
//    case _ => expr
//  }
//  val t2 = simplifyExpr2(BinOp("+", Var("x"), Var("x")))
//  println(s"simplifyExpr2 result: $t2")

  // 13.4 overlaps ...: put more specific matching rules before general ones

  // 13.5 sealed classes
//  def completeCaseList(e: Expr): Unit = e match {
//    case Var(_) => "var"
//    case Num(_) => "num"
//  }
//  completeCaseList(var1)

  // 13.6 Options ...

  // 13.7 other pattern matchings
//  val tp1 = (1, true)
//  val (result, success) = tp1
//
//  // case seq as partial function
//  val typeOf: Any => String =
//    case _: Int => "int"
//    case _: String => "string"
//
//  println(s"typeOf(12): ${typeOf(12)}")
//  // println(s"typeOf(12L): ${typeOf(12L)}")

  // 13.8 a larger example ...

  // case classes extended

  // case object
//  sealed trait Opt[+T]
//  case class Som[+T](v: T) extends Opt[T]
//  // case class Non1 extends Opt[Nothing]
//  case object Non extends Opt[Nothing]
//  var optInt: Opt[Int] = Som(12)
//  println(optInt)
//  optInt = Non
//  println(optInt)
//
//  abstract class Animal(`type`: String = "Unknown")
//  case class Cat(name: String) extends Animal("Cat")
//  case class Dog(name: String) extends Animal("Dog")
//  val cat = Cat("Kitty")
//  val ani = cat
//  val optCat: Opt[Cat] = Som(cat)
//  val optAni: Opt[Animal] = optCat
//  println(s"optAni: $optAni")

  // tuple
  // https://github.com/VirtusLab/unicorn/issues/11
//  case class Person(firstName: String, lastName: String, age: Int)
//  val tp:(String, String, Int) = ("John", "Smith", 23)
//  val tupled = (Person.apply _).tupled
//  val person:Person = tupled(tp)
//  println(person)

  // curried form
//  case class PersonEx(firstName: String, lastName: String)
//                     (val middleName: String = "")
//
//  val personEx0 = PersonEx("John", "Smith")
//  val personEx1 = PersonEx("John", "Smith")("A")
//  val personEx2 = PersonEx("John", "Smith")("T")
//  println(personEx2.middleName)
//  println(s"personEx0 == personEx1: ${personEx0 == personEx1}")
//  println(s"personEx2 == personEx1: ${personEx2 == personEx1}")
//
//  case class PersonFamily(firstName: String)(implicit val lastName: String) {
//    override def toString: String = s"PersonFamily(firstName=$firstName,lastName=$lastName)"
//  }
//  object FamilySmith {
//    implicit val familyName: String = "Smith"
//    def person(firstName: String): PersonFamily = PersonFamily(firstName)
//  }
//
//  val johnSmith = FamilySmith.person("John")
//  val jamesSmith = FamilySmith.person("James")
//  println(johnSmith)
//  println(jamesSmith)
//
//  val firstName = johnSmith match {
//    case PersonFamily(fn) => fn
//  }
//  println(firstName)
//
////  object PersonFamily {
////    def unapply(person: PersonFamily): Some[(String, String)] =
////      Some(person.firstName -> person.lastName)
////  }
////
////  val lastName = johnSmith match {
////    case PersonFamily(_, ln) => ln
////  }
////  println(lastName)

//  // private constructor
//  case class NaturalNum private (v: Int)
//  object NaturalNum {
//    def apply(v: Int): Option[NaturalNum] =
//      if (v > 0) Option(new NaturalNum(v)) else None
//  }
//  val n1 = NaturalNum(2).get
//  println(n1)
//  val n2 = n1.copy(v = -12)
//  println(n2)

  // trait Product
//  val firstElem = person.productElement(0)
//  val secondElem = person.productElement(1)
//  val elems = person.productArity
//  println(s"1st: $firstName, 2nd: $secondElem, #: $elems")

  def main(args: Array[String]): Unit = {

  }

}
