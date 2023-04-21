package org.ditw.pise5.ch11

import dotty.tools.backend.jvm.ClassNode1
import org.apache.commons.compress.archivers.zip.{ZipArchiveInputStream, ZipArchiveOutputStream}
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.io.IOUtils
import org.apache.commons.io.output.ByteArrayOutputStream

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.zip.GZIPInputStream

object Ch11_WR {

  // traits are similar to interfaces + default methods in java, but with fewer limitations
  trait Trait1 { // extends Object
    def abstractMethod(v: Int): Int
    def concreteMethod(v: String): String = v.toLowerCase()

    override def toString: String = {
      println(s"Trait1's super.toString: ${super.toString}")
      "T1"
    }
  }

  trait Trait2 { }

  // How to use (mix-in) traits
  class Class1 extends Trait1 {
    override def abstractMethod(v: Int): Int = ???
  }
  class Class2 extends Class1 with Trait2
  class Class3 extends Class1, Trait2
  // class Class3 extends Class1, Class2
  class Class4 extends Class1 with Trait1 with Trait2 {
  }
  class Class5 extends Trait1, Trait2 {
    override def abstractMethod(v: Int): Int = ???
  }
  // anonymous class
  val anon1 = new Trait1 {
    override def abstractMethod(v: Int): Int = ???
  }

  // key diff: super calls are statically bound for classes, but not traits, linearization


  // thin interfaces
  // RvLogging
//  trait RvLogging {
//    @transient
//    protected lazy val LOGGER = org.apache.log4j.LogManager.getLogger(this.getClass.getSimpleName)
//  }
//
//  trait RvPerf { self: RvLogging => // Self typed annotation
//    def timed[T](desc: String)(func: => T): T = {
//      val tsStart = System.currentTimeMillis()
//      val result = func
//      val elapsed = System.currentTimeMillis() - tsStart
//      LOGGER.warn(s"[PERF] $desc (elapsed: $elapsed ms)")
//      result
//    }
//  }
//
//  class Job extends RvLogging with RvPerf {
//    def run(): Unit = {
//      timed("Job instance 1") {
//        LOGGER.warn(s"Job started at ${System.currentTimeMillis()} ms ...")
//        Thread.sleep(1000) // do stuff
//        LOGGER.warn(s"\tended at ${System.currentTimeMillis()} ms")
//      }
//    }
//  }

  // Ordered[T] trait
  class Name(val firstName: String, val lastName: String) extends Ordered[Name] {
    override def compare(that: Name): Int = {
      if (lastName == that.lastName) {
        firstName.compare(that.firstName)
      } else {
        lastName.compare(that.lastName)
      }
    }

    override def toString: String = s"$firstName $lastName"

    override def hashCode(): Int = firstName.hashCode + lastName.hashCode

    override def equals(n2: Any): Boolean = n2 match {
      case name2: Name =>
        compareTo(name2) == 0
      case _ =>
        false
    }
  }

  val name1 = Name("John", "Smith")
  val name2 = Name("Johnny", "Smith")
  val name3 = Name("James", "White")
  val name11 = Name("John", "Smith")

  println(List(name1, name2, name3).sorted.mkString("\n\t", "\n\t", ""))
  println(s"name1 > name2: ${name1 > name2}")
  println(s"name1 == name11: ${name1 == name11}")

  // stackable modifications
  trait StreamTransformer {
    def pipe(strm: InputStream): InputStream
  }

  trait GZipTransformer extends StreamTransformer {
    abstract override def pipe(strm: InputStream): InputStream = {
      println("in GZipTransformer")
      val bytes = IOUtils.toByteArray(strm)
      println(s"\tinput length: ${bytes.length}")
      val bytesOutputStream = new ByteArrayOutputStream(1024)
      val gzip = new GzipCompressorOutputStream(bytesOutputStream)
      gzip.write(bytes)
      gzip.close()
      val outBytes = bytesOutputStream.toByteArray
      println(s"\toutput length: ${outBytes.length}")
      val gzipped = new ByteArrayInputStream(outBytes)
      super.pipe(gzipped)
    }
  }

  trait Base64Transformer extends StreamTransformer {
    abstract override def pipe(strm: InputStream): InputStream = {
      println("in Base64Transformer")
      val bytes = IOUtils.toByteArray(strm)
      println(s"\tinput length: ${bytes.length}")
      val str = Base64.getEncoder.encodeToString(bytes)
      val outBytes = str.getBytes(StandardCharsets.UTF_8)
      println(s"\toutput length: ${outBytes.length}")
      super.pipe(new ByteArrayInputStream(outBytes))
    }
  }

  class MyTransformer extends StreamTransformer {
    def pipe(strm: InputStream): InputStream = {
      println("in MyTransformer")
      val outBytes = IOUtils.toByteArray(strm)
      println(s"\tlength: ${outBytes.length}")
      new ByteArrayInputStream(outBytes)
    }
  }

