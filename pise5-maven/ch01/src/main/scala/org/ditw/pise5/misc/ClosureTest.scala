package org.ditw.pise5.misc

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object ClosureTest {
  private def invoke(f: Any => Unit, param: Any) = f(param)

  def main(args: Array[String]): Unit = {
    inLoop()

    useCase1Async("data")

    //    var verb = "Hi"
    //    val greet = (name: Any) => println(s"$verb $name")
    //
    //    invoke(greet, "John")
    //
    //    verb = "Hej"
    //    invoke(greet, "Erik")
  }

  def inLoop(): Unit = {
    var verb = "Hi"
    // val greet = (name: Any) => println(s"$verb $name")

    val verbs = List("Hej", "Hello")
    val greets = verbs.map { v =>
      verb = v
      (name: Any) => println(s"$verb $name")
    }

    greets.foreach(g => invoke(g, "John"))
  }

  def useCase1Async(data: String): Unit = {
    import scala.concurrent.ExecutionContext.Implicits._
    val ts = System.currentTimeMillis()
    val f = Future {
      Thread.sleep(1000)
      println(s"Request done, saving [$data] ...")
    }.andThen(_ => println(s"Elapsed: ${System.currentTimeMillis() - ts}"))

    Await.result(f, Duration("2 seconds"))

  }
}
