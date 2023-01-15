package org.ditw.pise5.advent2022

import org.apache.commons.io.IOUtils

import java.io.FileInputStream
import java.nio.charset.StandardCharsets

object Day14 {
  case class Coord(x: Int, y: Int) {
    override def toString: String = s"$x,$y"
  }

  case class Line(start: Coord, end: Coord) {
    override def toString: String = s"$start -> $end"
    val coords: Set[Coord] = {
      if (start.x == end.x) {
        val min = Math.min(start.y, end.y)
        val max = Math.max(start.y, end.y)
        (min to max).map(y => Coord(start.x, y)).toSet
      } else if (start.y == end.y) {
        val min = Math.min(start.x, end.x)
        val max = Math.max(start.x, end.x)
        (min to max).map(x => Coord(x, start.y)).toSet
      } else {
        throw new IllegalArgumentException(s"Error state: $start - $end")
      }
    }
  }

  case class ParsedLine(lines: List[Line]) {
    override def toString: String = {
      val startCoords = lines.map(_.start)
      val firstPart = startCoords.map(c => s"$c -> ")
      firstPart.mkString + lines.last.end.toString
    }

    val coords: Set[Coord] = lines.flatMap(_.coords).toSet

    def maxX: Int = lines.map(l => Math.max(l.start.x, l.end.x)).max
    def maxY: Int = lines.map(l => Math.max(l.start.y, l.end.y)).max
    def minX: Int = lines.map(l => Math.min(l.start.x, l.end.x)).min
    def minY: Int = lines.map(l => Math.min(l.start.y, l.end.y)).min
  }

  class Grid1(val parsedLines: List[ParsedLine]) {

    val coords: Set[Coord] = parsedLines.flatMap(_.coords).toSet
    import collection.mutable
    private val sandCoords: mutable.Set[Coord] = {
      val r = mutable.Set[Coord]()
      r ++= parsedLines.flatMap(_.coords)
    }

    def maxX: Int = parsedLines.map(_.maxX).max
    def maxY: Int = parsedLines.map(_.maxY).max
    def minX: Int = parsedLines.map(_.minX).min
    def minY: Int = parsedLines.map(_.minY).min

    def traceLines: String = {
      val lines = parsedLines.map(_.toString).mkString("\n")
      val maxMin = s"x: ($minX,$minY) -> ($maxX,$maxY)"
      s"$lines\n$maxMin"
    }

    private def isOccupied(coord: Coord): Boolean = {
      coords.contains(coord) || sandCoords.contains(coord)
    }

    private def stepOne(sandCoord: Coord): Option[Coord] = {
      val down = sandCoord.copy(y = sandCoord.y + 1)
      if (!isOccupied(down)) {
        Some(down)
      } else {
        val downLeft = Coord(sandCoord.x-1, sandCoord.y + 1)
        if (!isOccupied(downLeft)) {
          Some(downLeft)
        } else {
          val downRight = Coord(sandCoord.x+1, sandCoord.y + 1)
          if (!isOccupied(downRight)) {
            Some(downRight)
          } else {
            // rest
            sandCoords += sandCoord
            None
          }
        }
      }
    }

    private def outOfGrid(coord: Coord): Boolean = {
      coord.x < minX || coord.x > maxX || coord.y > maxY
    }
    private def dropOne: Boolean = {
      val startCoord = Coord(500, 0)
      var currCoord = Option(startCoord)
      while (currCoord.nonEmpty && !outOfGrid(currCoord.get)) {
        currCoord = stepOne(currCoord.get)
      }
      currCoord.isEmpty // false if out of grid
    }

    def run: Unit = {
      var sandNo = 1
      while (dropOne) {
        println(s"============= sandNo $sandNo")
        // show
        sandNo += 1
      }
      show
      println(s"Done!")
    }

    def run2: Unit = {
      var sandNo = 1
      while (dropOne) {
        println(s"============= sandNo $sandNo")
        // show
        sandNo += 1
      }
      show
      println(s"Done!")
    }

    def show: Unit = {
      val xDist = 500 - minX
      val dashes = (0 until (xDist - 3)).map(_ => '-').mkString
      println(s"----$minX${dashes}o---------------------------------------")
      (0 to maxY).foreach { y =>
        print(f"$y%03d")
        (minX-1 to maxX).foreach { x =>
          val coord = Coord(x, y)
          if (x < minX) {
            print(' ')
          } else if (coords.contains(coord)) {
            print("#")
          } else if (sandCoords.contains(coord)) {
            print("o")
          } else {
            print(".")
          }
        }
//        if (y >= minY) {
//
//        }
        println()
      }
      println("-----------------------------------------")
    }
  }

  class Grid2(_parsedLines: List[ParsedLine]) {
    private var maxX: Int = _parsedLines.map(_.maxX).max
    private var maxY: Int = _parsedLines.map(_.maxY).max
    private var minX: Int = _parsedLines.map(_.minX).min
    private var minY: Int = _parsedLines.map(_.minY).min
    private val parsedLines = {
      maxY = maxY + 2
//      minX = 450
//      maxX = 550
      val r = _parsedLines :+ ParsedLine(List(Line(Coord(minX, maxY), Coord(maxX, maxY))))
      r
    }

