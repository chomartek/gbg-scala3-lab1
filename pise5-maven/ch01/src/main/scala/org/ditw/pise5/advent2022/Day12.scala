package org.ditw.pise5.advent2022

object Day12 {
  import collection.mutable
  case class Pos(x: Int, y: Int, height: Char, isStart: Boolean) {

    def coord: (Int, Int) = y -> x

    var toSet = mutable.Set[Pos]()

    def checkAndAddToToSet(pos: Pos): Unit = {
      if (pos.height - height <= 1)
        toSet += pos
    }
  }

  case class PosState(fromPos: Option[Pos], dist: Int) {
    def fromPosDist(stateMap: mutable.Map[(Int, Int), PosState]): Int = {
      if (fromPos.isEmpty) {
        // start pos
        0
      } else {
        stateMap(fromPos.get.coord).dist
      }
    }
  }

  class HeightMap(val posGrid: Seq[Seq[Pos]], val startPos: Pos, val endPos: Pos) {

    private def processGrid(): Unit = {
      posGrid.indices.foreach { y =>
        val row = posGrid(y)
        row.indices.foreach { x =>
          val pos = row(x)
          if (y > 0) pos.checkAndAddToToSet(posGrid(y-1)(x))
          if (x > 0) pos.checkAndAddToToSet(posGrid(y)(x-1))
          if (y < posGrid.size-1) pos.checkAndAddToToSet(posGrid(y+1)(x))
          if (x < row.size-1) pos.checkAndAddToToSet(posGrid(y)(x+1))
        }
      }
    }

    def findAllPathFromA(): Unit = {
      val allSteps = posGrid.flatMap { row =>
        row.flatMap { pos =>
          if (pos.height == 'a') {
            findPath(pos)
          } else None
        }
      }
      println(s"All steps: $allSteps")
      println(s"Min steps: ${allSteps.min}")
    }

    def findPath(sPos: Pos): Option[Int] = {
      val posSet = mutable.ListBuffer(sPos)
      val visitedSet = mutable.Set[Pos]()
      val posDistMap = mutable.Map[(Int, Int), PosState]()
      posDistMap += sPos.coord -> PosState(None, 0)

      while (posSet.nonEmpty) {
        val currPos = posSet.head
        posSet.remove(0)
//        println(s"Removing $currPos")

//        val currPosState = try {
//          posDistMap(currPos.coord)
//        } catch {
//          case ex: Exception =>
//            println(posDistMap)
//            throw ex
//        }
        val currPosState = posDistMap(currPos.coord)

//        if (currPos.coord == (7, 1)) {
//          println("dbg")
//        }

        currPos.toSet.foreach { toPos =>
          val coord = toPos.coord
          if (posDistMap.contains(coord)) {
            val toPosState = posDistMap(coord)
            val fromDist = toPosState.fromPosDist(posDistMap)
            if (fromDist > currPosState.dist + 1) {
              // update toPos
//              if (currPos.x == 6 && currPos.y == 1) {
//                println("dbg")
//              }
              posDistMap(coord) = PosState(Some(currPos), currPosState.dist + 1)
              posSet += toPos
            } else {
              // to nothing
            }
          } else {
//            if (currPos.x == 6 && currPos.y == 1) {
//              println("dbg")
//            }
            posDistMap(coord) = PosState(Some(currPos), currPosState.dist + 1)
            posSet += toPos
          }
        }


//        posSet ++= (currPos.toSet -- visitedSet)
//
//        visitedSet += currPos
      }

      // val result = posDistMap.toMap
      val result = tracePath(posDistMap.toMap)
      println(s"From $sPos: $result")
      result
    }

    private def tracePath(pathMap: Map[(Int, Int), PosState]): Option[Int] = {
      val coordSet = mutable.Set[(Int, Int)]()
      var currPos = Option(endPos)
      while (currPos.nonEmpty && pathMap.contains(currPos.get.coord)) {
        val coord = currPos.get.coord
        coordSet += coord
        val state = pathMap(coord)
        // println(state)
        currPos = state.fromPos
      }

      val builder = new StringBuilder()
      posGrid.indices.foreach { y =>
        val row = posGrid(y)
        row.indices.foreach { x =>
          builder.append(row(x).height)
          val coord = row(x).coord
          if (coordSet.contains(coord)) {
            builder.append(f"${pathMap(coord).dist}%03d-")
            // builder.append(f"${pathMap(coord).dist}%02d-${coord._2}%02d ")
          } else {
            builder.append("    ")
          }
        }
        builder.append('\n')
      }
      if (pathMap.contains(endPos.coord)) {
        builder.append(s"State for endPos: ${pathMap(endPos.coord)}")
      }

      // println(builder.toString())
      pathMap.get(endPos.coord).map(_.dist)
    }

