package org.ditw.pise5.advent2022

import org.apache.commons.io.IOUtils

import java.io.FileInputStream
import java.nio.charset.StandardCharsets

object Day10 {

  case class CycleState(cycle: Int, v: Int, endValue: Int) {
    def signalStrength: Int = cycle * v
  }

  trait Opr {
    def run(currState: CycleState): Seq[CycleState]
  }

  private val Noop = new Opr {
    override def run(currState: CycleState): Seq[CycleState] = {
      Seq(
        CycleState(currState.cycle+1, currState.endValue, currState.endValue)
      )
    }
  }

  private def addx(x: Int) = new Opr {
    override def run(currState: CycleState): Seq[CycleState] = {
      Seq(
        CycleState(currState.cycle+1, currState.endValue, currState.endValue),
        CycleState(currState.cycle+2, currState.endValue, currState.endValue + x)
      )
    }
  }

  private def parseOpr(oprStr: String): Opr = {
    if (oprStr.trim == "noop") {
      Noop
    } else {
      val ptn = "addx\\h+(.*)".r
      val ptn(x) = oprStr
      addx(x.toInt)
    }
  }

  private def draw(states: Seq[CycleState]): Unit = {
    (0 until 6).foreach { row =>
      var crtPos: Int = 0
      (1 to 40).foreach { x =>
        val cycle = row*40 + x
        val state = states.filter(_.cycle == cycle).head
        if (Math.abs(crtPos - state.v) <= 1) {
          print("#")
        } else {
          print(".")
        }
        crtPos += 1
//        if (state.nonEmpty) {
//
//          spitePos = state.get.endValue
//
//        } else {
//          throw new IllegalStateException(s"State for cycle $cycle not found")
//        }
      }
      println()
    }


  }

  private def process(oprInput: String): Unit = {
    val oprStrs = oprInput.split("[\\r\\n]+")
      .map(_.trim)
      .filter(_.nonEmpty)

    val states: Seq[CycleState] = oprStrs.map(parseOpr)
      .foldLeft(
        Seq(CycleState(0, 1, 1))
      )(
        (seq: Seq[CycleState], opr: Opr) => seq ++ opr.run(seq.last)
      )

    var cycles = Set(20, 60, 100, 140, 180, 220)
    var filteredState = states.filter(s => cycles.contains(s.cycle))
//    cycles = (1 to 20).toSet
//    filteredState = states.filter(s => cycles.contains(s.cycle))
    println(filteredState.mkString("\n"))
    val strengths = filteredState.map(_.signalStrength)
    println(s"Strengths: $strengths")
    println(s"      Sum: ${strengths.sum}")
    draw(states)
  }

  def main(args: Array[String]): Unit = {
    val testOprInput = """addx 15
                         |addx -11
                         |addx 6
                         |addx -3
                         |addx 5
                         |addx -1
                         |addx -8
                         |addx 13
                         |addx 4
                         |noop
                         |addx -1
                         |addx 5
                         |addx -1
                         |addx 5
                         |addx -1
                         |addx 5
                         |addx -1
                         |addx 5
                         |addx -1
                         |addx -35
                         |addx 1
                         |addx 24
                         |addx -19
                         |addx 1
                         |addx 16
                         |addx -11
                         |noop
                         |noop
                         |addx 21
                         |addx -15
                         |noop
                         |noop
                         |addx -3
                         |addx 9
                         |addx 1
                         |addx -3
                         |addx 8
                         |addx 1
                         |addx 5
                         |noop
                         |noop
                         |noop
                         |noop
                         |noop
                         |addx -36
                         |noop
                         |addx 1
                         |addx 7
                         |noop
                         |noop
                         |noop
                         |addx 2
                         |addx 6
                         |noop
                         |noop
                         |noop
                         |noop
                         |noop
                         |addx 1
                         |noop
                         |noop
                         |addx 7
                         |addx 1
                         |noop
                         |addx -13
                         |addx 13
                         |addx 7
                         |noop
                         |addx 1
                         |addx -33
                         |noop
                         |noop
                         |noop
                         |addx 2
                         |noop
                         |noop
                         |noop
                         |addx 8
                         |noop
                         |addx -1
                         |addx 2
                         |addx 1
                         |noop
                         |addx 17
                         |addx -9
                         |addx 1
                         |addx 1
                         |addx -3
                         |addx 11
                         |noop
                         |noop
                         |addx 1
                         |noop
                         |addx 1
                         |noop
                         |noop
                         |addx -13
                         |addx -19
                         |addx 1
                         |addx 3
                         |addx 26
                         |addx -30
                         |addx 12
                         |addx -1
                         |addx 3
                         |addx 1
                         |noop
                         |noop
                         |noop
                         |addx -9
                         |addx 18
                         |addx 1
                         |addx 2
                         |noop
                         |noop
                         |addx 9
                         |noop
                         |noop
                         |noop
                         |addx -1
                         |addx 2
                         |addx -37
                         |addx 1
                         |addx 3
                         |noop
                         |addx 15
                         |addx -21
                         |addx 22
                         |addx -6
                         |addx 1
                         |noop
                         |addx 2
                         |addx 1
                         |noop
                         |addx -10
                         |noop
                         |noop
                         |addx 20
                         |addx 1
                         |addx 2
                         |addx 2
                         |addx -6
                         |addx -11
                         |noop
                         |noop
                         |noop""".stripMargin
    process(testOprInput)
    println("---------------------")

    val ins = new FileInputStream("advent-2022/input-10.txt")
    val input = IOUtils.toString(ins, StandardCharsets.UTF_8)
    ins.close()

    process(input)
  }
}
