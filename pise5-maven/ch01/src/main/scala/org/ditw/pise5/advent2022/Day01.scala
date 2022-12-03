package org.ditw.pise5.advent2022

import org.apache.commons.io.IOUtils

import java.io.{ByteArrayInputStream, FileInputStream, InputStream}
import java.nio.charset.StandardCharsets

object Day01 {

  private def run(ins: InputStream): Map[Int, Long] = {
    import collection.JavaConverters._
    val inputLines = IOUtils.readLines(ins, StandardCharsets.UTF_8).asScala
    var elfIndex = 0
    import collection.mutable
    val elfCalories = mutable.Map[Int, Long]()
    var maxCalory = Long.MinValue
    var currElfCalory = 0L
    inputLines.foreach { line =>
      if (line.trim.isEmpty) {
        if (currElfCalory > maxCalory) {
          maxCalory = currElfCalory
        }
        elfCalories.put(elfIndex, currElfCalory)
        currElfCalory = 0
        elfIndex += 1
      } else {
        currElfCalory += line.toLong
      }
    }
    // the last elf
    if (currElfCalory > maxCalory) {
      maxCalory = currElfCalory
    }
    elfCalories.put(elfIndex, currElfCalory)

    elfCalories.toMap
  }

  private def run(strInput: String): Unit = {
    val byteArrIns = new ByteArrayInputStream(strInput.getBytes(StandardCharsets.UTF_8))
    val elfCalories = run(byteArrIns)
    val sorted = elfCalories.toSeq.sortBy(_._2)(Ordering[Long].reverse)
    val top3 = sorted.take(3)
    println(top3.mkString("\t", "\n\t", ""))
    println(s"---------------------------------")
    println(s"  Total: ${top3.map(_._2).sum}")
    println()
  }

  def main(args: Array[String]): Unit = {
    //    if (args.isEmpty) {
    //      println("Input file expected.")
    //    } else {
    //      val fileName = args(0)
    //      val fileInStream = new FileInputStream(fileName)
    //      val (elfIndex, maxCalory) = run(fileInStream)
    //      fileInStream.close()
    //      println(s"Elf index: $elfIndex, max calory: $maxCalory")
    //    }

    run(
      """1000
        |2000
        |3000
        |
        |4000
        |
        |5000
        |6000
        |
        |7000
        |8000
        |9000
        |
        |10000""".stripMargin
    )

    val ins = new FileInputStream("C:\\tmp\\advent-code\\input-day01.txt")
    val str = IOUtils.toString(ins, StandardCharsets.UTF_8)
    ins.close()
    run(str)
  }
}
