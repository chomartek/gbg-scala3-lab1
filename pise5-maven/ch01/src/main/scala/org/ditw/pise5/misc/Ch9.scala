package org.ditw.pise5.misc

object Ch9 {

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

  def main(args: Array[String]): Unit = {

  }
}
