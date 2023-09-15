package org.ditw.pise5.ch14

import java.io.InputStream
import scala.collection.immutable.LinearSeq
import scala.collection.mutable.ListBuffer
import scala.math.ScalaNumber
import scala.util.Random

object Ch14WR {

  private def profiling(note: String)(op: => Unit) = {
    val startTs = System.currentTimeMillis()
    op
    val elapsed = (System.currentTimeMillis() - startTs) / 1000.0
    println(f"[Profiling] $note elapsed $elapsed%.3f seconds")
  }

  // 1. immutable & recursive
  //

  val arr1 = Array(1, 2, 3)
  arr1(2) = 4
  println(arr1.mkString(","))
  val lst1 = List(1, 2, 3)
  // lst(2) = 4

  // head - tail
  //         |
  //        head - tail
  //                |
  //               head - tail
  val lst2 = (1 to 5).toList
  var tail = lst2.tail
  while (tail.nonEmpty) {
    println(tail)
    tail = tail.tail
  }

  // 2. covariant
  // The list type in Scala is covariant. This means that for each pair of
  //  types S and T, if S is a subtype of T, then List[S] is a subtype of List[T].
  //  For instance, List[String] is a subtype of List[AnyRef] because String is a subtype of AnyRef
  val strList: List[String] = List("string", "list")
  val anyList: List[AnyRef] = strList
  println(anyList)

  // Nil: List[Nothing]
  // Nothing: bottom type
  //  https://docs.scala-lang.org/resources/images/tour/unified-types-diagram.svg
  val intList: List[Int] = List[Nothing]()
  val bigIntList: List[BigInt] = List[Nothing]()
  val streamList: List[InputStream] = List[Nothing]()

  println(s"intList == bigIntList: ${intList == bigIntList}")
  println(s"intList eq bigIntList: ${intList.eq(bigIntList)}")
  println(s"intList eq Nil: ${intList.eq(Nil)}")

  // None: Option[Nothing]

  // 3. Constructing lists

  // Cons operator
  // def ::[B >: A](elem: B): List[B] =
  val tlst1: List[String] = "abc" :: Nil
  println(tlst1)
  //                        String("abc")
  //             Nothing -> String

  //                        Int -> Any
  //             Nothing -> Int
  val tlst2: List[Any] = 1 :: Nil

  val list12 = List(1, 2)
  val list122 = 1 :: List(2)
  println(s"list12 == list12: ${list12 == list12}")

  //  BigDecimal(3.5) -> ScalaNumber -> ... -> AnyRef
  //        BigInt(1) -> ScalaNumber -> ...
  val tlst4: List[ScalaNumber] = List(BigDecimal(3.5), BigInt(1))

  //  most common ancestor
  //                        String("abc") -> AnyRef
  //      BigInt(1) -> ScalaNumber -> ... -> AnyRef
  val tlst3: List[AnyRef] = "abc" :: BigInt(1) :: Nil

  //                        Int -> Any
  //    String("abc") -> AnyRef -> Any
    val tlst5: List[Any] = 1 :: "abc" :: Nil

  // patterns
  val con1 = List(1, 2)
  con1 match {
    case 1 :: _ =>
      println(s"$con1 is a list starting with 1")
    case _ =>
      println(s"default")
  }

  val con2 = ::(1, List(2))
  println(s"con1 == con3: ${con1 == con2}")
  println(s"::.unapply(con3): ${::.unapply(con2)}")

  // first-order methods
  //  high-order methods: functions as arguments
  val l1 = List(1, 2, 3)
  val l2 = List(4, 5)
  println(s"${l1 ::: l2}")

  //  Right association
  case class MyBuf[T](d: List[T]) {
    def #:(elem: T): MyBuf[T] = MyBuf(elem :: d)
  }

  val buf1 = MyBuf(List(1))
  val buf2 = 23 #: buf1
  println(buf2)

  // zip
  val left = List(1, 2, 3)
  val right = List('a', 'b', 'b')
  println(s"left zip right: ${left zip right}")

  // mkString
  println(s"left: ${left.mkString("[", ", ", "]")}")
  val rand = Random(12)
  val passcodes = (0 to 1000).map(_ => rand.nextInt())
  val samples = passcodes.take(10).toList
  println(s"Sample passcodes: ${samples.mkString("\n\t", "\n\t", "")}")

  // not common to access element with indices
  println(left(2))

