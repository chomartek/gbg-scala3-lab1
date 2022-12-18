package org.ditw.pise5.advent2022

import org.apache.commons.io.IOUtils

import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import scala.collection.mutable.ListBuffer

object Day11 {

  class Monkey(
    val index: Int,
    val inputItems: List[BigInt],
    private val levelUpdater: BigInt => BigInt,
    val divider: Int,
    private val ifTrue: Int,
    private val ifFalse: Int
    // private val thrower: BigInt => Int
  ) {
    private val items = ListBuffer(inputItems: _*)
    private var inspected: Long = 0
    override def toString: String = {
      s"Monkey $index ($inspected): $items"
    }

    def getInspected: Long = inspected

    def run(commonDivider: Long): List[(Int, BigInt)] = {
      val oldItems = items.toList
      items.clear()
      inspected += oldItems.size
//      println(s"---- Monkey $index: ")
      oldItems.map { item =>
        val newLevel = levelUpdater(item) % commonDivider
//        println(s"\t$item -> $newLevel")
        val dstMonkeyIndex =
          if (newLevel % divider == 0) ifTrue
          else ifFalse
//        println(s"\t\t-> $dstMonkeyIndex")
        dstMonkeyIndex -> newLevel
      }

    }

    def addNewItem(item: BigInt): Unit = {
      items += item
    }
  }

  class MonkeyGroup(monkeys: Iterable[Monkey]) {
    private val map = monkeys.map(m => m.index -> m).toMap
    private val orderedIndices = map.keySet.toSeq.sorted
    def apply(index: Int): Monkey = map(index)

    private val commonDivider: Long = monkeys.map(_.divider).foldLeft(1L)((product, div) => product * div)

    def runOneLoop(): Unit = {
      orderedIndices.foreach { idx =>
        val monkey = map(idx)
        val newIndexToLevel = monkey.run(commonDivider)
        newIndexToLevel.foreach { p =>
//          val (monkeyIndex, itemLevel) = p.
//          println(p)
          val m = map(p._1)
          m.addNewItem(p._2)
        }
      }
    }

    override def toString: String = {
      map.keySet.toList.sorted.map(map).mkString("\n")
    }

    def businessLevel: Long = {
      val sorted = map.values.map(_.getInspected).toSeq.sorted(Ordering[Long].reverse)
      sorted(0) * sorted(1)
    }
  }

  private val LinesPerMonkey = 6
  private def parseOne(lines: Seq[String]): Monkey = {
    assert(lines.size == LinesPerMonkey)
    val ptn1 = "Monkey (\\d+):".r
    val ptn1(index) = lines(0)
    val ptn2 = "Starting items: (.*)".r // 72, 64, 51, 57, 93, 97, 68
    val ptn2(items) = lines(1)
    val ptn3 = "Operation: new = (.*)".r
    val ptn3(updater) = lines(2)
    val ptn4 = "Test: divisible by (\\d+)".r
    val ptn4(divisor) = lines(3)
    val ptn51 = "If true: throw to monkey (\\d+)".r
    val ptn51(ifTrue) = lines(4)
    val ptn52 = "If false: throw to monkey (\\d+)".r
    val ptn52(ifFalse) = lines(5)
    new Monkey(
      index.toInt, parseItems(items), (old: BigInt) => parseUpdater(updater)(old),
      divisor.toInt,
      ifTrue.toInt,
      ifFalse.toInt
    )
  }

  private def parseItems(itemsStr: String): List[BigInt] = {
    itemsStr.split(",").map(_.trim).filter(_.nonEmpty).map(v => BigInt(v)).toList
  }

  private def parseUpdater(updaterStr: String): BigInt => BigInt = {
    val addPrefix = "old + "
    val multPrefix = "old * "
    if (updaterStr.startsWith(addPrefix)) {
      val v = updaterStr.substring(addPrefix.length).toInt
      (old: BigInt) => old + v
    } else if (updaterStr.startsWith(multPrefix)) {
      val v = updaterStr.substring(multPrefix.length)

      if (v.trim == "old") {
        (old: BigInt) => old * old
      } else {
        (old: BigInt) => old * v.toInt
      }
    } else {
      throw new IllegalArgumentException(s"Unknown updater line: $updaterStr")
    }
  }

