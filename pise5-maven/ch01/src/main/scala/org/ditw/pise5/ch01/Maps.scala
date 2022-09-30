package org.ditw.pise5.ch01

object Maps:
  private def traceMap(m: Map[String, String]): Unit =
    val trace = m.toList.sorted.mkString("\n\t", "\n\t", "")
    println(s"map type: ${m.getClass.getSimpleName}$trace")
  def main(args: Array[String]): Unit = {
    val m1 = Map("Sweden" -> "Stockholm")
    traceMap(m1)
    val m11 = Map.apply("Sweden".->("Stockholm"))
    println(m11 == m1)
    val m2 = Map("Sweden" -> "Stockholm", "UK" -> "London")
    traceMap(m2)

    val m4 = m2 ++ Seq("Germany" -> "Berlin", "Ireland" -> "Dublin")
    traceMap(m4)
    val m5 = m4 + ("France" -> "Paris")
    traceMap(m5)
  }

  def main1(args: Array[String]): Unit = {
    val m1 = Map("Sweden" -> "Stockholm")
    traceMap(m1)
    val m2 = Map("Sweden" -> "Stockholm", ("UK", "London"))
    traceMap(m2)

    val m4 = m2 ++ Seq("Germany" -> "Berlin", "Ireland" -> "Dublin")
    traceMap(m4)
    val m5 = m4 + "France".->("Paris")
    traceMap(m5)
  }
