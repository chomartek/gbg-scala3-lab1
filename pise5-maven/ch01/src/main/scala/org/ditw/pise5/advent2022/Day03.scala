package org.ditw.pise5.advent2022

import org.apache.commons.io.IOUtils

import java.io.FileInputStream
import java.nio.charset.StandardCharsets

object Day03 {
  private val PriorityMap = ('a' to 'z').map(c => c -> (c - 'a' + 1)).toMap ++
    ('A' to 'Z').map(c => c -> (c - 'A' + 27))

  private def findShared(line: String): Set[Char] = {
    if (line.length % 2 != 0) {
      throw new IllegalArgumentException(s"Even length expected (actualy input line $line, length = ${line.length}")
    }

    val firstPart = line.substring(0, line.length/2)
    val secondPart = line.substring(line.length/2)

    val firstSet = firstPart.toCharArray.toSet
    val secondSet = secondPart.toCharArray.toSet
    val shared = firstSet.intersect(secondSet)

    shared
  }

  private def findShared2(lines: List[String]): Char = {
    if (lines.size != 3) {
      throw new IllegalArgumentException(s"Expecting 3-line input")
    }
    val firstSet = lines(0).toCharArray.toSet
    val secondSet = lines(1).toCharArray.toSet
    val thirdSet = lines(2).toCharArray.toSet

    val shared = firstSet.intersect(secondSet).intersect(thirdSet)

    if (shared.size != 1) {
      throw new IllegalStateException(s"Error: expecting exactly one shared: (actual: ${shared})")
    }
    shared.head
  }

  private def processLine(line: String): Int = {
    val shared = findShared(line)
    shared.map(PriorityMap).sum
  }

  private def processAll(input: String): Int = {
    val lines = input.split("[\\r\\n]").map(_.trim).filter(_.nonEmpty)
    lines.map(processLine).sum
  }

  private def processAll2(input: String): Int = {
    val lines = input.split("[\\r\\n]").map(_.trim).filter(_.nonEmpty).toList

    if (lines.size % 3 != 0) {
      throw new IllegalArgumentException(s"Expecting size divisible by 3 (actual length = ${lines.size}")
    }

    import collection.mutable
    val lineGroups = mutable.ListBuffer[List[String]]()
    var idx = 0
    while (idx < lines.size) {
      lineGroups += lines.slice(idx, idx+3)
      idx += 3
    }
    lineGroups.map { g =>
      val shared = findShared2(g)
      PriorityMap(shared)
    }.sum
  }

  def main(args: Array[String]): Unit = {
    val testLines = List(
      "vJrwpWtwJgWrhcsFMMfFFhFp",
      "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
      "PmmdzqPrVvPwwTWBwg",
      "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
      "ttgJtRGJQctTZtZT",
      "CrZsJsPPZsGzwwsLwLmpwMDw"
    )

    testLines.foreach { line =>
      val shared = findShared(line)
      val priority = processLine(line)
      println(s"shared: $shared, priority: $priority")
    }

    val test1 = """vJrwpWtwJgWrhcsFMMfFFhFp
        |jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
        |PmmdzqPrVvPwwTWBwg
        |wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
        |ttgJtRGJQctTZtZT
        |CrZsJsPPZsGzwwsLwLmpwMDw
        |""".stripMargin

    println(processAll(test1))

    val ins = new FileInputStream("C:\\tmp\\advent-code\\input-day03.txt")
    val input = IOUtils.toString(ins, StandardCharsets.UTF_8)
    ins.close()
    println(processAll(input))
    println("-------------------------------")

    println(processAll2(test1))
    println(processAll2(input))
  }
}
