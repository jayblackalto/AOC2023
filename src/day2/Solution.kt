package day2

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val counts = mapOf("red" to 12, "green" to 13, "blue" to 14)

        return input.sumOf { line ->
            val (game, rest) = line.split(":").map { it.trim() }
            val matches = Regex("([0-9]+) (blue|red|green)").findAll(rest)
            val possible = matches.all {
                val count = it.groups.get(1)!!.value.toInt()
                val colour = it.groups.get(2)!!.value
                counts[colour]!! >= count
            }
            if (possible) game.split(" ").last().toInt() else 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val (game, rest) = line.split(":").map { it.trim() }
            val matches = Regex("([0-9]+) (blue|red|green)").findAll(rest)
            val counts = matches.map {
                val count = it.groups.get(1)!!.value.toInt()
                val colour = it.groups.get(2)!!.value
                colour to count
            }.groupBy { it.first }.mapValues { it.value.maxOf { it.second } }
            counts.values.reduce { acc, i -> acc * i }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day2/example1")
    check(part1(testInput) == 8) { "${part1(testInput)} == 8" }

    val testInput2 = readInput("day2/example2")
    check(part2(testInput2) == 2286) { "${part2(testInput2)} == 2286" }

    val input = readInput("day2/test")
    part1(input).println()
    part2(input).println()
}
