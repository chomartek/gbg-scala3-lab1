package org.ditw.pise5.ch01

object OptionTest {
  def main(args: Array[String]): Unit = {
    val opt1 = Option("str")
    println(opt1.map(_.toUpperCase))
  }
}
