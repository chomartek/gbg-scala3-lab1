package org.ditw.pise5.misc

object ReturnTest {
  private def s1(v: Int *): Int = {
    v.foldLeft(0)((v1, v2) => v1 + v2)
  }
  private def s2(v: Int *): Int = {
    v.foldLeft(0)((v1, v2) => return v1 + v2)
  }

  def lazily(s: => String): String =
    try s catch { case t: Throwable => t.toString }

  def main(args: Array[String]): Unit = {
    println(s1(1, 2, 3))
    println(s2(1, 2, 3))

    println(lazily {
      return "hi"
    })

    def foo: () => Int = () => return () => 1
    foo()
  }
}
