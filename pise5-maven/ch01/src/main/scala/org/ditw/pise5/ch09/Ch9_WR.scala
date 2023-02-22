package org.ditw.pise5.ch09

import org.apache.commons.io.IOUtils

import java.io.{FileInputStream, InputStream}
import java.nio.charset.StandardCharsets
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Random

object Ch9_WR {
  // 1 control abstraction
  // 1.1 data abstraction vs control abstraction

  // 2 high order functions
  import collection.JavaConverters._
  private def readLogLines(logPath: String): List[String] = {
    val logStrm = new FileInputStream(logPath)
    val lines = IOUtils.readLines(logStrm, StandardCharsets.UTF_8)
    logStrm.close()
    lines.asScala.toList
  }
  val logLines = readLogLines("log4j-active.txt")

  // 3. control abstraction in Scala collection API
  // .exist .forall .find .sortBy ...
  def containsNeg(num: List[Int]): Boolean = {
    var exists = false
    num.foreach { n =>
      if (n < 0) exists = true
    }
    exists
  }

  def containsOdd(num: List[Int]): Boolean = {
    var exists = false
    num.foreach { n =>
      if (n % 2 == 1) exists = true
    }
    exists
  }


  // 4 currying
  def curriedSum(x: Int)(y: Int) = x + y
  val curriedSum1: Function[Int, Int] = curriedSum(1)
  val curriedSum2: Int = curriedSum1(2) // 3

  // 5 new control structure
  // the loan pattern
  //   with in python
  //     with open("/path/to/file", mode="w") as file:
  //       file.write(...)
  //   using in C#
  //     using(var reader = new SomeReader()) {
  //       ...
  //     }


  // 6. by-name param
  // 6.1. vs by-value param

  // 6.2. test framework

  // 3. Future
  import concurrent.ExecutionContext.Implicits.global

  def main(args: Array[String]): Unit = {

  }
}
