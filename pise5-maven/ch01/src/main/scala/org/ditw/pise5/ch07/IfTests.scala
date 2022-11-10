package org.ditw.pise5.ch07

object IfTests {
  def main(args: Array[String]): Unit = {
    val t1: String | Int = if (args.isEmpty) 0 else "n"
    println(s"type = ${t1.getClass.getSimpleName}, value = $t1")

    val t2 = if (args.isEmpty) 0
    println(s"type = ${t2.getClass.getSimpleName}, value = $t2")
  }
}
