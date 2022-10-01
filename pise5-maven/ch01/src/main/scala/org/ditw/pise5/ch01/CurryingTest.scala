package org.ditw.pise5.ch01

object CurryingTest {

  private def ff(a:Int)(b:Boolean): Option[Int] =
    if (b) Some(a) else None

  def main(args: Array[String]): Unit = {
    println(ff(1)(false))
    println(ff(0){
      true
    })

    val l4 = List(1, 2, 3, 4)
    println(
      l4.foldLeft(0)((sum, item) => sum + item)
    )

    println(
      l4.foldLeft(0)(_ + _)
    )
  }
}
