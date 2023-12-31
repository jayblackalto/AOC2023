package day3

import println
import readInput

fun main() {
    fun String.isSymbol(x: Int) =
        x in indices && (!get(x).isDigit() && get(x) != '.')

    fun String.isGear(x: Int) =
        x in indices && get(x) == '*'

    fun List<List<Boolean>>.isSerialNumber(start: Int, end: Int, lineNumber: Int): Boolean {
        val lines = (lineNumber - 1)..(lineNumber + 1)
        val chars = (start - 1)..(end + 1)
        return lines.any { l -> chars.any { c -> this.getOrNull(l)?.getOrNull(c) == true } }
    }

    fun List<List<Boolean>>.findSymbolCoordinates(start: Int, end: Int, lineNumber: Int): List<Pair<Int, Int>> {
        val lines = (lineNumber - 1)..(lineNumber + 1)
        val chars = (start - 1)..(end + 1)
        return lines.flatMap { l -> chars.mapNotNull { c -> if (this.getOrNull(l)?.getOrNull(c) == true) (l to c) else null } }
    }

    fun findSerials(symbols: List<List<Boolean>>, lineNumber: Int, line: String) = sequence {
        var start = -1
        line.forEachIndexed { charNum, c ->
            if (!c.isDigit()) {
                if (start > -1 && symbols.isSerialNumber(start, charNum - 1, lineNumber)) {
                    yield(line.substring(start, charNum).toInt())
                }
                start = -1
            } else {
                if (start == -1) start = charNum
            }
        }
        if (start > -1 && symbols.isSerialNumber(start, line.length, lineNumber)) {
            yield(line.substring(start).toInt())
        }
    }

    fun findGearRatios(symbols: List<List<Boolean>>, lineNumber: Int, line: String, ratiosByCoordinates: MutableMap<Pair<Int, Int>, MutableList<Int>>) {
        var start = -1
        line.forEachIndexed { charNum, c ->
            if (!c.isDigit()) {
                if (start > -1) {
                    symbols.findSymbolCoordinates(start, charNum - 1, lineNumber).forEach {
                        val ratios = ratiosByCoordinates.getOrPut(it) { mutableListOf() }
                        ratios += line.substring(start, charNum).toInt()
                    }
                }
                start = -1
            } else {
                if (start == -1) start = charNum
            }
        }
        if (start > -1) {
            symbols.findSymbolCoordinates(start, line.length, lineNumber).forEach {
                val ratios = ratiosByCoordinates.getOrPut(it) { mutableListOf() }
                ratios += line.substring(start).toInt()
            }
        }
    }

    fun part1(input: List<String>): Int {
        val symbols = input.map { line -> line.indices.map(line::isSymbol) }
        val serials = input.flatMapIndexed { lineNumber, line -> findSerials(symbols, lineNumber, line).toList() }
        return serials.sum()
    }

    fun part2(input: List<String>): Int {
        val symbols = input.map { line -> line.indices.map(line::isGear) }
        val coordinates = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
        input.forEachIndexed { lineNumber, line -> findGearRatios(symbols, lineNumber, line, coordinates) }
        coordinates.forEach { (k, v) -> println("$k -> $v") }
        return coordinates.filter { it.value.size > 1 }.values.sumOf { it.reduce { acc, i -> acc * i } }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("day3/example1")
    check(part1(testInput) == 4361) { "${part1(testInput)} == 4361" }

    val testInput2 = readInput("day3/example2")
    check(part2(testInput2) == 467835) { "${part2(testInput2)} == 467835" }

    val input = readInput("day3/test")
    part1(input).println()
    part2(input).println()
}
