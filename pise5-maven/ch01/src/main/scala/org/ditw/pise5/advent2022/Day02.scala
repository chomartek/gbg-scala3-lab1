package org.ditw.pise5.advent2022

import org.apache.commons.io.IOUtils

import java.io.{FileInputStream, InputStream}
import java.nio.charset.StandardCharsets

object Day02 {
  private val GestureRock = "Rock"
  private val GesturePaper = "Paper"
  private val GestureScissors = "Scissors"
  private val OpponentGestureMap = Map("A" -> GestureRock, "B" -> GesturePaper, "C" -> GestureScissors)

  private val C1MyGestureMap = Map("X" -> GestureRock, "Y" -> GesturePaper, "Z" -> GestureScissors)

  private val MatchResultLose = -1
  private val MatchResultDraw = 0
  private val MatchResultWin = 1
  private val C2MatchResultMap = Map("X" -> MatchResultLose, "Y" -> MatchResultDraw, "Z" -> MatchResultWin)

  private val ScoreMap = Map(GestureRock -> 1, GesturePaper -> 2, GestureScissors -> 3)

  private val WinningGesturePair = Set(
    GestureRock -> GesturePaper,
    GestureScissors -> GestureRock,
    GesturePaper -> GestureScissors
  )
  private val WinningGestureMap = WinningGesturePair.toMap
  private val LosingGestureMap = WinningGesturePair.map(p => p._2 -> p._1).toMap

  private val ScoreDraw = 3
  private val ScoreLose = 0
  private val ScoreWin = 6
  private def checkScore(opponentGesture: String, myGesture: String): Int = {
    if (opponentGesture == myGesture) ScoreDraw
    else {
      if (WinningGesturePair.contains(opponentGesture -> myGesture)) ScoreWin
      else ScoreLose
    }
  }
  private def processLine1(line: String): (Int, String) = {
    val parts = line.split("\\s+").filter(_.trim.nonEmpty)
    val opponentGesture = OpponentGestureMap(parts(0))
    val myGesture = C1MyGestureMap(parts(1))

    val scores = List(
      ScoreMap(myGesture),
      checkScore(opponentGesture, myGesture)
    )
    val scoreTotal = scores.sum
    val scoreTrace = scores.map(_.toString).mkString("+")
    scoreTotal -> scoreTrace
  }

  private def getMyGestureByResult(opponentGesture: String, matchResult: Int): String = matchResult match {
    case MatchResultDraw => opponentGesture
    case MatchResultLose => LosingGestureMap(opponentGesture)
    case MatchResultWin => WinningGestureMap(opponentGesture)
  }

  private def processLine2(line: String): (Int, String) = {
    val parts = line.split("\\s+").filter(_.trim.nonEmpty)
    val opponentGesture = OpponentGestureMap(parts(0))
    val matchResult = C2MatchResultMap(parts(1))
    val myGesture = getMyGestureByResult(opponentGesture, matchResult)

    val scores = List(
      ScoreMap(myGesture),
      checkScore(opponentGesture, myGesture)
    )
    val scoreTotal = scores.sum
    val scoreTrace = scores.map(_.toString).mkString("+")
    scoreTotal -> scoreTrace
  }

  private def run1(ins: InputStream): Seq[(Int, String)] = {
    import collection.JavaConverters._
    val inputLines = IOUtils.readLines(ins, StandardCharsets.UTF_8).asScala
    inputLines.map(processLine1).toSeq
  }
  private def run2(ins: InputStream): Seq[(Int, String)] = {
    import collection.JavaConverters._
    val inputLines = IOUtils.readLines(ins, StandardCharsets.UTF_8).asScala
    inputLines.map(processLine2).toSeq
  }
  def main(args: Array[String]): Unit = {
    println(processLine1("A Y"))
    println(processLine1("B X"))
    println(processLine1("C Z"))
    val ins1 = new FileInputStream("C:\\tmp\\advent-code\\input-day02.txt")
    val results1 = run1(ins1)
    ins1.close()
    println(results1.map(_._1).sum)

    println(processLine2("A Y"))
    println(processLine2("B X"))
    println(processLine2("C Z"))
    val ins2 = new FileInputStream("C:\\tmp\\advent-code\\input-day02.txt")
    val results2 = run2(ins2)
    ins2.close()
    println(results2.map(_._1).sum)

  }
}
