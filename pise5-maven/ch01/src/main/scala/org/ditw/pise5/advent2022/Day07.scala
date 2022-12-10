package org.ditw.pise5.advent2022

import org.apache.commons.io.IOUtils

import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import scala.collection.mutable.ListBuffer

object Day07 {
  trait Node {
    val name: String
    def size: Long
    val children: Seq[Node]

    def findAll(filter: Node => Boolean): Seq[Node]
  }

  class FileNode(val name: String, val size: Long, val children: Seq[Node] = Nil) extends Node {
    override def toString: String = s"[$size $name]"

    override def findAll(filter: Node => Boolean): Seq[Node] =
      if (filter(this)) Seq(this) else Seq()
  }

  import collection.mutable

  class DirNode(val name: String, val children: Seq[Node]) extends Node {
    def size: Long = children.map(_.size).sum
    override def toString: String = s"[dir $size $name]"

    override def findAll(filter: Node => Boolean): Seq[Node] = {
      val buf = ListBuffer[Node]()
      if (filter(this)) buf += this
      buf ++= children.flatMap(_.findAll(filter))
      buf.toSeq
    }
  }
  private def buildFileSysTree(input: String): Node = {
    val lineIterator = input.linesIterator
    val firstLine = lineIterator.next()
    assert(firstLine == "$ cd /")

    val node = buildDirRecur("/", mutable.ListBuffer(), lineIterator)
    node
  }

  private val CmdGoBackToParentDir = "$ cd .."
  private val CmdLs = "$ ls"
  private val CmdTempl = "$ cd"
  private val LsDirTempl = "dir"

  private def buildDirRecur(dirName: String, children: mutable.ListBuffer[Node], lineIterator: Iterator[String]): Node = {
    // 1. $ ls : get listing
    // 2. $ cd
    // 2.1 $ cd .. : return
    // 2.2 $ cd [childDir] : call buildDirRecur
    if (lineIterator.hasNext) {
      val line = lineIterator.next()
      line match {
        case CmdGoBackToParentDir =>
          new DirNode(dirName, children.toSeq)
        case CmdLs =>
          buildDirRecur(dirName, children, lineIterator)
        case _ if line.startsWith(CmdTempl) =>
          val childDir = line.substring(CmdTempl.length).trim
          val child = buildDirRecur(childDir, mutable.ListBuffer(), lineIterator)
          children += child
          buildDirRecur(dirName, children, lineIterator)
        case _ => // ls file lines
          if (line.startsWith(LsDirTempl)) {
            val childDirName = line.substring(LsDirTempl.length).trim
            // todo: not add here
            new DirNode(childDirName, Seq())
          } else {
            val ptnFile = """(\d+) ([\w\.]+)""".r
            val ptnFile(fileSize, fileName) = line
            children += new FileNode(fileName, fileSize.toLong)
          }

          buildDirRecur(dirName, children, lineIterator)
      }
    } else {
      new DirNode(dirName, children.toSeq)
    }
  }

  def main(args: Array[String]): Unit = {
    val testInput = """$ cd /
                      |$ ls
                      |dir a
                      |14848514 b.txt
                      |8504156 c.dat
                      |dir d
                      |$ cd a
                      |$ ls
                      |dir e
                      |29116 f
                      |2557 g
                      |62596 h.lst
                      |$ cd e
                      |$ ls
                      |584 i
                      |$ cd ..
                      |$ cd ..
                      |$ cd d
                      |$ ls
                      |4060174 j
                      |8033020 d.log
                      |5626152 d.ext
                      |7214296 k""".stripMargin

    val dirSizeFilter = (n: Node) => {
      n.isInstanceOf[DirNode] && n.size <= 100000
    }
    val testFileSys = buildFileSysTree(testInput)
    println(testFileSys)
    val testNodes = testFileSys.findAll(dirSizeFilter)
    println(s"All dir size sum: ${testNodes.map(_.size).sum}")

    val ins = new FileInputStream("advent-2022/input-07.txt")
    val input = IOUtils.toString(ins, StandardCharsets.UTF_8)
    ins.close()

    val fileSys = buildFileSysTree(input)
    println(fileSys)
    val dirNodes = fileSys.findAll(dirSizeFilter)
    println(s"All dir size sum: ${dirNodes.map(_.size).sum}")

    val requiredSpace = 30000000L - (70000000L - fileSys.size)
    val allDirs = fileSys.findAll(n => n.isInstanceOf[DirNode])
    val firstDir = allDirs.sortBy(_.size).find(_.size > requiredSpace)
    println(firstDir)
  }
}
