package org.ditw.pise5.ch04

import java.nio.charset.StandardCharsets

object Ch3_4WR {
  // for-loop in non-functional programming style
  val strArray = Array("Hi", "Hej", "你好")
//  var totalLen = 0
//  for (i <- 0 until strArray.length) {
//    strArray(i) += '!'
//    totalLen += strArray(i).length
//  }
//
//  var tmp = 0
//  val strArray2 = strArray.map { str =>
//    tmp += str.length
//    str + '!'
//  }
//  val totalLen2 = strArray.map(str => str.length).sum
//
//  // The for-yield produces exactly the same result as map
//  // because the compiler transforms the for-yield expression into the map call.
//  val strArray3 = {
//    var base = 15
//    for (i <- strArray.indices) yield strArray(i) + '!'
//  }
//
//  // The map operation is available across all collection types, including Option.
//  // Option is also a collection -- with 0 or 1 element.
//  val strOpt = Some("hi")
//  val strOpt2 = strOpt.map(_ + '!')
//
//  // Notice that when you map a List, you get a new List back.
//  // When you map a Vector, you get a new Vector back.
//  // You’ll find this pattern holds true for most types that define a map method.
//  private def trace(v: AnyRef): Unit =
//    println(s"v = $v, type: ${v.getClass.getSimpleName}")
//
//  val vec1 = Vector(1, 2, 3)
//  val vec2 = vec1.map(_.toString)
//  trace(vec2)
//
//  val list1 = (1 to 8).toList
//  val list2 = list1.map(_.toString + '!')
//  trace(list2)
//  val l3 = 1 :: Nil
//
//  // a bit more complicated than that
//  val map1 = Map(1 -> "first", 2 -> "second")
//  val map2 = map1.map(tp => tp._1+1 -> (tp._2 + '!'))
//  trace(map2)
//  val map3 = map1.map(tp => s"${tp._1}: ${tp._2}")
//  trace(map3)
//
//  // Defining a class in scala 2/3
//  class ChecksumAcc3:
//    var sum = 0
//
//  class ChecksumAcc2 {
//    protected var sum = 0
//    def getSum = sum
//  }
//
//  class ChecksumAcc2X extends ChecksumAcc2 {
//    def getSum2 = sum
//  }

  // val sum11 = (new ChecksumAcc2).sum

  // do NOT use `return`
  def pointOfNoReturn(): Int = {
    try {
      var sum = 0
      val l1 = List(1,2,3)
      l1.foreach { e =>
        sum += e
        return sum
      }
      sum
    } catch {
      case th: Throwable =>
        println(s"Throwable: $th")
        -1
    }
  }
  println(s"pointOfNoReturn = ${pointOfNoReturn()}")

  // Semicolon inference: ... should avoid using ';'

  // singleton objects
  class ChecksumAcc2 {
    private var sum = 0
  }

  object ChecksumAcc2 {
    def getSum(checksum: ChecksumAcc2) = checksum.sum
  }

  // case class
  case class Person(name: String, age: Int) {
    var extra = ""
  }

  object Person {
    def apply(name: String, age: Int): Person = {
      val nameWithInitial = name(0).toUpper + name.substring(1)
      new Person(nameWithInitial, age)
    }

    def unapply(person: Person): Option[(String, Int)] = {
      Some(person.name.toUpperCase, person.age)
    }
  }

  val john = Person.apply("john", 39)
  println(s"john: ${john.name}, age: ${john.age}")
  println(s"john: $john")

  val james = Person("james", 39)
  val john2 = Person("john", 39)
  println(s"john == james: ${john == james}")
  println(s"john == john2: ${john == john2}")

  def extractName(person: Person): String = {
    person match {
      case Person(name, _) => name
      case _ => "_Unknown_"
    }
  }



  println(s"john's name is ${extractName(john)}")

  // A Scala application
  def main(args: Array[String]): Unit = {
  }
}
