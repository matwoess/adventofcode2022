package day07

import java.io.File

abstract class Node(open val name: String) {
    abstract fun getNodeSize(): Int
    abstract fun printIndented(indent: String)
}

data class Directory(override val name: String, val parent: Directory?) : Node(name) {
    val children: MutableList<Node> = mutableListOf()
    override fun getNodeSize() = children.sumOf { it.getNodeSize() }
    override fun toString() = " - $name (dir, size=${getNodeSize()})"

    override fun printIndented(indent: String) {
        println("$indent$this")
        children.forEach { it.printIndented("$indent  ") }
    }

    fun getFoldersRecursive(): List<Directory> {
        return this.children.filterIsInstance<Directory>().flatMap { it.getFoldersRecursive() } + this
    }
}

data class File(override val name: String, val size: Int) : Node(name) {
    override fun getNodeSize() = size
    override fun printIndented(indent: String) = println("$indent$this")

    override fun toString(): String {
        return " - $name (file, size=$size)"
    }
}

data class FileSystem(val root: Directory = Directory("/", null)) {
    fun printFileSystem() {
        root.printIndented("")
    }
}

data class Interpreter(val fs: FileSystem, var pwd: Directory) {
    fun runCommand(cmd: Command) {
        when (cmd.prg) {
            Program.CD -> {
                pwd = when (cmd.arg) {
                    "/" -> fs.root
                    ".." -> pwd.parent!!
                    else -> {
                        val dir = pwd.children.first { n -> n.name == cmd.arg }
                        if (dir is Directory) dir
                        else throw RuntimeException("directory ${cmd.arg} not found in $pwd")
                    }
                }
            }

            Program.LS -> {
                for (line in cmd.output!!) {
                    val splitLine = line.split(" ")
                    if (splitLine[0] == "dir") {
                        val dir = Directory(splitLine[1], pwd)
                        pwd.children.add(dir)
                    } else {
                        val file = File(splitLine[1], splitLine[0].toInt())
                        pwd.children.add(file)
                    }
                }
            }
        }
    }
}

enum class Program {
    CD, LS
}

data class Command(val prg: Program, val arg: String?, val output: List<String>?)

fun interpretInput(text: String): FileSystem {
    val fs = FileSystem()
    val interpreter = Interpreter(fs, fs.root)
    val commands = mutableListOf<Command>()
    // ignores first command which is always "cd /"
    for (cmd in text.split("\n$ ")) {
        if (cmd.isEmpty()) continue
        if (cmd.startsWith("cd")) {
            val dir = cmd.split(" ")[1]
            commands.add(Command(Program.CD, dir, null))
        } else if (cmd.startsWith("ls")) {
            val output = cmd.split("\n").drop(1)
            commands.add(Command(Program.LS, null, output))
        }
    }
    commands.forEach { interpreter.runCommand(it) }
    return fs
}

fun part1(fs: FileSystem): Int {
    return fs.root.getFoldersRecursive().filter { it.getNodeSize() < 100000 }.sumOf { it.getNodeSize() }
}

fun part2(fs: FileSystem): Int {
    val fsSize = 70000000
    val required = 30000000
    val unused = fsSize - fs.root.getNodeSize()
    val toFree = required - unused
    return fs.root.getFoldersRecursive().filter { it.getNodeSize() >= toFree }.minOf { it.getNodeSize() }
}


fun main() {
    val input = File("inputs/day07.txt").readText()
    val fileSystem = interpretInput(input)
    fileSystem.printFileSystem()
    val answer1 = part1(fileSystem)
    println("Answer 1: $answer1") // 1517599
    val answer2 = part2(fileSystem)
    println("Answer 2: $answer2") // 2481982
}