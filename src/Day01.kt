fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val first = it.first { it.isDigit() }
            val last = it.last { it.isDigit() }
            "$first$last".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val words = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

        tailrec fun String.chompStart(): Pair<Char, String> {
            if (first().isDigit()) return first() to this
            val wordIndex = words.indexOfFirst { this.startsWith(it) }
            if (wordIndex > -1) return ('1' + wordIndex) to this
            return drop(1).chompStart()
        }
        tailrec fun String.chompEnd(): Pair<Char, String> {
            if (last().isDigit()) return last() to this
            val wordIndex = words.indexOfLast { this.endsWith(it) }
            if (wordIndex > -1) return ('1' + wordIndex) to this
            return dropLast(1).chompEnd()
        }

        return input.sumOf { line ->
            val (first, chomped1) = line.chompStart()
            val (last, _) = chomped1.chompEnd()
            "$first$last".toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142) { "${part1(testInput)} == 142" }

    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281) { "${part2(testInput2)} == 281" }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
