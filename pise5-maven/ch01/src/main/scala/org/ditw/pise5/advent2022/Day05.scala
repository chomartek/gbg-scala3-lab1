package org.ditw.pise5.advent2022

import org.apache.commons.io.IOUtils

import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import scala.collection.mutable.ListBuffer

object Day05 {
  class Stack(chars: Char*) {
    def this(str: String) = this(
      str
        .split("[\\r\\n]")
        .map(_.trim)
        .filter(_.nonEmpty)
        .map(_.head)
        .reverse: _*
    )
    private val buffer = ListBuffer(chars: _*)

    def remove(): Char = {
      val res = buffer.last
      buffer.remove(buffer.size-1)
      res
    }

    def add(ch: Char): Unit = {
      buffer += ch
    }

    def head: Char = buffer.last


    def apply(idx: Int): Char = buffer(idx)

    def size: Int = buffer.size

    def copy(): Stack = new Stack(buffer.toArray: _*)
  }

  class StackState(private val state: Map[Int, Stack]) {
    def move(count: Int, srcStackIndex: Int, dstStackIndex: Int): StackState = {
      val newSrcStack = state(srcStackIndex).copy()
      val newDstStack = state(dstStackIndex).copy()

      (0 until count).foreach { _ =>
        val ch = newSrcStack.remove()
        newDstStack.add(ch)
      }
      import collection.mutable
      val res = mutable.Map[Int, Stack](state.toList: _*)
      res(srcStackIndex) = newSrcStack
      res(dstStackIndex) = newDstStack

      new StackState(res.toMap)
    }

    def move2(count: Int, srcStackIndex: Int, dstStackIndex: Int): StackState = {
      val newSrcStack = state(srcStackIndex).copy()
      val newDstStack = state(dstStackIndex).copy()

      val tmpStack = Stack()
      (0 until count).foreach { _ =>
        val ch = newSrcStack.remove()
        tmpStack.add(ch)
      }
      (0 until count).foreach { _ =>
        val ch = tmpStack.remove()
        newDstStack.add(ch)
      }
      import collection.mutable
      val res = mutable.Map[Int, Stack](state.toList: _*)
      res(srcStackIndex) = newSrcStack
      res(dstStackIndex) = newDstStack

      new StackState(res.toMap)
    }

    def show(): Unit = {
      val maxHeight = state.values.map(_.size).max
      val sortedIndex = state.keys.toList.sorted
      (0 to maxHeight - 1).foreach { idx =>
        sortedIndex.foreach { stackIndx =>
          val crateIndex = maxHeight - 1 - idx
          showCrate(state(stackIndx), crateIndex)
        }
        println()
      }
      sortedIndex.foreach(idx => print(s" $idx  "))
      println()
    }

    def getTopCrates(): List[Char] = {
      val sortedIndex = state.keys.toList.sorted
      sortedIndex.map(idx => state(idx).head)
    }
  }

  private val InitState = new StackState(Map(
      1 -> Stack(
        """D
          |Z
          |T
          |H
          |""".stripMargin
      ),
      2 -> Stack(
        """S
          |C
          |G
          |T
          |W
          |R
          |Q
          |""".stripMargin
      ),
      3 -> Stack(
        """H
          |C
          |R
          |N
          |Q
          |F
          |B
          |P
          |""".stripMargin
      ),
      4 -> Stack(
        """Z
          |H
          |F
          |N
          |C
          |L
          |""".stripMargin
      ),
      5 -> Stack(
        """S
          |Q
          |F
          |L
          |G
          |""".stripMargin
      ),
      6 -> Stack(
        """S
          |C
          |R
          |B
          |Z
          |W
          |P
          |V
          |""".stripMargin
      ),
      7 -> Stack(
        """J
          |F
          |Z
          |""".stripMargin
      ),
      8 -> Stack(
        """Q
          |H
          |R
          |Z
          |V
          |L
          |D
          |""".stripMargin
      ),
      9 -> Stack(
        """D
          |L
          |Z
          |F
          |N
          |G
          |H
          |B
          |""".stripMargin
      )
    )
  )

  private def showCrate(stack: Stack, crateIndex: Int): Unit = {
    val str =
      if (crateIndex >= stack.size) {
        "    "
      } else {
        s"[${stack(crateIndex)}] "
      }
    print(str)
  }

  private def extractLine(line: String): (Int, Int, Int) = {
    val ptn = """move (\d+) from (\d+) to (\d+)""".r
    val ptn(count, from, to) = line
    (count.toInt, from.toInt, to.toInt)
  }

  private def processMoves(
    initState: StackState, lines: Iterable[String],
    moveOp: (StackState, Int, Int, Int) => StackState
  ): StackState = {
    val moves = lines
      .map(_.trim)
      .filter(_.nonEmpty)
      .filter(_.startsWith("move"))
      .map(extractLine)
    var currState = initState
    moves.foreach { move =>
      val (count, src, dst) = move
      currState = moveOp(currState, count, src, dst)
    }
    currState
  }

  private def processMoves1(initState: StackState, lines: Iterable[String]): StackState = {
    processMoves(initState, lines,
      (currState, count, src, dst) => currState.move(count, src, dst)
    )
  }

  private def processMoves2(initState: StackState, lines: Iterable[String]): StackState = {
    processMoves(initState, lines,
      (currState, count, src, dst) => currState.move2(count, src, dst)
    )
  }
  def main(args: Array[String]): Unit = {
    val testState = new StackState(Map(
      1 -> Stack(
        """N
          |Z""".stripMargin),
      2 -> Stack(
        """D
          |C
          |M""".stripMargin),
      3 -> Stack(
        """P"""
      )
    ))
    testState.show()
    val t1State = testState.move(1, 2, 1)
    t1State.show()

    InitState.show()

    println(extractLine("move 14 from 5 to 4"))

    val ins = new FileInputStream("advent-2022/input-05.txt")
    val lines = IOUtils.readLines(ins, StandardCharsets.UTF_8)
    ins.close()
    import collection.JavaConverters._
    val endState1 = processMoves1(InitState, lines.asScala)
    endState1.show()
    println(endState1.getTopCrates().mkString)

    val endState2 = processMoves2(InitState, lines.asScala)
    endState2.show()
    println(endState2.getTopCrates().mkString)
  }
}
