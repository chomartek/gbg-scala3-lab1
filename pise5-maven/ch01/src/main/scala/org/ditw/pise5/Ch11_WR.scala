package org.ditw.pise5

import org.apache.commons.compress.archivers.zip.{ZipArchiveInputStream, ZipArchiveOutputStream}
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.io.IOUtils
import org.apache.commons.io.output.ByteArrayOutputStream

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.zip.GZIPInputStream

object Ch11_WR {

  class Name(val firstName: String, val lastName: String) extends Ordered[Name] {
    override def compare(that: Name): Int = {
      if (lastName == that.lastName) {
        firstName.compare(that.firstName)
      } else {
        lastName.compare(that.lastName)
      }
    }
  }

  val name1 = Name("John", "Smith")
  val name2 = Name("James", "Smith")
  val name3 = Name("Johnny", "White")
  val name11 = Name("John", "Smith")

  println(List(name1, name2, name3).sorted)
  println(name1 > name2)
  println(name1 == name11)

  trait StreamTransformer {
    def pipe(strm: InputStream): InputStream
  }

  trait GZipTransformer extends StreamTransformer {
    abstract override def pipe(strm: InputStream): InputStream = {
      println("GZipTransformer")
      val bytes = IOUtils.toByteArray(strm)
      val bytesOutputStream = new ByteArrayOutputStream(1204)
      val gzip = new GzipCompressorOutputStream(bytesOutputStream)
      gzip.write(bytes)
      gzip.close()
      val gzipped = new ByteArrayInputStream(bytesOutputStream.toByteArray)
      super.pipe(gzipped)
    }
  }

  trait Base64Transformer extends StreamTransformer {
    abstract override def pipe(strm: InputStream): InputStream = {
      println("Base64Transformer")
      val bytes = IOUtils.toByteArray(strm)
      val str = Base64.getEncoder.encodeToString(bytes)
      super.pipe(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)))
    }
  }

  class MyTransformer extends StreamTransformer {
    def pipe(strm: InputStream): InputStream = {
      println("MyTransformer")
      val bytes = IOUtils.toByteArray(strm)
      println(s"- bytes: ${bytes.length}")
      new ByteArrayInputStream(bytes)
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


  println(text.length())

  val strm = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))
  val t1 = new MyTransformer with Base64Transformer with GZipTransformer
  val arr = IOUtils.toByteArray(t1.pipe(strm))
  println(arr.length)

  println(new String(arr, StandardCharsets.UTF_8))


  val unbase64 = Base64.getDecoder.decode(arr)
  val gunzip = new GZIPInputStream(new ByteArrayInputStream(unbase64))
  val unzipBuffer = new ByteArrayOutputStream(1024)
  val size = IOUtils.copy(gunzip, unzipBuffer)
  println(size)
  val unzipped = unzipBuffer.toByteArray
  println(s"---------------------- unzipped (len: ${unzipped.length}) ------------------" )
  println(new String(unzipped, StandardCharsets.UTF_8))

  def main(args: Array[String]): Unit = {

  }
}