  val text: String =
    """
      |The second funny thing is that the trait has a super call on a method
      |declared abstract. Such calls are illegal for normal classes because they will
      |certainly fail at run time. For a trait, however, such a call can actually succeed. Since super calls in a trait are dynamically bound, the super call in
      |trait Doubling will work so long as the trait is mixed in after another trait or
      |class that gives a concrete definition to the method.
      |This arrangement is frequently needed with traits that implement stackable modifications. To tell the compiler you are doing this on purpose, you
      |must mark such methods as abstract override. This combination of modifiers is only allowed for members of traits, not classes, and it means that
      |the trait must be mixed into some class that has a concrete definition of the
      |method in question
      |""".stripMargin

  println(s"text length: ${text.length()}")

  val strm = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))
  val t1 = new MyTransformer with Base64Transformer with GZipTransformer
  val arr = IOUtils.toByteArray(t1.pipe(strm))
  val base64Text = new String(arr, StandardCharsets.UTF_8)
  println(s"base64 encoded:\n$base64Text\n-------------\n")

  val strm2 = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))
  val t2 = new MyTransformer with GZipTransformer with Base64Transformer
  val arr2 = IOUtils.toByteArray(t2.pipe(strm2))
  println(s"arr2 length: ${arr2.length}\n")

  //
  val unbase64 = Base64.getDecoder.decode(arr)
  val gunzip = new GZIPInputStream(new ByteArrayInputStream(unbase64))
  val unzipBuffer = new ByteArrayOutputStream(1024)
  val size = IOUtils.copy(gunzip, unzipBuffer)
  val unzipped = unzipBuffer.toByteArray
  println(s"---------------------- unzipped (len: ${unzipped.length}) ------------------" )
  println(new String(unzipped, StandardCharsets.UTF_8))


  // linearization
  object Linearization {
    trait FixInt {
      def m(v: Int): Int = {
        println(s"FixInt: $v")
        v
      }
    }

    trait F1 extends FixInt {
      override def m(v: Int): Int = {
        val sv = super.m(v)
        println(s"F1 (+1): v = $v, super.m(v)=$sv")
        1 + sv
      }
    }

    trait F2 extends FixInt {
      abstract override def m(v: Int): Int = {
        val sv = super.m(v)
        println(s"F2 (^2): v = $v, super.m(v)=$sv")
        val vv = sv
        vv * vv
      }
    }

    trait F3 extends F1 with F2 {
      abstract override def m(v: Int): Int = {
        val sv = super.m(v)
        println(s"F3 (+10): v = $v, super.m(v)=$sv")
        10 + sv
      }
    }

    class C1 extends F2 with F3 {
      override def m(v: Int): Int = {
        val sv = super.m(v)
        println(s"C1 (+1000): v = $v, super.m(v)=$sv")
        1000 + sv
      }
    }

    class C2 extends F3 with F2 {
      override def m(v: Int): Int = {
        val sv = super.m(v)
        println(s"C2 (+1000): v = $v, super.m(v)=$sv")
        1000 + sv
      }
    }
  }

  import Linearization._

  //  Following rules are followed for the determining the linearization:
  //   - Take the first extended trait/class and write its complete inherited hierarchy
  //     in vertical form, store this hierarchy as X.
  //   - Take the next trait/class after the with clause, write its complete hierarchy
  //     and cancel the classes or traits that are repeated in hierarchy X. Add the remaining traits/classes to the front of the hierarchy X.
  //   - Go to step 2 and repeat the process, until no trait/class is left out.
  //   - Place the class itself in front of hierarchy as head for which the hierarchy is being written.

  // extends F2:
  //   F2: F2 -> FixInt
  //   overall:  F3 ->  F1 -> F2 -> FixInt
  // with F3:
  //   F3: F3 ->  F1
  //   overall: F3 -> F1 -> F2 -> FixInt
  // C1:
  //   overall: C1 -> F3 -> F1 -> F2 -> FixInt
  // 1012
  var tt: FixInt = new C1()
  println(tt.m(1))

  // extends F3:
  //   F3: F3 -> F2 -> F1 -> FixInt
  //   overall: F3 -> F2 -> F1 -> FixInt
  // with F2:
  //   F2: F2 -> FixInt
  //   overall: F3 -> F2 -> F1 -> FixInt
  // C2:
  //   overall: C2 -> F3 -> F2 -> F1 -> FixInt
  // 1014
  tt = new C2()
  println(tt.m(1))

  // trait with parameters
  trait TParam1(val v1: Int, val v2: String) {
    def traceStr: String = s"v1 = $v1; v2: $v2"
  }

  class Cls1(v1: Int, v2: String, val v3: Boolean) extends TParam1(v1, v2) {
    def show(): Unit = {
      println(s"base: $traceStr; v3 = $v3")
    }
  }
  new Cls1(11, "str2", false).show()

//  trait TParam2(v1: Int, v2: String, val v3: Boolean) extends TParam1(v1, v2) {
//
//  }

  def main(args: Array[String]): Unit = {

  }
}
