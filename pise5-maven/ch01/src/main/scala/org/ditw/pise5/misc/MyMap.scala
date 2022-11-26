package org.ditw.pise5.misc

import java.util
import scala.collection.{IterableFactory, MapFactory, MapFactoryDefaults, MapOps, StrictOptimizedMapOps, mutable}
import java.util.HashMap as JHashMap

object MyMap extends MapFactory[MyMap] {
  override def empty[K, V]: MyMap[K, V] = {
    new MyMap(new JHashMap[K, V]())
  }

  import collection.mutable
  override def from[K, V](it: IterableOnce[(K, V)]): MyMap[K, V] = newBuilder.addAll(it).result()

  override def newBuilder[K, V]: mutable.Builder[(K, V), MyMap[K, V]] = new mutable.Builder[(K, V), MyMap[K, V]] {
    private val _map = new JHashMap[K, V]()
    override def result(): MyMap[K, V] = new MyMap(_map)
    override def clear(): Unit = _map.clear()
    override def addOne(elem: (K, V)): this.type = {
      _map.put(elem._1, elem._2)
      this
    }
  }

  def main(args: Array[String]): Unit = {
    val jmap = new JHashMap[Int, String]()
    jmap.put(1, "first")
    jmap.put(2, "second")
    val myMap = new MyMap(jmap)

    val m2 = myMap - 1
    println(m2)

    val m3 = myMap.map(p => p._1 -> (p._2 + '!'))
    println(m3)
    val l1 = myMap.map(p => s"${p._1} -> ${p._2 + '!'}")
    println(l1)

  }
}

import collection.immutable
class MyMap[K, V](private val _map: JHashMap[K, V]) extends collection.Map[K, V]
  with MapOps[K, V, MyMap, MyMap[K, V]] {
  override def mapFactory: MapFactory[MyMap] = MyMap

  override def empty: MyMap[K, V] = mapFactory.empty

  override protected[this] def className: String = "MyMap"

  override def -(key: K): MyMap[K, V] = {
    val m2 = new JHashMap(_map)
    if (m2.containsKey(key)) {
      m2.remove(key)
    }
    new MyMap(m2)
  }

  override def -(key1: K, key2: K, keys: K*): MyMap[K, V] = {
    this - key1 - key2
  }

  import collection.JavaConverters._
  override def iterator: Iterator[(K, V)] =
    _map.entrySet().iterator().asScala.map(e => e.getKey -> e.getValue)

  override def get(key: K): Option[V] =
    if (_map.containsKey(key)) Some(_map.get(key))
    else None

  override protected def fromSpecific(coll: IterableOnce[(K, V)]): MyMap[K, V] =
    mapFactory.from(coll)

  override protected def newSpecificBuilder: mutable.Builder[(K, V), MyMap[K, V]] =
    mapFactory.newBuilder

}
