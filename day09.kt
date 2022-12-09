package day08

import java.io.File
import kotlin.math.abs
import kotlin.math.sign

enum class Direction { U, D, L, R }
data class Motion(val direction: Direction, val distance: Int)

data class Position(var x: Int, var y: Int) {
    fun moveInDirection(direction: Direction) {
        when (direction) {
            Direction.U -> y++
            Direction.D -> y--
            Direction.L -> x--
            Direction.R -> x++
        }
    }

    private fun distanceTo(other: Position): Pair<Int, Int> {
        return Pair(other.x - x, other.y - y)
    }

    fun follow(other: Position) {
        val (dx, dy) = this.distanceTo(other)
        if (abs(dx) > 1) {
            this.x += 1 * (sign(dx.toDouble())).toInt()
            if (abs(dy) == 1) this.y += dy
        }
        if (abs(dy) > 1) {
            this.y += 1 * (sign(dy.toDouble())).toInt()
            if (abs(dx) == 1) this.x += dx
        }
    }
}

fun preProcessData(lines: List<String>): List<Motion> {
    return lines.map {
        val (a, b) = it.split(" ")
        Motion(Direction.valueOf(a), b.toInt())
    }
}

fun part1(motions: List<Motion>): Int {
    val head = Position(0, 0)
    val tail = Position(0, 0)
    val tailPositions = mutableSetOf<Position>()
    tailPositions.add(tail)
    for (m in motions) {
        for (d in 0 until m.distance) {
            head.moveInDirection(m.direction)
            tail.follow(head)
            if (!tailPositions.contains(tail)) {
                tailPositions.add(tail.copy())
            }
        }
    }
    return tailPositions.size
}

fun part2(motions: List<Motion>, nKnots: Int = 9): Int {
    val head = Position(0, 0)
    val knots: List<Position> = (0 until nKnots).map { Position(0, 0) }
    val tail = knots.last()
    val tailPositions = mutableSetOf<Position>()
    tailPositions.add(tail)
    for (m in motions) {
        for (d in 0 until m.distance) {
            head.moveInDirection(m.direction)
            var previous = head
            for (k in knots) {
                k.follow(previous)
                previous = k
            }
            if (!tailPositions.contains(tail)) {
                tailPositions.add(tail.copy())
            }
        }
    }
    return tailPositions.size
}


fun main() {
    val input = File("inputs/day09.txt").readLines()
    val data = preProcessData(input)
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 6181
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 2386
}