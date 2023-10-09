package org.ditw.pise5.ch15

import scala.collection.StringOps

class Base(val k: Int) {
  override def equals(obj: Any): Boolean = obj match {
    case b2: Base => b2.k == k
    case _ => false
  }

  override def hashCode(): Int = k
}
class Child(k1: Int, val k2: Boolean) extends Base(k1) {
  override def equals(obj: Any): Boolean = obj match {
    case c2: Child => c2.k2 == k2 && c2.k == k
  }

  override def hashCode(): Int = {
    if (k2) 10000 + k1
    else 20000 + k1
  }
}

object Ch15WR {

  // 1. Sequences
  val listSeq: Seq[Int] = List(1, 2, 3)
  val vecSeq: Seq[Int] = Vector(1, 2, 3)
  println(listSeq == vecSeq)

  //  variance of array
//  val objList: List[Any] = List[Int](1, 2, 3)
//  val objArr: Array[Any] = Array[Int](1, 2, 3)

  // ListBuffer
  import collection.mutable

  val buf = mutable.ListBuffer[Int]()
  buf += 1
  buf ++= Seq(2, 3)
  println(buf)

  // String
  val str1 = "Hi"
  println(str1.map(_.toUpper))
  val t1: StringOps = "abc"
  println(t1.map(_.toUpper))
  val t2 = Predef.augmentString("abc")
//  t2.map(_.toUpper)
//  println(t2.map(_.toUpper))

  // 2. Sets and maps
  val set1 = Set(1, 2)
  val mutSet1 = mutable.Set(1, 2)
  // set1 += 3
  mutSet1 += 3


  // Set invariant

  // https://stackoverflow.com/questions/676615/why-is-scalas-immutable-set-not-covariant-in-its-type
  // 1. function
  //   trait SetOps[A, +CC[_], +C <: _root_.scala.collection.SetOps[A, CC, C]]
  //      extends _root_.scala.collection.IterableOps[A, CC, C]
  //      with (A => _root_.scala.Boolean)
  // 2. On the issue of sets, I believe the non-variance stems also from the implementations.
  //   Common sets are implemented as hashtables, which are non-variant arrays of the key type.
  //   I agree it's a slightly annoying irregularity.
  //   https://github.com/scala/scala/blob/2.13.x/src/library/scala/collection/immutable/HashSet.scala

  // val s0: Set[Any] = Set[String]("a", "b")
  val s1 = Set[String]("a", "b")
  println(s"s1(\"a\") : ${s1("a")}")
  println(s"s1(\"aa\"): ${s1("aa")}")

//  case class Som[+T](v: T) extends (T => Boolean) {
//    def apply(t: T): Boolean = true
//  }


  // Map
  var map1: Map[Int, Base] = Map(1 -> Base(1))
  println(map1)
  map1 = Map(2 -> Child(1, true))
  println(map1)

  var map2: Map[Base, Int] = Map(Base(1) -> 1)
  println(map2)
  val map3 = Map(Child(2, false) -> 2)
  // map2 = map3

  // sorted set / map

  // 3. selecting mutable versus immutable collections

  // 5. tuples

  def main(args: Array[String]): Unit = {

  }
}
