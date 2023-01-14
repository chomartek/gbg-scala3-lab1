package org.ditw.pise5.advent2022

object Day15 {
  case class Coord(x: Int, y: Int) {
    def mdist(coord: Coord): Int = Math.abs(x - coord.x) + Math.abs(y - coord.y)
  }
  case class Beacon(coord: Coord)
  case class Sensor(coord: Coord, nearestBeacon: Beacon) {
    val coveredDist: Int = coord.mdist(nearestBeacon.coord)
    val minX: Int = coord.x - coveredDist
    val minY: Int = coord.y - coveredDist
    val maxX: Int = coord.x + coveredDist
    val maxY: Int = coord.y + coveredDist

    def notBeaconPos(coord: Coord): Boolean = {
      this.coord.mdist(coord) <= coveredDist
    }

    def coveredRanges(rangeX: Range, rangeY: Range): Map[Int, Range] = {
      rangeY.flatMap { y =>
        val distY = Math.abs(y - coord.y)
        if (distY > coveredDist) {
          None
        } else {
          val maxDistX = coveredDist - distY
          val start = Math.max(rangeX.min, coord.x - maxDistX)
          val end = Math.min(coord.x + maxDistX, rangeX.max)
          if (start > end) {
            println("dbg")
          }
          val r = start to end
          Some(y -> r)
        }
      }.toMap
    }
  }

  class SensorGrid(sensors: Array[Sensor]) {
    val minX: Int = sensors.map(_.minX).min
    val minY: Int = sensors.map(_.minY).min
    val maxX: Int = sensors.map(_.maxX).max
    val maxY: Int = sensors.map(_.maxY).max
    private val nearestBeacons = sensors.map(_.nearestBeacon.coord).toSet

    def notBeaconPos(coord: Coord): Boolean = {
      if (nearestBeacons.contains(coord)) {
        false
      } else {
        val coveredBy = sensors.find(_.notBeaconPos(coord))
        coveredBy.nonEmpty
      }
    }

    def checkY(y: Int): Int = {
      (minX to maxX).count { x =>
        val coord = Coord(x, y)
        val r = notBeaconPos(coord)
        println(s"$coord: $r")
        r
      }
    }

    def checkArea(rangeX: Range, rangeY: Range): Int = {
      rangeX.map { x =>
        rangeY.count { y =>
          val coord = Coord(x, y)
          val r = notBeaconPos(coord)
          if (!r && !nearestBeacons.contains(coord)) {
            println(s"$coord: $r")
          }
          r
        }
      }.sum
    }

    def hasOverlap(r1: Range, r2: Range): Boolean = {
      (r1.min <= r2.max && r1.min >= r2.min) || (r2.min <= r1.max && r2.min >= r1.min)
    }

    def nextToEachOther(r1: Range, r2: Range): Boolean = {
      r1.min == r2.max+1 || r2.min == r1.max+1
    }

    import collection.mutable
    def mergeRanges(ranges: Set[Range], newRange: Range): Set[Range] = {
      val currSet = mutable.Set[Range]()
      currSet ++= ranges
      var currNew = newRange
      var overlapRange = currSet.find { r => hasOverlap(r, currNew) }
      while (overlapRange.nonEmpty && !currSet.exists(_.containsSlice(overlapRange.get))) {
        currSet -= overlapRange.get
        val t = Math.min(currNew.min, overlapRange.get.min) to Math.max(currNew.max, overlapRange.get.max)
        currSet += t
        currNew = t
        overlapRange = currSet.find { r => hasOverlap(r, currNew) }
      }

      if (!currSet.exists(_.containsSlice(currNew))) {
        currSet += currNew
      }

      currSet.toSet
    }

    def cleanup(ranges: Iterable[Range]): Set[Range] = {
      val rangeSet = mutable.Set[Range]()

      var updatedSet = Set[Range]()
      var currSet = ranges.toSet

      while (updatedSet != currSet) {
        updatedSet = currSet
        currSet.foreach { r =>
          if (!rangeSet.exists(rr => hasOverlap(rr, r) || nextToEachOther(rr, r))) {
            rangeSet += r
          } else {
            val overlap = rangeSet.find(rr => hasOverlap(rr, r) || nextToEachOther(rr, r))
            if (overlap.nonEmpty) {
              rangeSet -= overlap.get
              rangeSet -= r
              rangeSet += Math.min(overlap.get.min, r.min) to Math.max(overlap.get.max, r.max)
            }
          }
        }
        currSet = rangeSet.toSet
      }
      updatedSet
    }

    def calcRanges(rangeX: Range, rangeY: Range): Map[Int, List[Range]] = {
      sensors.flatMap { s =>
        s.coveredRanges(rangeX, rangeY).toList
      }.groupBy(_._1)
        .mapValues { itRanges =>
          val rangeList = itRanges.map(_._2).toList
//          var currHead = rangeList.head
//          var currTail = rangeList.tail
//          var currSet = mutable.Set[Range](currHead)
//
//          while (currTail.nonEmpty) {
//            currHead = currTail.head
//            if (currSet.isEmpty) {
//              println("dbg")
//            }
//            val merged = mergeRanges(currSet.toSet, currHead)
//            currSet = mutable.Set()
//            currSet ++= merged
//            currTail = currTail.tail
//          }

          val res = cleanup(rangeList)
//          res = cleanup(res)
          // println(res)
          res.toList.sortBy(_.min)
        }.toMap
    }
  }

