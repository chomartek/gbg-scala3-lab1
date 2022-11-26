package org.ditw.pise5.misc

object CustomInterpolator {

  import StringContext._

  implicit class PersonInterpolator(sc: StringContext) {
    def custom(args: Any*): String = {

      val stringContextIterator = sc.parts.iterator
      val argsIterator = args.iterator

      val context = stringContextIterator.next()
      println(s"context: $context")
      val sb = new java.lang.StringBuilder(context)

      while (argsIterator.hasNext) {
        val arg = argsIterator.next().toString
        println(s"arg: $arg")
        sb.append(str)
        val context = stringContextIterator.next()
        println(s"context: $context")
        sb.append(context)
      }
      sb.toString.toUpperCase()
    }
  }

  val str = "sss"
  val test1 = println(custom"hi $str!")

  val dec = 2228488
  println(dec.toBinaryString)
  // printf("%d", "001000100000000100001000".toLong)

  def main(args: Array[String]): Unit = {

  }
}
