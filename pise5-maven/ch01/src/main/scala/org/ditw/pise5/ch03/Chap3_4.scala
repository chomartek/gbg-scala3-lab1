package org.ditw.pise5.ch03

object Chap3_4 {

  case class Person(val name: String, val age: Int)
  object Person {
    def unapply(person: Person): Option[(String, Int)] = {
      println("In unapply")
      Some(person.name -> (person.age - 2))
    }
  }

  import Person._
  def isAdult(person: Person): Boolean = person match {
    case Person(name: String, age: Int) if age >= 18 => true
    case _ => false
  }

  val johnIsAdult = isAdult(Person("John", 19))
  println(s"johnIsAdult: $johnIsAdult")

  def main(args: Array[String]): Unit = {
    val intArray = Array(1, 2, 3)
    for (i <- 0 until intArray.length) {
      // intArray(i) += 1
      println(intArray(i))
    }
    println(intArray.mkString(","))

    val intArrayPlus1 = intArray.map(_ + 1)
    println(intArrayPlus1.mkString(","))

    val intArrayPlus2 = for (i <- 0 until intArray.length) yield intArray(i) + 2
    println(intArrayPlus2.mkString(","))

    val op1 = Option(1)
    val op2 = op1.map(v => s"v is ${if (v % 2 == 0) "even" else "odd"}")
    println(op2)

    val l1 = List(1, 2, 47, 150)
    val l2: List[String] = l1.map(e => f"$e%d = 0x$e%02X")
    l2.foreach(println)
    println(s"l2 type: ${l2.getClass.getSimpleName}")

    val m1 = Map(1 -> "first", 2 -> "second")
    val m2 = m1.map(p => p._1 -> p._2.length)
    println(m2)
    println(s"m2 type: ${m2.getClass.getSimpleName}")

    val m3 = m1.map(p => s"${p._1}: ${p._2}")
    println(m3)
    println(s"m3 type: ${m3.getClass.getSimpleName}")

    val noReturn = pointOfNoReturn()
    println(s"pointOfNoReturn: $noReturn")
  }

  // scala 2
  class ChecksumAcc2 {
    // protected var sum = 0
    // private[ch03] var sum = 0
    private var sum = 0
//    def add(b: Byte): Unit = {
//      sum += b
//    }
    def add(b: Byte): Unit = sum += b
//    def checksum(): Int = {
//      return ~(sum & 0xFF) + 1
//    }
    def checksum(): Int = ~(sum & 0xFF) + 1
  }

  def pointOfNoReturn(): Int = {
    try {
      val l1 = List(1, 2, 4)
      var sum = 0
      l1.foreach { e =>
        sum += e
        return sum
      }
      sum
    } catch {
      case ex: Exception =>
        println(s"ex: $ex")
        -1
      case th: Throwable =>
        println(s"th: $th")
        -2
    }

  }

//  println("test access modifier private[Chap3_4]: " + (new ChecksumAcc2).sum)

//  class ChecksumAcc2X extends ChecksumAcc2 {
//    def getSum: Int = sum
//  }

  // scala 3
  class ChecksumAcc3:
    var sum = 0
}
