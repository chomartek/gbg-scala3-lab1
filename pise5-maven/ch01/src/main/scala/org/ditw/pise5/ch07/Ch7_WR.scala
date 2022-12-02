package org.ditw.pise5.ch07

object Ch7_WR {
  private def trace(v: Any): Unit =
    println(s"v = $v, type: ${v.getClass.getSimpleName}")

  // 1. if expression
  val args = Array("t.txt")

//  var fileName = "default.txt"
//  if (args.nonEmpty) {
//    fileName = args(0)
//  }

//  val fileName =
//    if (args.nonEmpty) args(0)
//    else "default.txt"

//  val fn1: Unit = if (args.nonEmpty) args(0)
//  trace(fn1)

//  val fileName3: String =
//    if (args.nonEmpty) args(0) // String
//    else throw new IllegalArgumentException("At least 1 argument expected") // Nothing is a sub-type of everything, including Strings
//  trace(fileName3)

  // 2. while loop: ...

  // 3. Unit value
//  val unit: Unit = ()
//  trace(unit)

  // 4 for ... do
  val strs = List("A", "new", "language")
//  val for0 = for (str <- strs) {
//    print(str + ' ')
//  }
//  trace(for0)

//  for (
//    str <- strs;
//    ch <- str.toCharArray if ch >= 'a' && ch <= 'z'
//  ) {
//    print(ch.toString + '!')
//  }

  // 5 for ... yield expression
//  val for1 = for (str <- strs) yield str + '!'
//  trace(for1)
//
//  val for2 = strs.map(_ + '!')
//  trace(for2)
//
//  val for3 = for (
//      str <- strs;
//      ch <- str.toCharArray if ch >= 'a' && ch <= 'z'
//    ) yield ch.toString + '!'
//
//  trace(for3)
//
//  strs.foreach(print)

  // 6. try ... catch expression
  //   and finally { ... }
//  def div1(n: Int, d: Int): Option[Int] = {
//    val result = try {
//      Some(n/d)
//    } catch {
//      case ex: Exception => None
//    } finally {
//      Some(-1)
//    }
//
//    result
//  }
//  println(div1(5, 0))
//  println(div1(15, 4))

  // 7. match expressions
  // 7.1 switch
//  def div2(n: Int, d: Int): Option[Int] = d match {
//    case 0 => None
//    case _ => Some(n/d)
//  }
//  println(div2(5, 0))
//  println(div2(15, 4))

  // 7.2 de-construction of case class
//  case class Person(name: String, age: Int)
//  val alice = Person("Alice", 32)
//  val greetings = alice match {
//    case Person(name, _) => s"Hej $name"
//  }
//  println(greetings)

  // 7.3 list matching ops
  // 7.3.1 match elem position
  val numList = List(1, 3, 5, 7)
  val numList2 = List(1, 3, 5, 7, 9)
//  val has5 = numList match {
//    case List(_, _, 5, _) => "5 found at the 3rd position"
//    case _ => "5 NOT found at the 3rd position"
//  }
//  println(has5)
//  val has5x = numList2 match {
//    case List(_, _, 5, _*) => "5 found at the 3rd position"
//    case _ => "5 NOT found at the 3rd position"
//  }
//  println(has5x)

//  val startsWith1 = numList match {
//    case 1 :: _ => "Starts with 1"
//    case _ => "NOT starts with 1"
//  }
//  println(startsWith1)

//  val endsWith7 = numList match {
//    case _ :+ 7 => "Ends with 7"
//    case _ => "NOT ends with 7"
//  }
//  println(endsWith7)
//  val endsWith7x = numList match {
//    case l @ List(_*) :+ 7 if l.length == 4 => "Ends with 7 and length = 7"
//    case _ => "NOT ends with 7"
//  }
//  println(endsWith7x)

  // 7.3.2 prepending

  // 7.3.3 other infix patterns, e.g. checking the ending

  // 7.3.4 conditional

  // 7.3.5 or '|'

  // 8 no break / continue
  val params = Array("-f", "-x", "Test.scala", "param1")

//  var i = 0; // This is Java
//  var foundIt = false
//  import scala.util.control.Breaks._
//  breakable {
//    while (i < params.length) {
//      if (params(i).startsWith("-")) {
//        i = i + 1
//      }
//      if (params(i).endsWith(".scala")) {
//        foundIt = true
//        println(s"Found! Breaking...")
//        break
//      }
//      i = i + 1
//    }
//  }
//  println(s"foundIt: $foundIt, i: $i")

  val res1 = params.find(arg => !arg.startsWith("-") && arg.endsWith(".scala"))
  println(res1)
  val res2 = params.indices.find(idx => !params(idx).startsWith("-") && params(idx).endsWith(".scala"))
  println(res2)

  // 9 refactor imperative-style code
  val rows = (1 to 10).map { x =>
    val line = (1 to 10).map(y => f"${x*y}%4s").mkString
    line
  }
  println(rows.mkString("\n"))
  

  def main(args: Array[String]): Unit = {
//    var fileName2: Boolean | Int =
//      if (args.nonEmpty) true
//      else 1234
//    trace(fileName2)
//    fileName2 = 1234
//    trace(fileName2)
//    fileName2 = "false"
//    trace(fileName2)

  }
}
