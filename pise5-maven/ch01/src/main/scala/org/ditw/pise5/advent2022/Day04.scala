package org.ditw.pise5.advent2022

import org.apache.commons.io.IOUtils

import java.io.FileInputStream
import java.nio.charset.StandardCharsets

object Day04 {

  private def processLine1(line: String): Boolean = {
    processLineBase(
      line,
      (r1, r2) => r1.containsSlice(r2) || r2.containsSlice(r1)
    )
  }

  private def processLine2(line: String): Boolean = {
    processLineBase(
      line,
      (r1, r2) => r1.intersect(r2).nonEmpty
    )
  }


  private def processLineBase(line: String, contains: (Range, Range) => Boolean): Boolean = {
    val parts = line.split(",").map(_.trim).filter(_.nonEmpty)
    val ranges = parts.map { p =>
      val pp = p.split("-").map(_.trim).filter(_.nonEmpty)
      if (pp.length != 2) {
        throw new IllegalArgumentException(s"Illegal input format: $pp")
      }
      pp(0).toInt to pp(1).toInt
    }
    if (ranges.length != 2) {
      throw new IllegalArgumentException(s"Illegal input format: $line")
    }
    val r1 = ranges(0)
    val r2 = ranges(1)
    contains(r1, r2)
  }

  private def processAll1(input: String): Int = {
    val lines = input.split("[\\r\\n]").map(_.trim).filter(_.nonEmpty)
    val res = lines.map(processLine1)
    res.count(x => x)
  }

  private def processAll2(input: String): Int = {
    val lines = input.split("[\\r\\n]").map(_.trim).filter(_.nonEmpty)
    val res = lines.map(processLine2)
    res.count(x => x)
  }
  def main(args: Array[String]): Unit = {
    val testLines = List(
      "2-4,6-8",
      "2-3,4-5",
      "5-7,7-9",
      "2-8,3-7",
      "6-6,4-6",
      "2-6,4-8"
    )
    testLines.map(l => l -> processLine1(l)).foreach(println)

    val testContent =
      """2-4,6-8
        |2-3,4-5
        |5-7,7-9
        |2-8,3-7
        |6-6,4-6
        |2-6,4-8
        |""".stripMargin
    println(processAll1(testContent))
    println("-----------------")

    val ins = new FileInputStream("advent-2022/input-04.txt")
    val content = IOUtils.toString(ins, StandardCharsets.UTF_8)
    ins.close()
    println(processAll1(content))
    println("-----------------")
    println(processAll2(testContent))
    println("-----------------")
    println(processAll2(content))
    println("-----------------")

  }
}