  private def parseLine(inputLine: String): Sensor = {
    // Sensor at x=1259754, y=1927417: closest beacon is at x=1174860, y=2000000
    val ptn = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".r
    val ptn(sx, sy, bx, by) = inputLine

    Sensor(Coord(sx.toInt, sy.toInt), Beacon(Coord(bx.toInt, by.toInt)))
  }

  private def parse(input: String): SensorGrid = {
    val lines = input.split("[\\r\\n+]").map(_.trim).filter(_.nonEmpty)
    new SensorGrid(lines.map(parseLine))
  }

  def main(args: Array[String]): Unit = {
    val testGrid = parse(
      """Sensor at x=2, y=18: closest beacon is at x=-2, y=15
        |Sensor at x=9, y=16: closest beacon is at x=10, y=16
        |Sensor at x=13, y=2: closest beacon is at x=15, y=3
        |Sensor at x=12, y=14: closest beacon is at x=10, y=16
        |Sensor at x=10, y=20: closest beacon is at x=10, y=16
        |Sensor at x=14, y=17: closest beacon is at x=10, y=16
        |Sensor at x=8, y=7: closest beacon is at x=2, y=10
        |Sensor at x=2, y=0: closest beacon is at x=2, y=10
        |Sensor at x=0, y=11: closest beacon is at x=2, y=10
        |Sensor at x=20, y=14: closest beacon is at x=25, y=17
        |Sensor at x=17, y=20: closest beacon is at x=21, y=22
        |Sensor at x=16, y=7: closest beacon is at x=15, y=3
        |Sensor at x=14, y=3: closest beacon is at x=15, y=3
        |Sensor at x=20, y=1: closest beacon is at x=15, y=3""".stripMargin
    )

//    println(testGrid.checkY(10))
//    println(testGrid.checkY(11))
    println(testGrid.checkArea(0 to 20, 0 to 20))
    val tr1 = testGrid.calcRanges(0 to 20, 0 to 20)
    // val tr1 = testGrid.calcRanges(0 to 20, 11 to 11)
    (0 to 20).foreach(y => println(tr1.get(y)))

//    println(tr1.get(11))

    val grid = parse(
      """Sensor at x=1259754, y=1927417: closest beacon is at x=1174860, y=2000000
        |Sensor at x=698360, y=2921616: closest beacon is at x=1174860, y=2000000
        |Sensor at x=2800141, y=2204995: closest beacon is at x=3151616, y=2593677
        |Sensor at x=3257632, y=2621890: closest beacon is at x=3336432, y=2638865
        |Sensor at x=3162013, y=3094407: closest beacon is at x=3151616, y=2593677
        |Sensor at x=748228, y=577603: closest beacon is at x=849414, y=-938539
        |Sensor at x=3624150, y=2952930: closest beacon is at x=3336432, y=2638865
        |Sensor at x=2961687, y=2430611: closest beacon is at x=3151616, y=2593677
        |Sensor at x=142293, y=3387807: closest beacon is at x=45169, y=4226343
        |Sensor at x=3309479, y=1598941: closest beacon is at x=3336432, y=2638865
        |Sensor at x=1978235, y=3427616: closest beacon is at x=2381454, y=3683743
        |Sensor at x=23389, y=1732536: closest beacon is at x=1174860, y=2000000
        |Sensor at x=1223696, y=3954547: closest beacon is at x=2381454, y=3683743
        |Sensor at x=3827517, y=3561118: closest beacon is at x=4094575, y=3915146
        |Sensor at x=3027894, y=3644321: closest beacon is at x=2381454, y=3683743
        |Sensor at x=3523333, y=3939956: closest beacon is at x=4094575, y=3915146
        |Sensor at x=2661743, y=3988507: closest beacon is at x=2381454, y=3683743
        |Sensor at x=2352285, y=2877820: closest beacon is at x=2381454, y=3683743
        |Sensor at x=3214853, y=2572272: closest beacon is at x=3151616, y=2593677
        |Sensor at x=3956852, y=2504216: closest beacon is at x=3336432, y=2638865
        |Sensor at x=219724, y=3957089: closest beacon is at x=45169, y=4226343
        |Sensor at x=1258233, y=2697879: closest beacon is at x=1174860, y=2000000
        |Sensor at x=3091374, y=215069: closest beacon is at x=4240570, y=610698
        |Sensor at x=3861053, y=889064: closest beacon is at x=4240570, y=610698
        |Sensor at x=2085035, y=1733247: closest beacon is at x=1174860, y=2000000""".stripMargin
    )


    val xLimit = 4000000
    val yLimit = 4000000
    val ranges = grid.calcRanges(0 to xLimit, 0 to yLimit)
    // val tr1 = testGrid.calcRanges(0 to 20, 11 to 11)
    val notCoveredRow = (0 to yLimit).filter(y => ranges(y).size > 1)
    val y = notCoveredRow.head
    println(s"y: $y, ${ranges(y)}")

    val x = ranges(notCoveredRow.head).head.max + 1
    println(x.toLong * xLimit + y.toLong)
  }
}
