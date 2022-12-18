package org.ditw.pise5.advent2022

import org.apache.commons.io.IOUtils

import java.io.FileInputStream
import java.nio.charset.StandardCharsets

object Day08 {
  private val MaxHeightForEdge = -1

  class Node(val height: Int) {
    private var left: Option[Node] = None
    private var right: Option[Node] = None
    private var top: Option[Node] = None
    private var bottom: Option[Node] = None
    private var leftMax: Option[Int] = None
    private var rightMax: Option[Int] = None
    private var topMax: Option[Int] = None
    private var bottomMax: Option[Int] = None

    def setLeft(l: Node): Unit = left = Some(l)
    def setRight(r: Node): Unit = right = Some(r)
    def setTop(t: Node): Unit = top = Some(t)
    def setBottom(b: Node): Unit = bottom = Some(b)

    private def getOrUpdate(max: Option[Int], updateOps: Option[Int] => Unit): Int = {
      if (max.isEmpty) {
        val m =
          if (left.nonEmpty) {
            left.get.maxFromLeft
          } else {
            MaxHeightForEdge
          }
        updateOps(Some(m))
      }

      max.get
    }
    def getLeftMax: Int = {
      getOrUpdate(leftMax, max => leftMax = max)
    }

    private def maxFrom(neighbor: Option[Node], getMaxFrom: Node => Int): Int = {
      if (neighbor.nonEmpty) {
        Math.max(getMaxFrom(neighbor.get), height)
      } else {
        height
      }
    }

    private def visibleCountFrom(h: Int, n: Option[Node], getNextNode: Node => Option[Node]): Int = {
      if (n.nonEmpty) {
        if (n.get.height >= h) {
          1
        } else {
          1 + visibleCountFrom(h, getNextNode(n.get), getNextNode)
        }
      } else {
        0
      }
    }

    def maxFromLeft: Int = maxFrom(left, _.maxFromLeft)
    def visibleCountFromLeft: Int = visibleCountFrom(height, left, _.left)

    def getRightMax: Int = {
      getOrUpdate(rightMax, max => rightMax = max)
    }
    def maxFromRight: Int = maxFrom(right, _.maxFromRight)
    def visibleCountFromRight: Int = visibleCountFrom(height, right, _.right)

    def getTopMax: Int = {
      getOrUpdate(topMax, max => topMax = max)
    }
    def maxFromTop: Int = maxFrom(top, _.maxFromTop)
    def visibleCountFromTop: Int = visibleCountFrom(height, top, _.top)

    def getBottomMax: Int = {
      getOrUpdate(bottomMax, max => bottomMax = max)
    }
    def maxFromBottom: Int = maxFrom(bottom, _.maxFromBottom)
    def visibleCountFromBottom: Int = visibleCountFrom(height, bottom, _.bottom)

    def visibleScore: Int =
      visibleCountFromLeft * visibleCountFromRight * visibleCountFromTop * visibleCountFromBottom

    private def isVisibleFrom(n: Option[Node], getMax: Node => Int): Boolean = {
      if (n.nonEmpty) {
        getMax(n.get) < height
      } else {
        true
      }
    }

    def isVisibleFromLeft: Boolean = isVisibleFrom(left, _.maxFromLeft)
    def isVisibleFromRight: Boolean = isVisibleFrom(right, _.maxFromRight)
    def isVisibleFromTop: Boolean = isVisibleFrom(top, _.maxFromTop)
    def isVisibleFromBottom: Boolean = isVisibleFrom(bottom, _.maxFromBottom)

    def isVisible: Boolean =
      isVisibleFromLeft || isVisibleFromRight || isVisibleFromTop || isVisibleFromBottom

    override def toString: String = {
      val b = new StringBuilder
      if (isVisibleFromLeft) b.append("L ")
      if (isVisibleFromRight) b.append("R ")
      if (isVisibleFromTop) b.append("T ")
      if (isVisibleFromBottom) b.append("B ")
      val visibleInfo = b.toString().trim
      val visibleCounts = s"$visibleCountFromLeft $visibleCountFromRight $visibleCountFromTop $visibleCountFromBottom"
      s"$height ($visibleInfo) ($visibleCounts)"
    }
  }

  private def buildGrid(input: String): Array[Array[Node]] = {
    val heightGrid = input.split("[\\r\\n]+")
      .map(_.trim).filter(_.nonEmpty)
      .map(_.toCharArray)

    val gridHeight = heightGrid.length
    val gridWidths = heightGrid.map(_.length).toSet
    if (gridWidths.size != 1) {
      throw new IllegalArgumentException(s"Variant widths: $gridWidths")
    }

    val gridWidth = gridWidths.head
    val nodeGrid = (0 until gridHeight).map { h =>
      (0 until gridWidth).map { w =>
        Node(heightGrid(h)(w).toInt)
      }.toArray
    }.toArray

    (0 until gridHeight).foreach { h =>
      (0 until gridWidth).foreach { w =>
        val n = nodeGrid(h)(w)
        if (w > 0) n.setLeft(nodeGrid(h)(w-1))
        if (w < gridWidth-1) n.setRight(nodeGrid(h)(w+1))
        if (h > 0) n.setTop(nodeGrid(h-1)(w))
        if (h < gridHeight-1) n.setBottom(nodeGrid(h+1)(w))
      }
    }
    nodeGrid
  }

  def main(args: Array[String]): Unit = {

    val testInput = """30373
                      |25512
                      |65332
                      |33549
                      |35390""".stripMargin

    val testNodeGrid = buildGrid(testInput)

    val testAllVisibleCount = testNodeGrid.flatMap(_.map(_.isVisible)).count(x => x)
    println(s"testAllVisibleCount: $testAllVisibleCount")

    val ins = new FileInputStream("advent-2022/input-08.txt")
    val input = IOUtils.toString(ins, StandardCharsets.UTF_8)
    ins.close()

    val nodeGrid = buildGrid(input)

    val allVisibleCount = nodeGrid.flatMap(_.map(_.isVisible)).count(x => x)
    println(s"allVisibleCount: $allVisibleCount")

    val maxVisibleScore = nodeGrid.flatMap(_.map(_.visibleScore)).max
    println(s"maxVisibleScore: $maxVisibleScore")
  }
}
