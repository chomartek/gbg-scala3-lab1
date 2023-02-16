package org.ditw.pise5.ch09

import org.apache.commons.io.IOUtils

import java.io.{FileInputStream, InputStream}
import java.nio.charset.StandardCharsets
import scala.concurrent.{Await, Future}
import scala.util.Random

object Ch9_2 {

  def timer[R](op: => R): (R, Long) = {
    val startTime = System.currentTimeMillis()
    val result = op
    val endTime = System.currentTimeMillis()
    result -> (endTime - startTime)
  }

  val r = timer {
    Thread.sleep(800)
    "Done"
  }

  println(s"Timer result: $r")

  def when(cond: Boolean)(action: => Unit) = {
    if (cond) action
  }

  var t = 1
  when(t == 1) {
    println("t == 1")
  }
  t = 3
  when(t == 2) {
    println("t == 2")
  }

  def load[T](path: String)(loader: InputStream => T): T = {
    val instream = new FileInputStream(path)
    try {
      val result = loader(instream)
      result
    } finally {
      instream.close()
    }
  }

  import collection.JavaConverters.*

  val spinList = load("C:\\dcb\\dbg\\curr_spins.txt") { instream =>
    IOUtils.readLines(instream, StandardCharsets.UTF_8)
      .asScala
      .toList
  }
  println(spinList.take(10).mkString("\n"))

  // by-name:

  import concurrent.ExecutionContext.Implicits.global

  val fut1 = Future {
    println("Generating a random number ...")
    Thread.sleep(1000)
    new Random(System.currentTimeMillis())
      .nextInt(100)
  }

  // by-name ex 2: test framework


  import scala.concurrent.duration.*

  val futResult = Await.result(fut1, 5.seconds)
  println(futResult)

  def main(args: Array[String]): Unit = {

  }
}
