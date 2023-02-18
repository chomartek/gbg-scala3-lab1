package org.ditw.pise5.ch09

import org.apache.commons.io.IOUtils

import java.io.FileInputStream
import java.nio.charset.StandardCharsets

object Ch9_WR {

  import collection.JavaConverters._
  private def readLogLines(logPath: String): List[String] = {
    val logStrm = new FileInputStream(logPath)
    val lines = IOUtils.readLines(logStrm, StandardCharsets.UTF_8)
    logStrm.close()
    lines.asScala.toList
  }
  val logLines = readLogLines("log4j-active.txt")

  // 1. high order functions
  def lineStartsWith(lines: List[String], startStr: String): List[String] = {
    lines.filter(line => line.startsWith(startStr))
  }

  def lineContains(lines: List[String], containStr: String): List[String] = {
    lines.filter(line => line.contains(containStr))
  }

  def lineMatchesRegex(lines: List[String], regex: String): List[String] = {
    lines.filter(line => line.matches(regex))
  }

  def lineMatches(lines: List[String],
                  matcher: (String, String) => Boolean,
                  matcherParam: String
                 ): List[String] = {
    lines.filter(line => matcher(line, matcherParam))
  }

  val linesStart1 = lineStartsWith(logLines, "\tat")
  println(linesStart1.take(5).mkString("\t\n", "\t\n", ""))
  val linesStart2 = lineMatches(logLines, _.startsWith(_), "\tat")
  println(linesStart2.take(5).mkString("\t\n", "\t\n", ""))

  def main(args: Array[String]): Unit = {

  }
}
