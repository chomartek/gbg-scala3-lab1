package org.ditw.pise5.ch01
case class Person(name: String, countryCode: String)
object CountryPeople {
  def main(args: Array[String]): Unit = {

    val rvdcPeople = List(
      Person("Jiaji", "CN"),
      Person("Bin", "CN"),
      Person("Per", "SV"),
      Person("Marta", "PL"),
      Person("Mythili", "IN"),
      Person("Arindam", "IN"),
      Person("Arthur", "BR"),
    )

    val sortedMap = rvdcPeople
      .groupBy(_.countryCode)
      .mapValues(_.sortBy(_.name))

    sortedMap.toList
      .sortBy(_._1)
      .foreach { p =>
        println(s"${p._1}:")
        p._2.foreach(pp => println(s"\t$pp"))
      }

  }

}
