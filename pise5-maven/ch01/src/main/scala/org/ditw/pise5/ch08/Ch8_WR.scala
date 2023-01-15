package org.ditw.pise5.ch08

object Ch8_WR {
  // 1. method
  def convertToOrdinal(num: Int): String = {
    num match {
      case 1 => "first"
      case 2 => "second"
      case 3 => "third"
      case _ => num + "th"
    }
  }

  case class NumWrapper(num: Int) {
    def toOridinal: String = convertToOrdinal(num)
  }

  // function within function
//  def convertToOrdinalFixed(num: Int): String = {
//    def lastDigitToOrdinalPostfix(n: Int): String = {
//      val rem = n % 10
//      rem match {
//        case 1 => "st"
//        case 2 => "nd"
//        case 3 => "rd"
//        case _ => "th"
//      }
//    }
//
//    num match {
//      case 1 => "first"
//      case 2 => "second"
//      case 3 => "third"
//      case _ => num + lastDigitToOrdinalPostfix(num)
//    }
//  }

//  def convertToOrdinalFixed(num: Int): String = {
//    def lastDigitToOrdinalPostfix: String = {
//      val rem = num % 10
//      rem match {
//        case 1 => "st"
//        case 2 => "nd"
//        case 3 => "rd"
//        case _ => "th"
//      }
//    }
//
//    num match {
//      case 1 => "first"
//      case 2 => "second"
//      case 3 => "third"
//      case _ => num + lastDigitToOrdinalPostfix
//    }
//  }

  // 3 first-class functions
  def convertToOrdinalFixed(num: Int): String = {
    val lastDigitToOrdinalPostfix =
      // function literal: definition
      () => {
        val rem = num % 10
        rem match {
          case 1 => "st"
          case 2 => "nd"
          case 3 => "rd"
          case _ => "th"
        }
      }

    num match {
      case 1 => "first"
      case 2 => "second"
      case 3 => "third"
      case _ => num + lastDigitToOrdinalPostfix() // function value: instantiated at runtime
    }
  }

  // 3.1 .foreach
//  val numList = List(1, 3, 6, 21, 24)
//  val printFn = (n: Int) => println(convertToOrdinalFixed(n))
//  numList.foreach(printFn)
//  val isEvenFn = (n: Int) => n % 2 == 0
//  val evenNumList = numList.filter(isEvenFn)
//  println()
//  println(evenNumList)

  // 4. short forms
//  val numList = List(1, 3, 6, 21, 24)
//  val evenNumList = numList.filter(n => n % 2 == 0)
//  println(evenNumList)

  // 5. placeholder syntax
  val numList = List(1, 3, 6, 21, 24)
  val evenNumList = numList.filter(_ % 2 == 0)
  println(evenNumList)

  val sum: Int = numList.fold(0)(_ + _)
  println(sum)


  def main(args: Array[String]): Unit = { }
}
