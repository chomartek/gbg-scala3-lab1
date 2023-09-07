package org.ditw.pise5.ch14

import scala.collection.immutable.LinearSeq
import scala.collection.mutable.ListBuffer
import scala.math.ScalaNumber
import scala.util.Random

object Ch14WR {

  // 1. immutable & recursive
  //

  val arr1 = Array(1, 2, 3)
  arr1(2) = 4
  println(arr1.mkString(","))
  val lst1 = List(1, 2, 3)
  // lst(2) = 4

  val lst2 = (1 to 5).toList
  var tail = lst2.tail
  while (tail.nonEmpty) {
    println(tail)
    tail = tail.tail
  }

  // 2. covariant
  // The list type in Scala is covariant. This means that for each pair of
  //  types S and T, if S is a subtype of T, then List[S] is a subtype of List[T].
  //  For instance, List[String] is a subtype of List[Object].

  // Nil: List[Nothing]
  // Nothing: bottom type
  println(s"List() == Nil: ${List() == Nil}")
  println(s"List() eq Nil: ${List().eq(Nil)}")

  var tmpList1: List[String] = Nil
  val tmpList2: List[Int] = Nil

  //                        String("abc")
  //             Nothing -> String
  val tlst1: List[String] = "abc" :: Nil
  println(tlst1)

  //                        Int -> Any
  //             Nothing -> Int
  val tlst2: List[Any] = 1 :: Nil

  //                        String("abc") -> AnyRef
  //      BigInt(1) -> ScalaNumber -> ... -> AnyRef
  val tlst3: List[AnyRef] = "abc" :: BigInt(1) :: Nil

  //  BigDecimal(3.5) -> ScalaNumber -> ... -> AnyRef
  //        BigInt(1) -> ScalaNumber -> ...
  val tlst4: List[ScalaNumber] = List(BigDecimal(3.5), BigInt(1))

  //                        Int -> Any
  //    String("abc") -> AnyRef -> Any
  //                        Int -> Any
  val tlst5: List[Any] = 1 :: "abc" :: Nil

  case class MyBuf[T](d: List[T]) {
    def #:(elem: T): MyBuf[T] = MyBuf(elem :: d)
  }

  val buf1 = MyBuf(List(1))
  val buf2 = 23 #: buf1
  println(buf2)

  // patterns
  val con1 = ::(1, List(2))
  println(con1)

  con1 match {
    case 1 :: _ =>
      println(s"$con1 is a list starting with 1")
    case _ =>
      println(s"default")
  }
  println(s"::.unapply(con1) = ${::.unapply(con1)}")

  // first-order methods
  //   high-order methods: functions as arguments
  val l1 = List(1, 2, 3)
  val l2 = List(4, 5)
  println(s"${l1 ::: l2}")

  println(l1(2)) // not common to access element with indices
  val seq1 = Seq(1, 2, 3)

  // performance: list vs vector
  private def sum1(nums: Seq[Int], iter: Int): Unit = {
    var sum = 0L
    val tsStart = System.currentTimeMillis()
    (0 to iter).foreach { i =>
      sum += nums(500000 + i)
    }
    val msElapsed = (System.currentTimeMillis() - tsStart) / 1000.0
    println(s"Elapsed $msElapsed seconds, sum = $sum")
  }

//  sum1((0 to 1000000).toList, 2000)
//  sum1((0 to 1000000).toVector, 2000)

  // zip
  val left = List(1, 2, 3)
  val right = List('a', 'b', 'b')
  println(s"${left zip right}")

  // mkString
  println(s"left: ${left.mkString("[", ", ", "]")}")
  val rand = Random(12)
  val passcodes = (0 to 1000).map(_ => rand.nextInt())
  val samples = passcodes.take(20).toList
  println(s"Sample passcodes: ${samples.mkString("\n\t", "\n\t", "")}")

  // high-order methods
  //   map, flatMap, foreach
  //   filter ...
  //   forall, exists
  //   foldLeft, foldRight
  val concatFromLeft = left.foldLeft("")((s, i) => s + i.toString)
  println(s" concatFromLeft: $concatFromLeft")
  val concatFromRight = left.foldRight("")((i, s) => s + i.toString)
  println(s"concatFromRight: $concatFromRight")
  //   sortWith
  val sorted = List(1, 12, -3, 2).sortWith(_ < _)
  println(sorted)
  val sorted1 = List(1, 12, -3, 2).sortWith(Math.abs(_) < Math.abs(_))
  println(sorted1)

  // List object: creation ...

  // lazy zip
  val lz1 = List(1, 2).lazyZip(List('a', 'b'))
  println(lz1)

  // type reference
  def msort[T](less: (T, T) => Boolean)
              (xs: List[T]): List[T] = xs.sortWith(less)

  def msort1[T](xs: List[T])(less: (T, T) => Boolean): List[T] = xs.sortWith(less)


  val abcd = List('a', 'c', 'd', 'b')
  val sort1 = abcd.sortWith(_ > _)
  // msort(_ > _)(abcd)
//  msort(_ > _)(abcd)
  println(s" msort(abcd): ${msort[Char](_ < _)(abcd)}")
  println(s"msort1(abcd): ${msort1(abcd)(_ < _)}")

  // using Ordering?
  import math.Ordered.orderingToOrdered
  def msort2[T](xs: List[T])(using Ordering[T]): List[T] = xs.sortWith(_ < _)
  println(s"msort2(abcd): ${msort2(abcd)}")


  def main(args: Array[String]): Unit = {

  }
}
