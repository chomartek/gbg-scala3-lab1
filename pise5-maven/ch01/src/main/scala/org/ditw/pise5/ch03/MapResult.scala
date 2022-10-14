package org.ditw.pise5.ch03

object MapResult {
  def main(args: Array[String]): Unit = {
    val m1 = Map("John" -> "Smith")

    val m2 = m1.map(p => p._1(0) -> p._2(0))
    println(s"m2 type: ${m2.getClass.getName}\n\t$m2")

    val m3 = m1.map(p => p._1.length + p._2.length)
    println(s"m3 type: ${m3.getClass.getName}\n\t$m3")

  }
}
