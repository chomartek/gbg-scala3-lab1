package org.ditw.pise5.advent2022

import org.apache.commons.io.IOUtils

import java.io.FileInputStream
import java.nio.charset.StandardCharsets

object Day06 {
  private val PacketMarkerLength = 4
  private val MessageMarkerLength = 14
  private def findPackageMarker(input: String): Int = {
    findMarker(input, PacketMarkerLength)
  }
  private def findMessageMarker(input: String): Int = {
    findMarker(input, MessageMarkerLength)
  }
  private def findMarker(input: String, markerLength: Int): Int = {
    val chars = input.toCharArray
    val res = chars.indices.find { idx =>
      if (idx < chars.length-markerLength) {
        val slice = chars.slice(idx, idx+markerLength)
        slice.toSet.size == markerLength
      } else {
        false
      }
    }

    if (res.nonEmpty) {
      res.get + markerLength
    } else {
      throw new IllegalArgumentException("Found non!")
    }
  }

  def main(args: Array[String]): Unit = {
    println(findPackageMarker("mjqjpqmgbljsphdztnvjfqwrcgsmlb"))
    println(findPackageMarker("bvwbjplbgvbhsrlpgdmjqwftvncz"))
    println(findPackageMarker("nppdvjthqldpwncqszvftbrmjlhg"))
    println(findPackageMarker("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"))
    println(findPackageMarker("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"))
    println(findMessageMarker("mjqjpqmgbljsphdztnvjfqwrcgsmlb"))
    println(findMessageMarker("bvwbjplbgvbhsrlpgdmjqwftvncz"))
    println(findMessageMarker("nppdvjthqldpwncqszvftbrmjlhg"))
    println(findMessageMarker("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"))
    println(findMessageMarker("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"))

    val ins = new FileInputStream("advent-2022/input-06.txt")
    val content = IOUtils.toString(ins, StandardCharsets.UTF_8)
    ins.close()
    println(findPackageMarker(content))

    println(findMessageMarker(content))

  }
}
