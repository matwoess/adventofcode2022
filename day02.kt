import java.io.File

enum class Shape {
    Rock, Paper, Scissors;

    fun getScore() = when (this) {
        Rock -> 1
        Paper -> 2
        Scissors -> 3
    }

    fun getInferior() = when (this) {
        Rock -> Scissors
        Paper -> Rock
        Scissors -> Paper
    }

    fun getSuperior() = when (this) {
        Rock -> Paper
        Paper -> Scissors
        Scissors -> Rock
    }

    companion object {
        fun fromChar(char: Char): Shape {
            return when (char) {
                'A' -> Rock
                'B' -> Paper
                'C' -> Scissors
                'X' -> Rock
                'Y' -> Paper
                'Z' -> Scissors
                else -> throw RuntimeException("Invalid argument: $char")
            }
        }
    }
}

data class Argument(val ch: Char)

enum class Outcome {
    Win, Draw, Loss;

    fun getScore() = when (this) {
        Loss -> 0
        Draw -> 3
        Win -> 6
    }

    companion object {
        fun fromArgument(arg: Argument) = when (arg.ch) {
            'X' -> Loss
            'Y' -> Draw
            'Z' -> Win
            else -> throw RuntimeException("invalid argument: ${arg.ch}")
        }
    }
}

data class Round(val enemyShape: Shape, val argument: Argument) {
    fun getOutcome(myShape: Shape): Outcome {
        return when (myShape) {
            enemyShape.getSuperior() -> Outcome.Win
            enemyShape.getInferior() -> Outcome.Loss
            else -> Outcome.Draw
        }
    }

    companion object {
        fun fromString(line: String): Round {
            val (shapeCh, argCh) = line.split(" ").map { it[0] }.take(2)
            return Round(Shape.fromChar(shapeCh), Argument(argCh))
        }
    }
}


fun preProcessData(lines: List<String>): List<Round> {
    return lines.map(Round::fromString)
}


fun part1(rounds: List<Round>): Int {
    var sum = 0
    for (round in rounds) {
        val myShape = Shape.fromChar(round.argument.ch)
        sum += myShape.getScore() + round.getOutcome(myShape).getScore()
    }
    return sum
}

fun part2(rounds: List<Round>): Int {
    var sum = 0
    for (round in rounds) {
        val outcome = Outcome.fromArgument(round.argument)
        val myShape = when (outcome) {
            Outcome.Win -> round.enemyShape.getSuperior()
            Outcome.Loss -> round.enemyShape.getInferior()
            Outcome.Draw -> round.enemyShape
        }
        sum += myShape.getScore() + outcome.getScore()
    }
    return sum
}


fun main() {
    val fileData = File("inputs/day02.txt").readLines()
    val processedData = preProcessData(fileData)
    val answer1 = part1(processedData)
    println("Answer 1: $answer1") // 15572
    val answer2 = part2(processedData)
    println("Answer 2: $answer2") // 16098
}