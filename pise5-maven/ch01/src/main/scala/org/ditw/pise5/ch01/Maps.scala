package org.ditw.pise5.ch01

object Maps:
  private def traceMap(m: Map[String, String]): Unit =
    println(s"map type: ${m.getClass.getSimpleName}")
  def main(args: Array[String]): Unit = {
    val m1 = Map("Sweden" -> "Stockholm")
    traceMap(m1)
    val m2 = m1 + ("UK" -> "London")
    traceMap(m2)

    val m4 = m2 ++ Seq("Germany" -> "Berlin", "Ireland" -> "Dublin")
    traceMap(m4)
    val m5 = m4 + ("France" -> "Paris")
    traceMap(m5)
  }
