package org.ditw.pise5.ch03

object Chap3_4 {

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
  }
}
