package day5

import println
import readInput

data class AlmanacMappingRange(val destination: Long, val source: Long, val length: Long) {
    val sourceRange: LongRange = (source until source + length)
}

data class Almanac(
    val seeds: List<Long>,
    val mappings: Map<String, List<AlmanacMappingRange>>,
) {
    val seedByRange =
        seeds.chunked(2).flatMap { (s, l) ->
            (s until s + l).toList()
        }
}

data class AlmanacCategory(val name: String, val values: List<Long>)

fun main() {
    fun List<String>.parseAlmanac(): Almanac {
        val seeds = mutableListOf<Long>()
        val mappings = mutableMapOf<String, MutableList<AlmanacMappingRange>>()
        var currentName: String? = null

        forEach { line ->
            val (name, valuesString) = when {
                ':' in line -> line.split(":", limit = 2)
                else -> listOf(currentName, line)
            }
            val values = valuesString!!.split(" ").filterNot { it.isBlank() }.map { it.toLong() }
            if (values.isNotEmpty()) {
                if (name == "seeds") {
                    seeds += values
                } else {
                    val mappingName = name!!.removeSuffix(" map")
                    val mappingsForName = mappings.getOrPut(mappingName, ::mutableListOf)
                    mappingsForName += values.chunked(3).map { (d, s, l) -> AlmanacMappingRange(d, s, l) }
                }
            }
            currentName = name
        }

        return Almanac(seeds, mappings)
    }

    fun List<AlmanacMappingRange>.resolve(value: Long): Long {
        val mapping = find { value in it.sourceRange } ?: return value
        return mapping.destination + (value - mapping.source)
    }

    fun Almanac.resolve(current: AlmanacCategory): AlmanacCategory? {
        val mappingEntry = mappings.entries.find { it.key.startsWith("${current.name}-to-") }
            ?: return null
        val (_, target) = mappingEntry.key.split("-to-")
        val values = current.values.map { mappingEntry.value.resolve(it) }
        return AlmanacCategory(target, values)
    }

    fun part1(input: List<String>): Long {
        val almanac = input.parseAlmanac()
        val seed = AlmanacCategory("seed", almanac.seeds)
        val resolvedCategories = generateSequence(seed) { almanac.resolve(it) }.associateBy { it.name }
        return resolvedCategories["location"]?.values?.min() ?: 0L
    }

    fun part2(input: List<String>): Long {
        val almanac = input.parseAlmanac()
        val seed = AlmanacCategory("seed", almanac.seedByRange)
        val resolvedCategories = generateSequence(seed) { almanac.resolve(it) }.associateBy { it.name }
        return resolvedCategories["location"]?.values?.min() ?: 0L
    }

//    val testInput = readInput("day5/example1")
//    check(part1(testInput) == 35L) { "${part1(testInput)} == 35" }

    val testInput2 = readInput("day5/example2")
    check(part2(testInput2) == 46L) { "${part2(testInput2)} == 46" }

    val input = readInput("day5/test")
    part1(input).println()
    part2(input).println()
}