  private def parse(input: String): List[Monkey] = {
    val lines = input.split("[\\r\\n]+").map(_.trim).filter(_.nonEmpty)
    val it = lines.iterator

    import collection.mutable
    val result = ListBuffer[Monkey]()
    var currMonkeyLines = ListBuffer[String]()
    while (it.hasNext) {
      currMonkeyLines += it.next()
      if (currMonkeyLines.size == LinesPerMonkey) {
        result += parseOne(currMonkeyLines.toSeq)
        currMonkeyLines = ListBuffer[String]()
      }
    }
    result.toList
  }

  private def run(group: MonkeyGroup, rounds: Int): Unit = {
    val displayRounds = rounds / 10
    var currTs = System.currentTimeMillis()
    (1 to rounds).foreach { round =>
      group.runOneLoop()
      if (round % displayRounds == 0) {
        println(s"=== After round $round")
        println(group)
        println(group.businessLevel)
        val ts = System.currentTimeMillis()
        val elapsed = ts - currTs
        println(s"Elapsed: $elapsed ms")
        currTs = ts
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val testInput = """Monkey 0:
                      |  Starting items: 79, 98
                      |  Operation: new = old * 19
                      |  Test: divisible by 23
                      |    If true: throw to monkey 2
                      |    If false: throw to monkey 3
                      |
                      |Monkey 1:
                      |  Starting items: 54, 65, 75, 74
                      |  Operation: new = old + 6
                      |  Test: divisible by 19
                      |    If true: throw to monkey 2
                      |    If false: throw to monkey 0
                      |
                      |Monkey 2:
                      |  Starting items: 79, 60, 97
                      |  Operation: new = old * old
                      |  Test: divisible by 13
                      |    If true: throw to monkey 1
                      |    If false: throw to monkey 3
                      |
                      |Monkey 3:
                      |  Starting items: 74
                      |  Operation: new = old + 3
                      |  Test: divisible by 17
                      |    If true: throw to monkey 0
                      |    If false: throw to monkey 1""".stripMargin

    val testGroup = new MonkeyGroup(
      parse(testInput)
    )

    run(testGroup, 20)

//    val ins = new FileInputStream("advent-2022/input-11.txt")
//    val input = IOUtils.toString(ins, StandardCharsets.UTF_8)
//    ins.close()

    val input = """Monkey 0:
                  |  Starting items: 72, 64, 51, 57, 93, 97, 68
                  |  Operation: new = old * 19
                  |  Test: divisible by 17
                  |    If true: throw to monkey 4
                  |    If false: throw to monkey 7
                  |
                  |Monkey 1:
                  |  Starting items: 62
                  |  Operation: new = old * 11
                  |  Test: divisible by 3
                  |    If true: throw to monkey 3
                  |    If false: throw to monkey 2
                  |
                  |Monkey 2:
                  |  Starting items: 57, 94, 69, 79, 72
                  |  Operation: new = old + 6
                  |  Test: divisible by 19
                  |    If true: throw to monkey 0
                  |    If false: throw to monkey 4
                  |
                  |Monkey 3:
                  |  Starting items: 80, 64, 92, 93, 64, 56
                  |  Operation: new = old + 5
                  |  Test: divisible by 7
                  |    If true: throw to monkey 2
                  |    If false: throw to monkey 0
                  |
                  |Monkey 4:
                  |  Starting items: 70, 88, 95, 99, 78, 72, 65, 94
                  |  Operation: new = old + 7
                  |  Test: divisible by 2
                  |    If true: throw to monkey 7
                  |    If false: throw to monkey 5
                  |
                  |Monkey 5:
                  |  Starting items: 57, 95, 81, 61
                  |  Operation: new = old * old
                  |  Test: divisible by 5
                  |    If true: throw to monkey 1
                  |    If false: throw to monkey 6
                  |
                  |Monkey 6:
                  |  Starting items: 79, 99
                  |  Operation: new = old + 2
                  |  Test: divisible by 11
                  |    If true: throw to monkey 3
                  |    If false: throw to monkey 1
                  |
                  |Monkey 7:
                  |  Starting items: 68, 98, 62
                  |  Operation: new = old + 3
                  |  Test: divisible by 13
                  |    If true: throw to monkey 5
                  |    If false: throw to monkey 6""".stripMargin
    val group = new MonkeyGroup(
      parse(input)
    )
    println(group)
    run(group, 10000)


  }
}
