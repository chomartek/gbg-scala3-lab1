package org.ditw.pise5.ch13

trait Expr
// sealed trait Expr
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

  var x = 1.0
  val t = x match {
    // case Pi => s"Pi == $x" // compilation error
    case pi => s"Pi == $x"
    case _ => s"something else"
  }
  println(t)

  // constructor pattern, deep matches

  // sequence pattern

  // tuple pattern

  // type pattern

  // type erasure

  // variable binding

  // 13.3 pattern guard

  // 13.4 overlaps

  // 13.5 sealed classes
  def completeCaseList(e: Expr): Unit = e match {
    case Var(_) => "var"
    case Num(_) => "num"
  }
  completeCaseList(var1)

  // 13.6 Options

  // 13.7 other pattern matchings

  // 13.8 a larger example

  // case classes extended

  // case object
  sealed trait Opt[+T]
  case class Some[+T](v: T) extends Opt[T]
  case object Non extends Opt[Nothing]
  var optInt: Opt[Int] = Some(12)
  println(optInt)
  optInt = Non
  println(optInt)

  // tuple
  // https://github.com/VirtusLab/unicorn/issues/11
  case class Person(firstName: String, lastName: String, age: Int)
  val tp:(String, String, Int) = ("John", "Smith", 23)
  val tupled = (Person.apply _).tupled
  val person:Person = tupled(tp)
  println(person)

  // curried form
  case class PersonEx(firstName: String, lastName: String)
                     (val middleName: String = "")

  val personEx0 = PersonEx("John", "Smith")
  val personEx1 = PersonEx("John", "Smith")("A")
  val personEx2 = PersonEx("John", "Smith")("T")
  println(personEx2.middleName)
  println(s"personEx0 == personEx1: ${personEx0 == personEx1}")
  println(s"personEx2 == personEx1: ${personEx2 == personEx1}")

  case class PersonFamily(firstName: String)(implicit val lastName: String) {
    override def toString: String = s"PersonFamily(firstName=$firstName,lastName=$lastName)"
  }
  object FamilySmith {
    implicit val familyName: String = "Smith"
    def person(firstName: String): PersonFamily = PersonFamily(firstName)
  }

  val johnSmith = FamilySmith.person("John")
  val jamesSmith = FamilySmith.person("James")
  println(johnSmith)
  println(jamesSmith)

  val firstName = johnSmith match {
    case PersonFamily(fn) => fn
  }
  println(firstName)

//  object PersonFamily {
//    def unapply(person: PersonFamily): Some[(String, String)] =
//      Some(person.firstName -> person.lastName)
//  }
//
//  val lastName = johnSmith match {
//    case PersonFamily(_, ln) => ln
//  }
//  println(lastName)

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

  def main(args: Array[String]): Unit = {

  }

}
