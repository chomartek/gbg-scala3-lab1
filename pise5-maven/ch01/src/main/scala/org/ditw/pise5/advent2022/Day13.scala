package org.ditw.pise5.advent2022

import org.apache.commons.io.IOUtils

import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import scala.collection.mutable.ListBuffer

object Day13 {
  trait SigValue extends Comparable[SigValue]

  case class ListSigValue(valueList: List[SigValue]) extends SigValue {
    def size: Int = valueList.size
    override def compareTo(otherValue: SigValue): Int = {
      otherValue match {
        case intValue: IntSigValue =>
          compareTo(ListSigValue(List(intValue)))
        case listValue: ListSigValue =>
          val minSize = Math.min(size, listValue.size)
          val valueCmp = (0 until minSize).map { idx =>
            valueList(idx).compareTo(listValue.valueList(idx))
          }
          val firstDiff = valueCmp.find(_ != 0)
          val result = if (firstDiff.nonEmpty) {
            firstDiff.get
          } else {
            if (size > minSize)
              1
            else if (listValue.size > minSize)
              -1
            else
              0
          }
          result
      }
    }

    override def toString: String = {
      val content = valueList.map(_.toString).mkString(",")
      s"[$content]"
    }
  }

  case class IntSigValue(v: Int) extends SigValue {
    override def compareTo(otherValue: SigValue): Int = {
      val result: Int = otherValue match {
        case intValue: IntSigValue =>
          v - intValue.v
        case listValue: ListSigValue =>
          ListSigValue(List(this)).compareTo(listValue)
        case _ =>
          throw new IllegalArgumentException(s"Unexpected value type")
      }
      result
    }

    override def toString: String = v.toString
  }

  private def parseInts(preSegment: String): List[SigValue] = {
    val parts = preSegment.split(",").map(_.trim).filter(_.nonEmpty)
    if (preSegment.trim.contains("]")) {
      println("dbg")
    }
    parts.map(p => IntSigValue(p.toInt)).toList
  }

  private def findClosingBracket(input: String, startIndex: Int): Int = {
    var pair: Int = 1
    var currIdx = startIndex
    while (currIdx < input.length-1 && pair > 0) {
      currIdx = currIdx + 1
      input.charAt(currIdx) match {
        case '[' =>
          pair = pair + 1
        case ']' =>
          pair = pair - 1
        case _ =>
      }
    }

    if (pair == 0) {
      currIdx
    } else {
      throw new IllegalArgumentException(s"Error finding closing ']', input: $input")
    }
  }

  private def parseValue(segment: String): List[SigValue] = {
    if (segment.isEmpty) {
      List()
    }
    else {
      val startIndex = segment.indexOf("[")
      if (startIndex < 0) {
        parseInts(segment)
      } else {
        val preSeg = segment.substring(0, startIndex)
        val preValues = parseInts(preSeg)
        val lastIndex = findClosingBracket(segment, startIndex) // rem.lastIndexOf("]")
        val childList = parseValue(segment.substring(startIndex+1, lastIndex))
        var remrem = segment.substring(lastIndex+1).trim
        if (remrem.startsWith(",")) {
          remrem = remrem.substring(1)
        }

        val remValues = parseValue(remrem)
        val values = preValues ::: ListSigValue(childList) :: remValues
        values
      }
    }


  }

  private def readValues(input: String): List[(SigValue, SigValue)] = {
    val lines = input.split("[\\r\\n]+").map(_.trim).filter(_.nonEmpty)

    val pairs = ListBuffer[(SigValue, SigValue)]()
    var v1: SigValue = null
    var v2: SigValue = null
    lines.indices.foreach { idx =>
      if (idx % 2 == 0) {
        v1 = parseValue(lines(idx)).head
      } else if (idx % 2 == 1) {
        v2 = parseValue(lines(idx)).head
        pairs += v1 -> v2
      }
    }
    pairs.toList
  }

  private def sort(pairs: List[(SigValue, SigValue)]): Int = {
    val dividers = List(
      parseValue("[[2]]").head,
      parseValue("[[6]]").head
    )
    println(dividers)
    val allValues = pairs.flatMap { p =>
      Seq(p._1, p._2)
    } ++ dividers

    val sorted = allValues.sorted

    val d1Idx = sorted.indices.find(idx => sorted(idx).toString == "[[2]]")
    val d2Idx = sorted.indices.find(idx => sorted(idx).toString == "[[6]]")

    (d1Idx.get + 1) * (d2Idx.get+1)
  }

  def main(args: Array[String]): Unit = {
//    println(parseValue("[[0,5,[[],[],2,[7,9]]],[[8,8,3,[6,3,8,9,1]],[2,0,10,7,10],4,10,[9,[1,8],4,[4,0,5,10],[4,0,8,8]]],[9,10],[],[0,[[]],4,10]]"))
    println(parseValue("[1,1,3,1,1]"))
    val testPairs = readValues(
      """[1,1,3,1,1]
        |[1,1,5,1,1]
        |
        |[[1],[2,3,4]]
        |[[1],4]
        |
        |[9]
        |[[8,7,6]]
        |
        |[[4,4],4,4]
        |[[4,4],4,4,4]
        |
        |[7,7,7,7]
        |[7,7,7]
        |
        |[]
        |[3]
        |
        |[[[]]]
        |[[]]
        |
        |[1,[2,[3,[4,[5,6,7]]]],8,9]
        |[1,[2,[3,[4,[5,6,0]]]],8,9]""".stripMargin
    )

    var testScore = 0
    testPairs.indices.foreach { idx =>
      val p = testPairs(idx)
      println(s"${p._1}\n${p._2}")
      if (p._1.compareTo(p._2) <= 0) {
        println("=== true")
        testScore += idx + 1
      } else {
        println("=== false")
      }
      println()
    }
    println(s"testPairs score: $testScore")

    val testSorted = sort(testPairs)
    println(testSorted)

    val ins = new FileInputStream("advent-2022/input-13.txt")
    val input = IOUtils.toString(ins, StandardCharsets.UTF_8)
    ins.close()
    val pairs = readValues(input)
    var score = 0
    pairs.indices.foreach { idx =>
      val p = pairs(idx)
      println(s"${p._1}\n${p._2}")
      if (p._1.compareTo(p._2) <= 0) {
        println("=== true")
        score += idx + 1
      } else {
        println("=== false")
      }
      println()
    }
    println(s"pairs score: $score")

    val sorted = sort(pairs)
    println(sorted)

  }


}
