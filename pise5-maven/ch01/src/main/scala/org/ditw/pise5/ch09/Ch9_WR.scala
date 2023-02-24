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
  abstract class Person {
    def getId(): String
    def identify(): Boolean // the process of verifying the identity of a person
  }

  class PersonWithSSN(ssn: String) extends Person {
    override def getId(): String = ssn
    override def identify(): Boolean = {
      // identity with ssn
      ???
    }
  }
  class PersonWithPassport(passport: String) extends Person {
    override def getId(): String = passport
    override def identify(): Boolean = {
      // identify with passport
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

  def linesContaining(lines: List[String], containStr: String): List[String] = {
    lines.filter(line => line.contains(containStr))
  }
  def linesStartingWith(lines: List[String], startStr: String): List[String] = {
    lines.filter(line => line.startsWith(startStr))
  }
  def linesMatchingRegex(lines: List[String], regex: String): List[String] = {
    lines.filter(line => line.matches(regex))
  }

  def linesMatching(
                     lines: List[String],
                     matcher: (String, String) => Boolean,
                     matcherParam: String
                   ): List[String] = {
    lines.filter(line => matcher(line, matcherParam))
  }
  // control abstraction: the behavior of testing a log line against a condition

  val contain1 = linesStartingWith(logLines, "\tat ")
  contain1.take(5).foreach(println)
  val contain2 = linesMatching(logLines, (line, startStr) => line.startsWith(startStr), "\tat ")
  println()
  contain2.take(5).foreach(println)

  def cleanupLogs(logs: List[String], filters: List[String => Boolean]): List[String] = {
    var result = logs
    filters.foreach { f => result = result.filter( line => f(line) ) }
    result
  }

  val cleanedLogs = cleanupLogs(
    logLines,
    List(
      line => !line.startsWith("\tat "),
      line =>
        Set("FairSchedulableBuilder", "DeltaParquetFileFormat", "AbfsClient", "spark.sql.hive.convertCTAS")
          .exists(keyword => line.contains(keyword))
    )
  )

  cleanedLogs.foreach(println)

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

  def containsNeg1(num: List[Int]) = num.exists(_ < 0)
  def containsOdd1(num: List[Int]) = num.exists(_ % 2 == 1)

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
    4*16
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
