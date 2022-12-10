package day10

import java.io.File


enum class InstructionType { ADDX, NOOP }
data class Instruction(val type: InstructionType, val arg: Int?)

data class CPU(var regX: Int = 1) {
    val regXHistory = mutableListOf<Int>()

    fun executeInstruction(instruction: Instruction) {
        when (instruction.type) {
            InstructionType.NOOP -> regXHistory.add(regX)
            InstructionType.ADDX -> {
                regXHistory.add(regX)
                regXHistory.add(regX)
                regX += instruction.arg!!
            }
        }
    }
}

fun preProcessData(programText: List<String>): List<Int> {
    val instructions = programText.map { line ->
        val parts = line.split(" ")
        Instruction(
            InstructionType.valueOf(parts[0].uppercase()),
            if (parts.size > 1) parts[1].toInt() else null
        )
    }
    val cpu = CPU()
    instructions.forEach(cpu::executeInstruction)
    return cpu.regXHistory
}

fun part1(regXHistory: List<Int>): Int {
    return listOf(20, 60, 100, 140, 180, 220).sumOf { it * regXHistory[it - 1] }
}

fun part2(regXHistory: List<Int>, displayWidth: Int = 40): String {
    val display = mutableListOf<Char>()
    for ((cycle, posSprite) in regXHistory.withIndex()) {
        val posCRT = cycle % displayWidth
        if (posCRT in (posSprite - 1).rangeTo(posSprite + 1)) {
            display.add('#')
        } else {
            display.add('.')
        }
    }
    return display.chunked(displayWidth).joinToString(separator = "\n") { it.joinToString(separator = " ") }
}


fun main() {
    val input = File("inputs/day10.txt").readLines()
    val data = preProcessData(input)
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 14220
    val answer2 = part2(data)
    println("Answer 2: \n$answer2") // ANSWER
}