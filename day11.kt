package day11

import java.io.File
import java.util.*

sealed class Operation {
    object Square : Operation()
    data class Add(val value: Long) : Operation()
    data class MultiplyBy(val value: Long) : Operation()
}

fun String.toOperation(): Operation {
    val (op, value) = this.split(" ")
    return when (op) {
        "*" -> if (value == "old") Operation.Square else Operation.MultiplyBy(value.toLong())
        "+" -> Operation.Add(value.toLong())
        else -> throw RuntimeException("ERR: invalid operation '$op'")
    }
}

data class Monkey(
    val items: Queue<Long>,
    val operation: Operation,
    val testDivisor: Int,
    val trueMonkey: Int,
    val falseMonkey: Int
) {
    var nInspections = 0L
    fun inspectItems(allMonkeys: List<Monkey>, normalizeByLCM: Int = -1) {
        while (items.isNotEmpty()) {
            var item = items.remove()
            when (operation) {
                is Operation.Square -> item *= item
                is Operation.Add -> item += operation.value
                is Operation.MultiplyBy -> item *= operation.value
            }
            if (normalizeByLCM != -1) {
                item %= normalizeByLCM
            } else {
                item /= 3
            }
            if (item % testDivisor == 0L) {
                allMonkeys[trueMonkey].items.add(item)
            } else {
                allMonkeys[falseMonkey].items.add(item)
            }
            nInspections++
        }
    }

    companion object {
        fun fromStringDefinition(def: String): Monkey {
            val items = def.substringAfter("Starting items: ").substringBefore("\n")
                .split(", ")
                .map(String::toLong)
            val operation = def.substringAfter("Operation: new = old ").substringBefore("\n").toOperation()
            val testDivisor = def.substringAfter("Test: divisible by ").substringBefore("\n").toInt()
            val trueMonkey = def.substringAfter("If true: throw to monkey ").substringBefore("\n").toInt()
            val falseMonkey = def.substringAfter("If false: throw to monkey ").substringBefore("\n").toInt()
            return Monkey(items.toCollection(LinkedList()), operation, testDivisor, trueMonkey, falseMonkey)
        }
    }
}

fun preProcessData(input: String): List<Monkey> {
    val monkeyDefinitions = input.split("\n\n")
    return monkeyDefinitions.map(Monkey::fromStringDefinition)
}

fun part1(monkeys: List<Monkey>): Long {
    repeat(20) { monkeys.forEach { it.inspectItems(monkeys) } }
    val (secondHighest, highest) = monkeys.sortedBy { it.nInspections }.takeLast(2)
    return secondHighest.nInspections * highest.nInspections
}

fun part2(monkeys: List<Monkey>): Long {
    // LCM = (d1 * d2 * d3 * ...) / GCD
    // omit long LCM computation because 2 monkeys have divisor 2 and 3 --> GCD = 1
    // simply product of all test divisors
    val lcm = monkeys.map { it.testDivisor }.reduce { acc, i -> acc * i }
    repeat(10000) { monkeys.forEach { it.inspectItems(monkeys, normalizeByLCM = lcm) } }
    val (secondHighest, highest) = monkeys.sortedBy { it.nInspections }.takeLast(2)
    return secondHighest.nInspections * highest.nInspections
}


fun main() {
    val input = File("inputs/day11.txt").readText()
    var data = preProcessData(input)
    val dataCopy = data.map { it.copy(items = it.items.toCollection(LinkedList())) }
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 51075
    val answer2 = part2(dataCopy)
    println("Answer 2: $answer2") // 11741456163
}