    val coords: Set[Coord] = parsedLines.flatMap(_.coords).toSet
    import collection.mutable
    private val sandCoords: mutable.Set[Coord] = {
      val r = mutable.Set[Coord]()
      r ++= parsedLines.flatMap(_.coords)
    }

    def traceLines: String = {
      val lines = parsedLines.map(_.toString).mkString("\n")
      val maxMin = s"x: ($minX,$minY) -> ($maxX,$maxY)"
      s"$lines\n$maxMin"
    }

    private def isOccupied(coord: Coord): Boolean = {
      coords.contains(coord) || sandCoords.contains(coord) || coord.y == maxY
    }

    private def stepOne2(sandCoord: Coord): Option[Coord] = {
      val down = sandCoord.copy(y = sandCoord.y + 1)
      if (!isOccupied(down)) {
        Some(down)
      } else {
        val downLeft = Coord(sandCoord.x-1, sandCoord.y + 1)
        if (!isOccupied(downLeft)) {
          Some(downLeft)
        } else {
          val downRight = Coord(sandCoord.x+1, sandCoord.y + 1)
          if (!isOccupied(downRight)) {
            Some(downRight)
          } else {
            // rest
            sandCoords += sandCoord
            None
          }
        }
      }
    }

    private def expandGridIfNeeded(coord: Coord): Unit = {
      if (coord.x < minX)
        minX = coord.x
      if (coord.x > maxX)
        maxX = coord.x
    }
    private def dropOne2: Boolean = {
      val startCoord = Coord(500, 0)
      val step1 = stepOne2(startCoord)

      if (step1.isEmpty) {
        false
      } else {
        var currCoord = step1

        while (currCoord.nonEmpty) {
          expandGridIfNeeded(currCoord.get)
          currCoord = stepOne2(currCoord.get)
        }
//        if (currCoord.nonEmpty) {
//          throw new IllegalStateException("todo")
//        }
        true
      }

    }

//    def run: Unit = {
//      var sandNo = 1
//      while (dropOne2) {
//        println(s"============= sandNo $sandNo")
//        // show
//        sandNo += 1
//      }
//      show
//      println(s"Done: sandNo: ${sandNo-1}")
//    }

    def run2: Unit = {
      var sandNo = 1
      while (dropOne2) {
        println(s"============= sandNo $sandNo")
        // show
        sandNo += 1
      }
      show
      println(s"Done: sandNo: ${sandNo-1}")
    }

    def show: Unit = {
      val xDist = 500 - minX
      val dashes = (0 until (xDist - 3)).map(_ => '-').mkString
      println(s"----$minX${dashes}o---------------------------------------")
      (0 to maxY).foreach { y =>
        print(f"$y%03d")
        (minX-1 to maxX).foreach { x =>
          val coord = Coord(x, y)
          if (x < minX) {
            print(' ')
          } else if (coords.contains(coord) || coord.y == maxY) {
            print("#")
          } else if (sandCoords.contains(coord)) {
            print("o")
          } else {
            print(".")
          }
        }
        //        if (y >= minY) {
        //
        //        }
        println()
      }
      println("-----------------------------------------")
    }
  }

  private def parseLines(inputLine: String): ParsedLine = {
    val coordStrs = inputLine.split("[->]").map(_.trim).filter(_.nonEmpty)
    val coords = coordStrs.map { coordStr =>
      val parts = coordStr.split(",").map(_.trim).filter(_.nonEmpty)
      Coord(parts(0).toInt, parts(1).toInt)
    }

    val lines = (0 until coords.length-1).map { idx =>
      Line(coords(idx), coords(idx+1))
    }
    ParsedLine(lines.toList)
  }

  private def parseGrid(input: String): Grid1 = {
    val inputLines = input.split("[\\r\\n]+").map(_.trim).filter(_.nonEmpty)
    val parsedLines = inputLines.map(parseLines)
    Grid1(parsedLines.toList)
  }

  private def parseGrid2(input: String): Grid2 = {
    val inputLines = input.split("[\\r\\n]+").map(_.trim).filter(_.nonEmpty)
    val parsedLines = inputLines.map(parseLines)
    Grid2(parsedLines.toList)
  }

  private def problem1(): Unit = {
    val testGrid = parseGrid(
      """498,4 -> 498,6 -> 496,6
        |503,4 -> 502,4 -> 502,9 -> 494,9""".stripMargin
    )

    println(testGrid.traceLines)
    testGrid.show
    testGrid.run

//    val ins = new FileInputStream("advent-2022/input-14.txt")
//    val input = IOUtils.toString(ins, StandardCharsets.UTF_8)
//    ins.close()
//    val grid = parseGrid(input)
//    println(grid.traceLines)
//    grid.show
//    grid.run
  }

  private def problem2(): Unit = {
    val testGrid2 = parseGrid2(
      """498,4 -> 498,6 -> 496,6
        |503,4 -> 502,4 -> 502,9 -> 494,9""".stripMargin
    )

    println(testGrid2.traceLines)
    testGrid2.show
    testGrid2.run2

    val ins = new FileInputStream("advent-2022/input-14.txt")
    val input = IOUtils.toString(ins, StandardCharsets.UTF_8)
    ins.close()
    val grid2 = parseGrid2(input)
    println(grid2.traceLines)
    grid2.show
    grid2.run2
  }

  def main(args: Array[String]): Unit = {
    problem2()
    // problem1()
  }
}