    processGrid()

    private def fromDir(pos: Pos): String = {
      pos.toSet.map { fpos =>
        if (pos.x > 0 && fpos == posGrid(pos.y)(pos.x-1)) "L"
        else if (pos.y > 0 && fpos == posGrid(pos.y-1)(pos.x)) "U"
        else if (pos.y < posGrid.size-1 && fpos == posGrid(pos.y+1)(pos.x)) "D"
        else if (pos.x < posGrid(pos.y).size-1 && fpos == posGrid(pos.y)(pos.x+1)) "R"
        else throw new IllegalStateException(s"Error: unknown from pos: $fpos (pos = $pos)")
      }.mkString
    }

    override def toString: String = {
      val builder = new StringBuilder()
      posGrid.indices.foreach { y =>
        val row = posGrid(y)
        row.indices.foreach { x =>
          if (startPos.x == x && startPos.y == y) builder.append('S')
          else if (endPos.x == x && endPos.y == y) builder.append('E')
          else builder.append(row(x).height)
          val fromInfo = fromDir(posGrid(y)(x))
          builder.append(f"[$fromInfo%4s] ")
        }
        builder.append('\n')
      }
      builder.toString()
    }
  }

  def buildMap(input: String): HeightMap = {
    val lines = input.split("[\\r\\n]+").map(_.trim).filter(_.nonEmpty)
    var startPos: Pos = null
    var endPos: Pos = null
    val posGrid = lines.indices.map { y =>
      val line = lines(y)
      val chars = line.toCharArray
      chars.indices.map { x =>
        val ch = chars(x)
        val currPos = ch match {
          case 'S' =>
            startPos = Pos(x, y, 'a', true)
            startPos
          case 'E' =>
            endPos = Pos(x, y, 'z', false)
            endPos
          case _ =>
            Pos(x, y, ch, false)
        }
        currPos
      }
    }

    new HeightMap(posGrid, startPos, endPos)
  }

