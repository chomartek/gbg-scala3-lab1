package org.ditw.pise5.advent2022

import org.apache.commons.io.IOUtils

import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import javax.swing.text.StyledEditorKit.BoldAction

object Day09 {

  case class Distance(xdiff: Int, ydiff: Int)
  case class Pos(x: Int, y: Int) {
    def distance(to: Pos): Distance = Distance(Math.abs(x - to.x), Math.abs(y - to.y))

    def move(xmove: Int, ymove: Int): Pos = Pos(x+xmove, y+ymove)
    def move(dist: Distance): Pos = Pos(x+dist.xdiff, y+dist.ydiff)
  }

  import collection.mutable
  class PosList(count: Int, board: Board) {
    private val posList = mutable.Seq((0 until count).map(_ => Pos(0, 0)): _*)

    private val tailFootprints = mutable.Set[Pos](Pos(0, 0))

    private def updateTailPos(pos: Pos): Unit = {
      posList(posList.length-1) = pos
      tailFootprints.add(pos)
    }

    def getTailFootprints: Set[Pos] = tailFootprints.toSet

    private def follow(): Unit = {

      (0 until posList.size - 1).foreach { idx =>
        val currHeadPos = posList(idx)
        val currTailPos = posList(idx+1)
        val distance = currTailPos.distance(currHeadPos)
        val shouldFollow = distance.xdiff > 1 || distance.ydiff > 1
        if (shouldFollow) {
          val xmove = if (distance.xdiff > 0) {
            if (currHeadPos.x > currTailPos.x) 1 else -1
          } else 0
          val ymove = if (distance.ydiff > 0) {
            if (currHeadPos.y > currTailPos.y) 1 else -1
          } else 0

          val newCurrTailPos = currTailPos.move(xmove, ymove)
          if (idx == posList.size-2) {
            updateTailPos(newCurrTailPos)
          } else {
            posList(idx+1) = newCurrTailPos
          }
        } else {
          // do nothing
        }
      }
    }

    def moveHead(dir: String, steps: Int): Unit = {
      val moveStep = dir match {
        case "R" => Distance(1, 0)
        case "L" => Distance(-1, 0)
        case "U" => Distance(0, 1)
        case "D" => Distance(0, -1)
      }

      (0 until steps).foreach { _ =>
        val newHeadPos = posList.head.move(moveStep)
        board.expandIfNeeded(newHeadPos)
        posList(0) = newHeadPos
        follow()
        //        if (contains(newHeadPos)) {
        //
        //        }
      }
    }

    def posTrace(x: Int, y: Int): String = {
      val pos = Pos(x, y)
      val foundIdx = posList.indices.find(idx => posList(idx) == pos)
      if (foundIdx.nonEmpty) {
        if (foundIdx.get == 0) "H"
        else if (foundIdx.get == count-1) "T"
        else foundIdx.get.toString
      } else {
        "."
      }
    }
  }

  class Board(count: Int) {
    private var xmax: Int = 0
    private var xmin: Int = 0
    private var ymax: Int = 0
    private var ymin: Int = 0
    var posList: PosList = PosList(count, this)


    def trace: Unit = {
      println("-------------------")
      (ymin until ymax).foreach { h =>
        (xmin until xmax).foreach { w =>
          print(posList.posTrace(w, ymin + (ymax-1 - h)) + ' ')
        }
        println()
      }
    }



    def expandIfNeeded(pos: Pos): Unit = {
      if (pos.x >= xmax) {
        xmax = pos.x + 1
      } else if (pos.x < xmin) {
        xmin = pos.x
      }
      if (pos.y >= ymax) {
        ymax = pos.y + 1
      } else if (pos.y < ymin) {
        ymin = pos.y
      }
    }

//    private def contains(pos: Pos): Boolean = {
//      pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height
//    }
  }

  private def runSteps(board: Board, input: String): Unit = {

    val lines = input.split("[\\r\\n]+")
      .map(_.trim)
      .filter(_.nonEmpty)
      .foreach { line =>
        val ptn = "(\\w) (\\d+)".r
        val ptn(dir, steps) = line
        board.posList.moveHead(dir, steps.toInt)
//        board.trace
//        println(s"$dir $steps")
      }

    board.trace
    println(board.posList.getTailFootprints.size)
  }

  def main(args: Array[String]): Unit = {
//
//    val testInput = """R 4
//                      |U 4
//                      |L 3
//                      |D 1
//                      |R 4
//                      |D 1
//                      |L 5
//                      |R 2""".stripMargin
//    runSteps(new Board(2), testInput)

//    val ins = new FileInputStream("advent-2022/input-09.txt")
//    val input = IOUtils.toString(ins, StandardCharsets.UTF_8)
//    ins.close()
//
//    runSteps(new Board(2), input)
//    runSteps(new Board(10), testInput)

    val testInput2 = """R 5
                      |U 8
                      |L 8
                      |D 3
                      |R 17
                      |D 10
                      |L 25
                      |U 20""".stripMargin
    runSteps(new Board(10), testInput2)

    val ins = new FileInputStream("advent-2022/input-09.txt")
    val input = IOUtils.toString(ins, StandardCharsets.UTF_8)
    ins.close()

    runSteps(new Board(10), input)
  }
}
