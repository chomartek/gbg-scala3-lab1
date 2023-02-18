package org.ditw.pise5.misc

import org.apache.commons.io.IOUtils

import java.io.FileInputStream
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

  val logLines = readLogLines("log4j-active.txt")

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


  type LineFilter = (String) => Boolean
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
