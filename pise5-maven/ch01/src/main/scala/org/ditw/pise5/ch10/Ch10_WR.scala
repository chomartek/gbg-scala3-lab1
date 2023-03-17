package org.ditw.pise5.ch10

object Ch10_WR {
  // 2-d layout lib
  //   above / beside
  //   _____________
  //   |hello| *** |
  //   | *** |world|
  //   -------------

  //  Element
  //      |__ VectorElement : |hello|
  //                          |world|
  //            |__ LineElement : |hello|
  //      |__ UniformElement : |aaaa|
  //                           |aaaa|
  //                           |aaaa|

  abstract class Element {
    // abstract method
    def contents: Vector[String]

    def height: Int = contents.length
    def width: Int = if (height == 0) 0 else contents(0).length
    // val height: Int = contents.length
    // val width: Int = if (height == 0) 0 else contents(0).length

    // def surrogateIdsPath(): String =
    //    Path.tablePath(Path.ZONE_CLOSED, Path.GROUP_VEHICLE, Path.TABLE_SURROGATE_IDS)

    override def toString: String = s"Element, size: $width X $height"
  }

  class VectorElement(conts: Vector[String]) extends Element {
    // implementation for abstract methods
    override def contents = conts

    override def width: Int = contents.map(_.length).max
  }

  // how to invoke super constructor
  class LineElement(s: String) extends VectorElement(Vector(s)) {
    override def toString: String = s"LineElement, size: $width X $height"
  }

  class UniformElement(ch: Char, override val width: Int, override val height: Int) extends Element {
    override def contents: Vector[String] = {
      val line = ch.toString * width
      Vector.fill(height)(line)
    }
  }

  //  Interaction between elements
  //   * Above:
  //             |elem11|  above  |elem21|
  //             |elem12|         |elem22|
  //     ===>
  //             |elem11|
  //             |elem12|
  //             |elem21|
  //             |elem22|
  //   * Beside:
  //             |elem11|  beside  |elem21|
  //             |elem12|          |elem22|
  //     ===>
  //             |elem11|elem21|
  //             |elem12|elem22|

  // accidental overrides: mandatory keyword 'override'
//  class LineElement1(s: String) extends VectorElement(Vector(s)) {
//    val height: Int = 1
//  }

  // val can be used to override a parameterless method
    class VectorElement1(val contents: Vector[String]) extends Element {
    }

  // subtyping: encapsulating implementation details
  val ve1: Element = new VectorElement(Vector("High", "Low"))

  // Polymorphism
  var elem: Element = new VectorElement(Vector("..."))
  println(s"elem: $elem")
  elem = new LineElement("Line elem")
  println(s"elem: $elem")

  // dynamic binding
//  val elem1: Element = new UniformElement('A', 3, 4)
//  val elem2: Element = new LineElement("New Line")
//  println(elem1)
//  println(elem2)

  // final member / class

  class A
  class B {
    val a: A = new A
  }
  // composition vs inheritance
  // is-a / has-a

  // factory object

  object ElementFactory {
    abstract class Elem () {
      def contents: Vector[String]

      val height = contents.length
      val width = if (height == 0) 0 else contents(0).length

      override def toString: String = s"Element, size: $width X $height"
    }

    // no public constructors
    private class VectorElem protected[ElementFactory](val contents: Vector[String]) extends Elem {
      override def toString: String = s"VectorElement, size: $width X $height"
    }

    private class VectorElem2 protected[ElementFactory](line: String, extraLines: List[String]) extends Elem {
      override def contents: Vector[String] = (line :: extraLines).toVector
      override def toString: String = s"VectorElement2, size: $width X $height"
    }

    private class LineElem protected[ElementFactory] (s: String) extends VectorElem(Vector(s)) {
      override def toString: String = s"LineElement, size: $width X $height"
    }

    private class UniformElem protected[ElementFactory] (ch: Char, override val width: Int, override val height: Int) extends Elem {
      override def contents: Vector[String] = {
        val line = ch.toString * width
        Vector.fill(height)(line)
      }
    }

    // def vector(lines: Vector[String]): Elem = new VectorElement(lines)
    def vector(lines: Vector[String]): Elem = new VectorElem2(lines.head, lines.tail.toList)
    def line(line: String): Elem = new LineElem(line)
    def uniform(ch: Char, width: Int, height: Int): Elem = new UniformElem(ch, width, height)
  }

  import ElementFactory._

//  val vectorElem = new ElementFactory.VectorElem()


  val e1 = vector(Vector("aa", "bb"))
  // val e11 = new ElementFactory.VectorElement(Vector("aaa", "bbb"))
  val e2 = line("aa")
  val e3 = uniform('x', 4, 3)
//  println(e1)
//  println(e2)
//  println(e3)

  // 1. abstract class: why?
  //    a. implementation
  //    b.
  //    ...

  // 2. traits in Scala 3
  trait Greeting(val greetingWord: String, val name: String) {
    override def toString: String = s"$greetingWord $name!"
  }

  class GreetingSv(name: String) extends Greeting("Hej", name)

  class GreetingCn(name: String) extends Greeting("你好", name)

  class C

  // class CannotExtendMultipleClasses(name: String) extends C with GreetingSv(name)
  class CanExtend1ClassWithMultipleTraits(greetingWord: String, name: String) extends C
    with Greeting(greetingWord, name)
    with Ordered[CanExtend1ClassWithMultipleTraits] {
    override def compare(that: CanExtend1ClassWithMultipleTraits): Int = {
      this.name.compareTo(that.name)
    }
  }

  println(new GreetingSv("Johan"))
  println(new GreetingCn("Johan"))

  val greetingList = List(
    CanExtend1ClassWithMultipleTraits("Hej", "John"),
    CanExtend1ClassWithMultipleTraits("Hej", "Johan"),
    CanExtend1ClassWithMultipleTraits("你好", "Alice")
  )

  greetingList.sorted.foreach(println)

  def main(args: Array[String]): Unit = {

  }
}
