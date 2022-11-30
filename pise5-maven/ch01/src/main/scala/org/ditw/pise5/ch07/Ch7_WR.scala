package org.ditw.pise5.ch07

object Ch7_WR {
  private def trace(v: Any): Unit =
    println(s"v = $v, type: ${v.getClass.getSimpleName}")

  // 1. if expression

  // 2. while loop: ...

  // 3. Unit value

  // 4. for ... do
  val strs = List("A", "new", "language")
  val for0 = for (str <- strs) {
    print(str + ' ')
  }
  println()
  trace(for0)

  for (
    str <- strs;
    ch <- str.toCharArray
  ) {
    print(ch.toString + ' ')
  }

  // 5. for ... yield expression
  val for1 = for (str <- strs) yield {
    str + ' '
  }
  for1.foreach(s => println(s"[$s]"))

  // 6. try ... catch expression
  //   and finally { ... }
  private def div1(n: Int, d: Int): Option[Int] = {
    val result = try {
      Some(n/d)
    } catch {
      case th: Throwable => None
    }
//    finally {
//      Some(-1)
//    }

    result
  }
  println(div1(5, 0))
  println(div1(5, 4))


  // 7. match expressions
  // 7.1 switch
  private def div2(n: Int, d: Int): Option[Int] = d match {
    case 0 => None
    case _ => Some(n / d)
  }

  println(div2(5, 0))
  println(div2(5, 4))

  // 7.2 de-construction of case class
  case class Person(name: String, age: Int)
  val alice = Person("Alice", 32)
  val greetings = alice match {
    case Person(name, _) => s"Hej $name"
  }
  println(greetings)

  // 7.3 list extractors
  val numList = List(1, 3, 5, 7)
  val numList2 = List(1, 3, 5, 7, 9)
  val has5 = numList match {
    case List(_, _, 5, _) => "5 found at 3rd position"
    case _ => "5 NOT found at 3rd position"
  }
  println(has5)
  val has5x = numList2 match {
    case List(_, _, 5, _*) => "5 found at 3rd position"
    case _ => "5 NOT found at 3rd position"
  }
  println(has5x)
  // 7.4 prepending
  val startsWith1 = numList match {
    case 1 :: _ => "Starts with 1"
    case _ => "Not starts with 1"
  }
  println(startsWith1)

  // 7.5 other infix patterns, e.g. checking the ending
  val endsWith7 = numList match {
    case _ :+ 7 => "Ends with 7"
    case _ => "NOT ends with 7"
  }
  println(endsWith7)

  // 7.6 conditional
  val endsWith7x = numList match {
    case l @ List(1, _*) :+ 7 if l.size == 4 => "Starts with 1 and ends with 7"
    case _ => "NOT Starts with 1 or ends with 7"
  }
  println(endsWith7x)

  // 7.7 or '|'
  val endsWith7xx = numList match {
    case (_ :+ 7) | (1 :: _) => "Starts with 1 or ends with 7"
    case _ => "NOT Starts with 1 nor ends with 7"
  }
  println(endsWith7xx)

  // 8 no break / continue
  val params = Array("-f", "-x", "Test.scala", "param1")
  val res1 = params.find(arg => !arg.startsWith("-") && arg.endsWith(".scala"))
  println(res1)
  val res2 = params.indices.find(idx => !params(idx).startsWith("-") && params(idx).endsWith(".scala"))
  println(res2)

  //
  val rows = (1 to 10).map { x =>
    val line = (1 to 10).map(y => f"${x*y}%4s").mkString
    line
  }
  println(rows.mkString("\n"))


  def main(args: Array[String]): Unit = {

  }
}
