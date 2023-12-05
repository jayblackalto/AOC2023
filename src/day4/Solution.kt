package day4

import println
import readInput
import kotlin.math.pow

fun main() {
    fun List<String>.scoreCards() = filter { it.isNotBlank() }.map {
        val (_, winnersString, mineString) = it.replace(Regex(" +"), " ").split(':', '|').map { it.trim() }
        val winners = winnersString.split(" ").map { it.toInt() }
        val mine = mineString.split(" ").map { it.toInt() }
        val myWinners = mine.filter { it in winners }
        myWinners
    }

    fun part1(input: List<String>): Int {
        return input.scoreCards().map { 2.0.pow(it.size - 1).toInt() }.sum()
    }

    fun part2(input: List<String>): Int {
        data class WithCardNumber(val wins: Int, val number: Int)
        val scores = input.scoreCards().mapIndexed { i, it -> WithCardNumber(it.size, i + 1) }.toList()

        val total = mutableListOf<Int>()
        var nextScores = scores
        while (!nextScores.isEmpty()) {
            total += nextScores.map { it.number }
            nextScores = nextScores.flatMap {
                val take = scores.drop(it.number).take(it.wins)
                take
            }
        }
        return total.size
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("day4/example1")
    check(part1(testInput) == 13) { "${part1(testInput)} == 13" }

    val testInput2 = readInput("day4/example2")
    check(part2(testInput2) == 30) { "${part2(testInput2)} == 30" }

    val input = readInput("day4/test")
    part1(input).println()
    part2(input).println()
}
