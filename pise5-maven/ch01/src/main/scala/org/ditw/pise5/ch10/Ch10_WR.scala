package org.ditw.pise5.ch10

object Ch10_WR {
  abstract class Element {
    def contents: Vector[String]
    val height = contents.length
    val width = if (height == 0) 0 else contents(0).length

    override def toString: String = s"Element, size: $width X $height"
  }

//  class VectorElement(conts: Vector[String]) extends Element {
//    override def contents = conts
//  }

  class VectorElement(val contents: Vector[String]) extends Element {
    override def toString: String = s"VectorElement, size: $width X $height"
  }

  val ve1 = new VectorElement(Vector("High", "Low"))

  println(ve1.width)

  class LineElement(s: String) extends VectorElement(Vector(s)) {
    override val height: Int = 1
    override def toString: String = s"LineElement, size: $width X $height"
  }

  // accidental overrides: manditory keyword 'override'
//  class LineElement(s: String) extends VectorElement(Vector(s)) {
//    val height: Int = 1
//  }

  // Polymorphism
//  val elem: Element = new VectorElement(Vector("..."))
  val elem: Element = new LineElement("Line elem")

  class UniformElement(ch: Char, override val width: Int, override val height: Int) extends Element {
    override def contents: Vector[String] = {
      val line = ch.toString * width
      Vector.fill(height)(line)
    }
  }

  // dynamic binding
  val elem1: Element = new UniformElement('A', 3, 4)
  val elem2: Element = new LineElement("New Line")
  println(elem1)
  println(elem2)

  // final member / class

  // composition and inheritance
  // is-a / has-a

  // above (same width):
  //   elem1
  //   elem2

  // beside (same height):
  //   elem1 elem2

  // factory object

  object ElementFactory {
    abstract class Elem protected () {
      def contents: Vector[String]

      val height = contents.length
      val width = if (height == 0) 0 else contents(0).length

      override def toString: String = s"Element, size: $width X $height"
    }

    private class VectorElement protected[ElementFactory](val contents: Vector[String]) extends Elem {
      override def toString: String = s"VectorElement, size: $width X $height"
    }

    private class VectorElement2 protected[ElementFactory](line: String, extraLines: List[String]) extends Elem {
      override def contents: Vector[String] = (line :: extraLines).toVector
      override def toString: String = s"VectorElement, size: $width X $height"
    }

    private class LineElement protected[ElementFactory] (s: String) extends VectorElement(Vector(s)) {
      override val height: Int = 1
      override def toString: String = s"LineElement, size: $width X $height"
    }

    private class UniformElement protected[ElementFactory] (ch: Char, override val width: Int, override val height: Int) extends Elem {
      override def contents: Vector[String] = {
        val line = ch.toString * width
        Vector.fill(height)(line)
      }
    }

    // def vector(lines: Vector[String]): Elem = new VectorElement(lines)
    def vector(lines: Vector[String]): Elem = new VectorElement2(lines.head, lines.tail.toList)
    def line(line: String): Elem = new LineElement(line)
    def uniform(ch: Char, width: Int, height: Int): Elem = new UniformElement(ch, width, height)
  }

  import ElementFactory._
  val e1 = vector(Vector("aa", "bb"))
  // val e11 = new ElementFactory.VectorElement(Vector("aaa", "bbb"))
  val e2 = line("aa")
  val e3 = uniform('x', 4, 3)
  println(e1)
  println(e2)
  println(e3)

  // Heighten and widen ...
  // Spiral ...

  def main(args: Array[String]): Unit = {

  }
}
