package org.ditw.pise5.misc

import scala.collection.mutable.ListBuffer
import scala.collection.{AbstractIterator, IterableFactory, IterableOps, mutable}

trait StackOps[A, CC[_], C] extends IterableOps[A, CC, C] {
  def pop: Option[A]
  def push(elem: A): C
}

class Stack[A](elems: A*) extends Iterable[A] with StackOps[A, Stack, Stack[A]] {
  private val buf = ListBuffer[A]()
  buf ++= elems
  override def iterator: Iterator[A] = {

    new AbstractIterator[A] {
      private var curr = 0
      override def hasNext: Boolean = buf.nonEmpty

      override def next(): A = pop.get
    }
  }

  override protected[this] def className: String = "Stack"

  override def iterableFactory = Stack

  override protected def fromSpecific(coll: IterableOnce[A]): Stack[A] =
    iterableFactory.from(coll)

  override protected def newSpecificBuilder: mutable.Builder[A, Stack[A]] =
    iterableFactory.newBuilder

  override def empty: Stack[A] = iterableFactory.empty

  override def push(elem: A): Stack[A] = {
    buf.insert(0, elem)
    this
  }

  override def pop: Option[A] =
    if (buf.isEmpty) None else {
      val h = buf.remove(0)
      Some(h)
    }

}

object Stack extends IterableFactory[Stack] {
  override def empty[A]: Stack[A] = new Stack()
  override def newBuilder[A]: mutable.Builder[A, Stack[A]] = {
    new mutable.ImmutableBuilder[A, Stack[A]](empty) {
      override def addOne(elem: A): this.type = {
        elems push elem
        this
      }

//      override def addAll(elem: IterableOnce[A]): this.type = {
//        elems push elem
//      }
    }
  }
  override def from[A](source: IterableOnce[A]): Stack[A] = {
    val b = newBuilder[A]
    b ++= source
    b.result()
  }
}

object StackTest {
  val st1 = Stack(1, 2)
//  println(st1)
//
//  val e0 = st1.pop
//  println(e0)
//  println(st1)
//  st1.push(3)
//  println(st1)
  val st2 = st1.take(2)
  println(st2)
  println(st1)

  val st3 = List(1, 2, 3).to(Stack)
  println(st3)

  def main(args: Array[String]): Unit = {

  }
}
