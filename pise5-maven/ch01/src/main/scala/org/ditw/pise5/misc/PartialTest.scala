package org.ditw.pise5.misc

import scala.util.Random

object PartialTest {
  type SortingFunc = Array[Int] => Array[Int]
  private def genRandom(count: Int): Array[Int] = {
    val r = new Random(System.currentTimeMillis());
    (1 to count).map(_ => Math.abs(r.nextInt)).toArray
  }

  private def profiling(alg: SortingFunc, size: Int): Unit = {
    println(s"Profiling with $size elements...")
    val input = genRandom(size)
    val startTs = System.currentTimeMillis()
    val res = alg(input)
//    println(input.take(10).mkString(","))
//    println(res.take(10).mkString(","))
    val endTs = System.currentTimeMillis()
    println(s"\ttime elapsed: ${endTs - startTs} ms")
  }

  private val JAVA_SORT: SortingFunc = arr => {
    import java.util.Arrays
    Arrays.sort(arr)
    arr
  }

  private val SCALA_SORT: SortingFunc = arr => arr.sorted

  def main(args: Array[String]): Unit = {

    val scalaProfiling = profiling(SCALA_SORT, _)
    scalaProfiling(100)
    scalaProfiling(10000)
    scalaProfiling(1000000)

    val javaProfiling = profiling(JAVA_SORT, _)

    javaProfiling(100)
    javaProfiling(10000)
    javaProfiling(1000000)

    val profiling1M = profiling(_, 1000000)

    profiling1M(JAVA_SORT)
    profiling1M(SCALA_SORT)

  }
}
