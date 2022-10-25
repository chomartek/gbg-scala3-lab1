package org.ditw.pise5.ch06

object UnderscoreTest {
  class T1(v: Int) {
    private var _myvar: Int = v
    def myvar: Int = _myvar
    def myvar_=(f: Float): Unit =
      _myvar = f.toInt
  }
  def main(args: Array[String]): Unit = {
    val t1 = T1(1)
    println(t1.myvar)

    t1.myvar = 21.5f
    println(t1.myvar)
  }
}
