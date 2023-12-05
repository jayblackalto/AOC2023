package day4

import println
import readInput
import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return input.filter { it.isNotBlank() }.sumOf {
            val (_, winnersString, mineString) = it.replace(Regex(" +"), " ").split(':', '|').map { it.trim() }
            val winners = winnersString.split(" ").map { it.toInt() }
            val mine = mineString.split(" ").map { it.toInt() }
            val myWinners = mine.filter { it in winners }
            2.0.pow(myWinners.size - 1).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("day4/example1")
    check(part1(testInput) == 13) { "${part1(testInput)} == 13" }

//    val testInput2 = readInput("day4/example2")
//    check(part2(testInput2) == 467835) { "${part2(testInput2)} == 467835" }

    val input = readInput("day4/test")
    part1(input).println()
    part2(input).println()
}
