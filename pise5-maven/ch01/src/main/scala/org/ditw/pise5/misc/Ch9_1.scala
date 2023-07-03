package org.ditw.pise5.misc

import org.apache.commons.io.IOUtils

import java.io.{FileInputStream, InputStream}
import java.nio.charset.StandardCharsets

object Ch9_1 {

  import collection.JavaConverters._
  private def readLogLines(logPath: String): List[String] = {
    val logStrm = new FileInputStream(logPath)
    val lines = IOUtils.readLines(logStrm, StandardCharsets.UTF_8)
    logStrm.close()
    lines.asScala.toList
  }

  // 1. high order functions
  def lineStartingWith(lines: Iterable[String], starting: String): Iterable[String] = {
    lines.filter(_.startsWith(starting))
  }

  def lineContaining(lines: Iterable[String], substring: String): Iterable[String] = {
    lines.filter(_.contains(substring))
  }

  def lineRegex(lines: Iterable[String], regex: String): Iterable[String] = {
    lines.filter(_.matches(regex))
  }

  def lineMatches(
                   lines: Iterable[String],
                   matcher: (String, String) => Boolean,
                   matcherParam: String
                 ): Iterable[String] = {
    lines.filter(line => matcher(line, matcherParam))
  }

  val logLines = readLogLinesInLoanPattern("log4j-active.txt")

  //  val filtered1 = lineStartingWith(logLines, "\tat")
  //  println("---- filtered1 ----")
  //  filtered1.take(5).foreach(println)
  //  val filtered11 = lineMatches(logLines, _.startsWith(_), "\tat")
  //  println("---- filtered11 ----")
  //  filtered11.take(5).foreach(println)
  //
  //  val filtered2 = lineContaining(logLines, "FairSchedulableBuilder")
  //  filtered2.take(5).foreach(println)
  //  val filtered21 = lineMatches(logLines, _.contains(_), "FairSchedulableBuilder")
  //  filtered21.take(5).foreach(println)


  type LineFilter = String => Boolean
  def lineFilter(lines: Iterable[String], lineFilters: List[LineFilter]): Iterable[String] = {
    var result = lines
    lineFilters.foreach { lineFilter =>
      result = result.filter(lineFilter)
    }
    result
  }

  val cleanedLogLines = lineFilter(logLines,
    List(
      // excluding lines containing a set of words
      line =>
        !Set("FairSchedulableBuilder", "DeltaParquetFileFormat", "spark.sql.hive.convertCTAS").exists(line.contains),
      !_.startsWith("\tat")
    )
  )

  cleanedLogLines.foreach(println)

  // 2. control abstraction in Scala collection API
  def containsNeg(num: List[Int]): Boolean = {
    var exists = false
    num.foreach { n =>
      if (n < 0) exists = true
    }
    exists
  }

  def containsOdd(num: List[Int]): Boolean = {
    var exists = false
    num.foreach { n =>
      if (n % 2 == 1) exists = true
    }
    exists
  }
  def containsNeg1(num: List[Int]): Boolean = num.exists(_ < 0)
  def containsOdd1(num: List[Int]): Boolean = num.exists(_ % 2 == 1)

  val t11 = List("aa", "ba", "ca").forall(_.contains("a"))
  val t12 = List("aa", "ba", "ca").forall(s => s.contains("a") && s.length == 2)
  println(t11)
  println(t12)

  val t2 = List("aa", "ba", "ca").find(_.contains("b"))
  println(t2)
  val t3 = List("aaa", "baba", "ca").sortBy(_.length)
  println(t3)

  // 3 currying
  def curriedSum(x: Int)(y: Int) = x + y
  def curriedSum1: Function1[Int, Int] = curriedSum(1) // 1 + y
  def curriedSum2: Int = curriedSum1(2) // 1 + 2

  // 4 new control structure
  // the loan pattern
  private def logReader[T](logPath: String)(reader: InputStream => T): T = {
    var logStrm: InputStream = null
    try {
      logStrm = new FileInputStream(logPath)
      reader(logStrm)
    } finally {
      if (logStrm != null) {
        logStrm.close()
      }
    }
    //    val lines = IOUtils.readLines(logStrm, StandardCharsets.UTF_8)
    //    logStrm.close()
    //    lines.asScala.toList
  }

  def readLogLinesInLoanPattern(logPath: String): List[String] = {
    logReader(logPath) { logStrm =>
      IOUtils.readLines(logStrm, StandardCharsets.UTF_8)
        .asScala
        .toList
    }
  }

  //  def curriedSum(x: Int)(y: Int)(z: Int) = x + y + z
  //  val curriedSum1: Function1[Int, Function1[Int, Int]] = curriedSum(1)
  //  // def curriedSum1(y: Int)(z: Int) = 1 + y + z
  //  val curriedSum2: Function1[Int, Int] = curriedSum1(2)
  //  val curriedSum3: Int = curriedSum2(3)
  //  println(curriedSum1)
  //  println(curriedSum2)
  //  println(curriedSum3)

  //  def timer[R](op: => R): (R, Long) = {
  //    val startTime = System.currentTimeMillis()
  //    val result = op
  //    val endTime = System.currentTimeMillis()
  //    result -> (endTime - startTime)
  //  }
  //
  //  val r = timer {
  //    Thread.sleep(800)
  //    "Done"
  //  }
  //
  //  println(s"Timer result: $r")
  //
  //  def when(cond: Boolean)(action: => Unit) = {
  //    if (cond) action
  //  }
  //
  //  var t = 1
  //  when(t == 1) {
  //    println("t == 1")
  //  }
  //  t = 3
  //  when(t == 2) {
  //    println("t == 2")
  //  }

  def main(args: Array[String]): Unit = {

  }
}