  // performance: list vs vector
    private def sum1(notes: String, nums: Seq[Int], iter: Int): Unit = {
      profiling(notes) {
        var sum = 0L
        (0 to iter).foreach { i =>
          sum += nums(500000 + i)
        }
      }
    }

    sum1("List indexing: ", (0 to 1000000).toList, 2000)
    sum1("Vector indexing: ", (0 to 1000000).toVector, 2000)

  // high-order methods
  //   map, flatMap, foreach
  println(s"left increment: ${left.map(_ + 1)}")
  println(s"left cloned x3 flattened: ${left.flatMap(i => List(i, i, i))}")
  println(s"left cloned x3: ${left.map(i => List(i, i, i))}")

  //   filter ...
  //   forall, exists
  //   foldLeft, foldRight
  //           op    s = "123"
  //         op  3   s = "12"
  //       op  2     s = "1"
  //     ""  1       s = ""
  val concatFromLeft = left.foldLeft("")((s, i) => s + i.toString)
  println(s" concatFromLeft: $concatFromLeft")
  //       op        s = "321"
  //      1  op      s = "32"
  //        2  op    s = "3"
  //          3  ""  s = ""
  val concatFromRight = left.foldRight("")((i, s) => s + i.toString)
  println(s"concatFromRight: $concatFromRight")
  //   sortWith
  val sorted = List(1, 12, -3, 2).sortWith(_ < _)
  println(sorted)
  val sorted1 = List(1, 12, -3, 2).sortWith(Math.abs(_) < Math.abs(_))
  println(sorted1)

  // Example: List reversal using fold
  val list1 = List(2, 3, 4, 5)
  val revList1 = list1.foldLeft(List[Int]())((s, i) => i :: s)
  println(s"$list1 reversed: $revList1")

  // lazy zip
  val lz1 = List(1, 2).lazyZip(List('a', 'b'))
  println(lz1)

  val lz1m = (1 to 10000000).map(_.toString).toList

  def zipSample[T](src: List[T], count: Int) = {
    profiling("Zip op") {
      val zipped = src zip src
      zipped.take(10)
    }
  }
  zipSample(lz1m, 20)

  def lazyZipSample[T](src: List[T], count: Int) = {
    profiling("lazyZip op") {
      val zipped = src lazyZip src
      zipped.take(10)
    }
  }
  lazyZipSample(lz1m, 20)

  // type reference
  def mysort[T](less: (T, T) => Boolean)
              (xs: List[T]): List[T] = xs.sortWith(less)

  def mysort1[T](xs: List[T])(less: (T, T) => Boolean): List[T] = xs.sortWith(less)


  val abcd = List('a', 'c', 'd', 'b')
  val sort1 = abcd.sortWith(_ > _)
  // msort(_ > _)(abcd)
//  msort(_ > _)(abcd)
  println(s" mysort(abcd): ${mysort[Char](_ < _)(abcd)}")
  println(s"mysort1(abcd): ${mysort1(abcd)(_ < _)}")
  // library design principle:
  //   When designing a polymorphic method that takes some non-function arguments and a function argument,
  //   place the function argument last.

  def mysort2[T](xs: List[T])(ord: T => Ordered[T]): List[T] = xs.sortWith((a, b) => ord(a) < b)
  val charToOrdered = (c: Char) => new Ordered[Char] {
    override def compare(that: Char): Int = c - that
  }
  println(s"mysort2(abcd): ${mysort2(abcd)(charToOrdered)}")
  val list321 = List(3, 2, 1)
  val intToOrdered = (i: Int) => new Ordered[Int] {
    override def compare(that: Int): Int = i - that
  }
  println(s"mysort2(list321): ${mysort2(list321)(intToOrdered)}")
  import math.Ordered.orderingToOrdered
  def mysort3[T](xs: List[T])(implicit ord: T => Ordered[T]): List[T] = xs.sortWith(_ < _)
  println(s"mysort3(abcd): ${mysort3(abcd)}")
//  val charOrdering: Ordering[Char] = implicitly[Ordering[Char]]
//  implicit val charOrdered: Char => Ordered[Char] = c => orderingToOrdered(c)

  // using Ordering
  def mysort4[T](xs: List[T])(using Ordering[T]): List[T] = xs.sortWith(_ < _)
  println(s"mysort4(abcd): ${mysort4(abcd)}")
  // println(charOrdering)


  def main(args: Array[String]): Unit = {

  }
}
