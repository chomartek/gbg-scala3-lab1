package org.ditw.pise5.misc

import scala.collection.{AbstractIterator, IterableFactory, IterableOps, mutable}
import scala.collection.mutable.ArrayBuffer

object CollInDepth {

  val l1 = (2 to 6).toList
  val it1 = l1.grouped(3)
  println(it1.toList)

  val arr1 = new Array[Int](5)
  val copyRes1 = l1.copyToArray(arr1)
  println(arr1.toList)
  println(copyRes1)

  println(arr1.sameElements(l1))

  import collection.immutable

  class CapSeq1Factory(capacity: Int) extends IterableFactory[CapSeq1] {
    override def from[A](source: IterableOnce[A]): CapSeq1[A] = {
      (newBuilder[A] ++= source).result()
    }
    override def empty[A]: CapSeq1[A] = new CapSeq1[A](capacity)
    override def newBuilder[A]: mutable.Builder[A, CapSeq1[A]] = {
      new mutable.ImmutableBuilder[A, CapSeq1[A]](empty[A]) {
        override def addOne(elem: A): this.type = {
          elems = elems :+ elem
          this
        }
      }
    }
  }

  class CapSeq1[A] private(val capacity: Int, val length: Int, offset: Int, elems: ArrayBuffer[A])
    extends immutable.Iterable[A]
    with IterableOps[A, CapSeq1, CapSeq1[A]] { self =>
    def this(capacity: Int) = this(capacity, 0, 0, new ArrayBuffer(capacity))

    override def iterableFactory: IterableFactory[CapSeq1] = {
      new CapSeq1Factory(capacity)
    }

    override protected def fromSpecific(coll: IterableOnce[A]): CapSeq1[A] = {
      iterableFactory.from(coll)
    }

    override protected def newSpecificBuilder: mutable.Builder[A, CapSeq1[A]] = {
      iterableFactory.newBuilder
    }

    override def empty: CapSeq1[A] = {
      iterableFactory.empty
    }

    def appended[B >: A](elem: B): CapSeq1[B] = {
      val newElems = new ArrayBuffer[B](capacity)
      newElems ++= elems
      val (newOffset, newLength) =
        if (length == capacity) {
          newElems(offset) = elem
          ((offset + 1) % capacity, length)
        } else {
          newElems += elem
          (offset, length + 1)
        }
      new CapSeq1[B](capacity, newLength, newOffset, newElems)
    }

    @inline def :+ [B >: A](elem: B): CapSeq1[B] = appended(elem)

    def apply(i: Int): A = elems((i + offset) % capacity)

    override def iterator: Iterator[A] = new AbstractIterator[A] {
      private var curr = 0
      override def hasNext: Boolean = curr < self.length

      override def next(): A = {
        val res = elems(curr)
        curr += 1
        res
      }
    }

    override protected[this] def className: String = "CapSeq1"
  }

  val cap1 = new CapSeq1(capacity = 4)
  val cap2 = cap1 :+ 1 :+ 2 :+ 3
  println(cap2)
  println(cap2.length)
  val cap3 = cap2 :+ 4 :+ 5 :+ 6
  println(cap3)
  println(cap3.length)
  println(cap3.take(3))

  val seq1 = Seq("a", "ab", "abb")
  println(seq1.indexWhere(_ > "ab"))

  def main(args: Array[String]): Unit = {
    
  }
}
