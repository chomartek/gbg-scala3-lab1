package org.ditw.pise5.misc

import org.apache.commons.io.IOUtils

import java.io.{FileInputStream, InputStream}
import java.nio.charset.StandardCharsets
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Random

object Ch9 {
  // 1 control abstraction
  // 1.1 data abstraction vs control abstraction
  abstract class Person {
    def getId: String
    def indentify(): Boolean
  }

  class PersonWithSSN(ssn: String) extends Person {
    override def getId: String = ssn

    override def indentify(): Boolean = {
      // identify with ssn
      ???
    }
  }

  class PersonWithPassport(passportNumber: String) extends Person {
    override def getId: String = passportNumber

    override def indentify(): Boolean = {
      // identify with passport number
      ???
    }
  }

  // 2 high order functions
  import collection.JavaConverters._
  private def readLogLines(logPath: String): List[String] = {
    val logStrm = new FileInputStream(logPath)
    val lines = IOUtils.readLines(logStrm, StandardCharsets.UTF_8)
    logStrm.close()
    lines.asScala.toList
  }
  val logLines = readLogLines("log4j-active.txt")

  def lineContaining(lines: List[String], containStr: String): List[String] = {
    lines.filter(line => line.contains(containStr))
  }

  //  val tmp1 = lineContaining(logLines, "FairSchedulableBuilder")
  //  tmp1.foreach(println)

  def lineStartingWith(lines: List[String], startStr: String): List[String] = {
    lines.filter(_.startsWith(startStr))
  }
  val tmp2 = lineStartingWith(logLines, "\tat ")
  tmp2.take(5).foreach(println)

  def lineMatchesRegex(lines: List[String], regex: String): List[String] = {
    lines.filter(_.matches(regex))
  }

  def lineMatches(
                   lines: List[String],
                   matcher: (String, String) => Boolean,
                   matcherParam: String
                 ): List[String] = {
    lines.filter(line => matcher(line, matcherParam))
  }
  val tmp11 = lineMatches(logLines,
    (line, containStr) => line.startsWith(containStr),
    "\tat ")
  println()
  tmp11.take(5).foreach(println)

  def filterLogLines(
                      lines: List[String],
                      filters: List[String => Boolean]
                    ): List[String] = {
    var result = lines
    filters.foreach { f =>
      result = result.filter(f)
    }
    result
  }

  val cleanedLogLines = filterLogLines(
    logLines,
    List(
      !_.startsWith("\tat "),
      line =>
        !Set("FairSchedulableBuilder", "DeltaParquetFileFormat", "spark.sql.hive.convertCTAS").exists(line.contains)
    )
  )

  println("========= Cleaned log lines: ")
  cleanedLogLines.take(10).foreach(println)

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

  val strList = List("aab", "baba", "ca")
  strList.forall(_.contains("a")) // true
  strList.forall(_.contains("b")) // false
  strList.find(_.matches("^b")) // "ba"
  strList.sortBy(_.length) // "ca", "aab", "baba"

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
  def readWithLoanPattern[T](path: String)(reader: InputStream => T): T = {
    var strm: InputStream = null
    try {
      strm = new FileInputStream(path)
      reader(strm)
    } finally {
      if (strm != null) {
        strm.close()
      }
    }
  }

  def readLogLinesWithLoanPattern(path: String): List[String] = {
    readWithLoanPattern(path) { strm =>
      IOUtils.readLines(strm, StandardCharsets.UTF_8)
        .asScala
        .toList
    }
  }


  // 6. by-name param
  // 6.1. vs by-value param
  def delayed[T](delayMs: Int)(action: () => T): T = {
    Thread.sleep(delayMs)
    action()
  }

  delayed(1000) { () =>
    Random.nextInt()
  }

  def delayedByName[T](delayMs: Int)(action: => T): T = {
    Thread.sleep(delayMs)
    action
  }
  delayedByName(1000) {
    Random.nextInt()
  }

  def runTwice(action: => Int) = {
    action*action
  }

  var x = 2
  val result = runTwice {
    x = x*x
    x
  }
  println(result)

  // 6.2. test framework

  // 3. Future
  import concurrent.ExecutionContext.Implicits.global

  val future1 = Future {
    Thread.sleep(500)
    "Future has come"
  }

  val futureResult = Await.result(future1, Duration.Inf)
  println("futureResult: " + futureResult)

  def main(args: Array[String]): Unit = {

  }
}
