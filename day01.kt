import java.io.File

typealias Calories = Int

data class Elf(val foods: List<Calories>)

fun preProcessData(text: String): List<Elf> {
    val elfInputs: List<String> = text.split("\n\n")
    return elfInputs
        .map { it.split("\n").filter(String::isNotEmpty) }
        .map { foods -> Elf(foods.map { calString -> calString.toInt() }) }
}


fun part1(elves: List<Elf>): Int {
    return elves.map { it.foods.sum() }.max()
}

fun part2(elves: List<Elf>): Int {
    return elves.map { it.foods.sum() }.sorted().takeLast(3).sum()
}


fun main() {
    val fileData = File("inputs/day01.txt").readText()
    val processedData = preProcessData(fileData)
    val answer1 = part1(processedData)
    println("Answer 1: $answer1") // 68802
    val answer2 = part2(processedData)
    println("Answer 2: $answer2") // 205370
}