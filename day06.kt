package day06

import java.io.File

fun part1(p: String): Int {
    return p.windowed(4).indexOfFirst { s -> s.toSet().size == 4 } + 4
}

fun part2(p: String): Int {
    return p.windowed(14).indexOfFirst { s -> s.toSet().size == 14 } + 14
}


fun main() {
    val data = File("inputs/day06.txt").readText()
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 1760
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 2974
}