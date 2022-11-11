package org.ditw.pise5.ch07

object ControlStructureTest {


  def main(args: Array[String]): Unit = {
    val x = for (i <- 1 to 4) yield s"# $i"
    val y = for (i <- 1 to 4) s"# $i"
    println(s"x = $x, type: ${x.getClass.getSimpleName}")
    println(s"y = $y, type: ${y.getClass.getSimpleName}")

    val s = for (i <- 1 to 4 if i % 2 == 0) yield s"# $i"
    println(s"s = $s, type: ${s.getClass.getSimpleName}")
    val t = for
      i <- 1 to 4 if i % 2 == 0
      j <- 1 to 6 if j % 3 == 0
    yield s"$i x $j = ${i*j}"
    println(s"t = $t, type: ${t.getClass.getSimpleName}")
  }
}