  def main(args: Array[String]): Unit = {
//    val testMap = buildMap(
//      """Sabqponm
//        |abcryxxl
//        |accszExk
//        |acctuvwj
//        |abdefghi""".stripMargin
//    )
//    println(testMap)
//    testMap.findPath()

//    val testMap2 = buildMap(
//      """abacccccccccccccccccccccccccaaEc
//        |abcccccccccccccccccccccccccccccc
//        |Sbaaccccccaccaaacccccccccccccccc
//        |abaaccccccaaaaaacccccccccccccccc""".stripMargin
//    )
//    println(testMap2)
//    testMap2.findPath()

    val inputMap = buildMap(
      """abcccccccccccaaaaacccccccaaaaaaccccccccccccccccccccccccccccccccaaaaaaaaaaaaaacccccccccccccccccccaaaaaacccccccacccccccccaaccaaccccccccccccccccccccccccccccccaaaaaa
        |abcccccccccccaaaaaaacccccaaaaaaccccccccaaccccccccccaaaaccccccccaaaaaaaaaaaaaccccccccccccccccccccaaaaaaccccaaaacccccccccaaaaaaccccccccccccccccccccccccccccccccaaaa
        |abccccccccccaaaaaaaacccccaaaaaccccccccaaaacccccccccaaaacccccccccccaaaaaaacccccccccccccccccccccccaaaaacccccaaaaaaccccccccaaaaacccccccccccaaaccccccccccccccccccaaaa
        |abccccccccccaaaaaaaaccccccaaaaacccccccaaaacccccccccaaaacccccaaaccccaaaaaaccccccccccccccccccccccccaaaaacccccaaaaacccccccaaaaaacccccccccccaaaacccccccccccccccccaaaa
        |abccccccccccaaaaaaccccccccaaaaacccccccaaaaccccccccccaaacccccaaaaccaaaaaaaacccccccccccccccccccccccaaaaaccccaaaaacccccccaaaaaaaaccccccccccaaaaccaaccccccccccccaaaaa
        |abcccccccccccccaaaccccccccccccccccccccccccccccccccccccccccccaaaaccaaaaaaaaccccccccccccccccccccccccccccccccaccaaccccaaaaaaaaaaacccccccccccaaaaaaccccccccccccccaccc
        |abcacccccccccccccaaaccccccccccccccaaacccccccccccccccccccccccaaaccaaaacccaaacccccccccccccccccccccccaacccccccccccccccaaacccaaccccccccccccccaaaalllllccccccccccccccc
        |abaacccccccccccccaaaaaacccccccccccaaaccccccccccccccccccccccccccccaaaccccaaaccccccccccccccccccccccaaccccccccccccccccaaaaaaaaccccccccccccccckklllllllccccaaaccccccc
        |abaaaaacccccccccaaaaaaacccccccccaaaaaaaacccccaacccccccccccccccccccccccccaaaccccccccaacccccccccaaaaacaacaacaacccccaaaaaaaaccccccccccccaaakkkkllllllllcccaaaccccccc
        |abaaaaaccccccccaaaaaaaacccccccccaaaaaaaacccccaaaaaacccccccccccccccccccccaaaccccccaaaacacccccccaaaaaaaacaaaaaccccaaaaaaaaaccccccccckkkkkkkkkklsssslllcccaaaaaacccc
        |abaaaccccccccccaaaaaaacccccccccccaaaaacccccccaaaaaaccccccccccccccccccaaaaaaaaccccaaaaaacccccccccaaaaacaaaaacccccaaaaaaaacccaaaccjjkkkkkkkkkssssssslllcccaaaaacccc
        |abaaaccccccccccccaaaaaacccccaacccaaaaaaccccaaaaaaaccaaaccccccccccccccaaaaaaaacccccaaaacccccccccaaaaaccaaaaaacccccccaaaaaaccaaaajjjjkkkkkkssssssssslllcddaaaaccccc
        |abcaaacccccccccccaaaaaacaaacaacccaaaaaaccccaaaaaaaaaaaaaaccccccccccccccaaaaaccccccaaaaccccaaaaaaacaaacccaaaaaaacccaaaaaaaccaaajjjjrrrrrrssssuuuussqmmddddaaaccccc
        |abccaacccccccccccaaccccccaaaaacccaaaccaccccaaaaaaaaaaaaaacccccccccccccaaaaaacccccaacaaccccaaaaacccaaccccacccaaaaaaaaaccaaccaaajjjrrrrrrrrssuuuuuvqqmmmdddaaaccccc
        |abccccccccaaccccccccccccccaaaaaacccccccccccccaaaaaaaaaaaccccccccccccccaaaaaacccccccccccccaaaaaaccccccccccccaaaaaaaaaacccccccccjjjrrruuuuuuuuuuuvvqqmmmmddddaccccc
        |abaacccccaaaaccccccccccccaaaaaaacccccccccccccaacccccaaaaacccccccccccccaccaaacccccccccccccaaaaaacccccccccccaaaaaaaaccccccccccccjjjrrruuuuuuuuxyyvvqqqmmmddddcccccc
        |abaacccccaaaacccccccccccaaaaaaccccccccaaccccccccacccaaaaacccccccccccccccccccccccccccccccccaaaaacccccccccccaaaaaaacccccccccccccjjjrrttuxxxxuxxyyvvqqqqmmmmddddcccc
        |abaacccccaaaacccccccccccaacaaacccccaaaaacccaaaaaaaccccccccccccccccccccccccccccccccccccccccaaacccccccccccccccaaaaaaccccccccccccjjjrrtttxxxxxxyyyvvqqqqqmmmmdddcccc
        |abacccccccccccccccccccccccccaaccccccaaaaaccaaaaaaaccccccccaaccccccccccccccccccccccccccccccccccccccccccccccccaaaaaaccccccccccccjjjqrrttxxxxxxyyyvvvvqqqqmmmdddcccc
        |abccccccccccccccccccccccccccccccccccaaaaaccaaaaaaacccccccaaaccccccccccaaaccccccccccccccccccaaaccccccccccccccaaccccccccccaaccccjjjqqqtttxxxxxyyyyyvvvqqqqmmmeeeccc
        |SbaaccccccaccaaacccccccccccccccccccaaaaacccaaaaaaaaccccaaaaaaaacccccccaaacaaccccccccccccccaaaaaaccccccccccccccccccccccaaaaaaccciiiqqqttxxxxEzyyyyyvvvqqqnnneeeccc
        |abaaccccccaaaaaaccccccccccccccccccccccaaccaaaaaaaaaacccaaaaaaaacccccaaaaaaaaccccccccccccccaaaaaaccccccccccccccccccccccaaaaaaccciiiqqtttxxxyyyyyyyvvvvqqqnnneeeccc
        |abaaaaacccaaaaaaccccccccccccccccccccccccccaaaaaaaaaaccccaaaaaaccccccaaaaaaaaccccccccccccccaaaaaacccccccccccccccccccccccaaaaacciiiqqqttxxyyyyyyywvvvvrrrqnnneeeccc
        |abaaaaacccaaaaaaaccccccccaaaccccccccccccccaacaaaccccccccaaaaaaccccccaaaaaaaccccccccccccccccaaaaaccccccccccccccccccccccaaaaaccciiiqqtttxxxyyyyywwwvrrrrrnnneeecccc
        |abaaaccccaaaaaaaaccccccccaaaacccccccccccccccccaaccccccccaaaaaaccccccccaaaaaccccccccccccccccaacaaccccccccccccccccccccccaaaaaccciiqqqttxxxwwwwyyywwrrrrnnnnneeecccc
        |abaaaccccaaaaaaaaccccccccaaaacccccaaaaacccccccaaccccccccaaccaacccccccaaacaaacccccccccccccccccccccccccccccccccccccccccccccccccciiqqqtttwwwwwwywwwrrrrnnnnneeeccccc
        |abcaaaccccccaaaccccccccccaaaccccccaaaaacccccccccccccaaaaccccccccccccccaacccccccccccccccccccccccccccccccaaaacccccccccccccccccciiiqqqtttssssswwwwwrrrnnnneeeecccccc
        |abccaaccccccaaaccccccccccccccccccaaaaaacccccccccccccaaaaccccccccccccccccccccccccccccccccccccccccccccccaaaaacccccccccccccccccciiiqqqqtssssssswwwwrronnnfeeeacccccc
        |abcccccccccccccccccccccccccccccccaaaaaacccccccccccccaaaaccccccccccccccccccccccccccccccccccccccccccccccaaaaaaccccccccccccccccciiiiqqppssssssswwwwrroonfffeaacccccc
        |abcccccccccccccccccccccccccccccccaaaaaacaaaccccccccccaacccccccccccccccccccccccccccccccccaaacccccccccccaaaaaaccccccccccccccccccihhpppppppppsssssrrroonfffaaaaacccc
        |abcccccccccccccccccccccccccccccccccaacccaaaaacccccccccccccccccccccccccccccccccccccccccccaaaaaaccccccccaaaaaccccccccccccccccccchhhhppppppppppssssrooofffaaaaaacccc
        |abccccccccaaaccccccccccccccccccccccccccaaaaacccccccccccccccccccccccccccccccccccccaccccaaaaaaaaccccccccccaaacccccccccccccccccccchhhhhhhhhpppposssoooofffaaaaaccccc
        |abccccccccaaaacccccaaccccccccccccccccccaaaaaccccccccccccccccccccccccaaccccccccccaaccccaaaaaaaacccccccccccccccccccccccccccccccccchhhhhhhhhgppooooooofffaaaaacccccc
        |abaaccccccaaaacccccaacaaccccccccccccccccaaaaacccccccccccccccaacaaccaaaaaaccccaaaaacaacaaaaaaacccccccccccccccccccccccccccccccccccccchhhhhhgggooooooffffaaaaaaccccc
        |abaaacccccaaaacccccaaaaaccccccccccccccccaaccccccccccccccccccaaaaaccaaaaaaccccaaaaaaaacccaaaaaccccccccccccccccccccccccaaacaacccccccccccccgggggooooffffccccaacccccc
        |abaaaccccccccccccaaaaaaccccaaccccccccccccccccccaaacccccccccccaaaaaaaaaaaacaacccaaaaaccccaacaaacccccccccccccccccccccccaaaaaaccccccccccccccaggggggggfffcccccccccccc
        |abaaaccccccccccccaaaaaaaacaaaaccccccccaaaccccccaaaacccccccccaaaaaaaaaaaaaaaaccaaaaacccccaaaaaccccccccccccccaaccccccccaaaaaaccccccccccccccaagggggggfccccccccccccca
        |abaacccccccccccccaccaaaaacaaaaccccccccaaaccccccaaaacccccccccaaaaccaaaaaaaaaaccaacaaaccccccaaaccccccccccccaaaaaacccccaaaaaaacccccccccccccaaaaccggggcccccccccccccaa
        |abaacccccccccccccccaaacaccaaaacccccaaaaaaaaccccaaaccccccccccccaaccaaaaaaaacccccccaacccccaacaaaaacccccccccaaaaaacccccaaaaaaaaccccccccccccaaaccccccccccccccccaaacaa
        |abcccccccccccccccccaaccccccccccccccaaaaaaaaccccccccccccccccccccaaaaaaaaaaaccccccccccccccaaaaaaaacccccccccaaaaaacccccaaaaaaaaccccccccccccacaccccccccccccccccaaaaaa
        |abccccccccccccccccccccccccccccccccccaaaaaacccccccccccccccccccccaaaaaaaaaaaaccccccccccccccaaaaaaccccccccccaaaaaccccccccaaacccccccccccccccccccccccccccccccccccaaaaa""".stripMargin
    )
    // println(inputMap)
    inputMap.findPath(inputMap.startPos)
    inputMap.findAllPathFromA()
  }
}
