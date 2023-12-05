package year2023

import utils.*
import kotlin.math.min

object Type {
    const val soil = "soil"
    const val seed = "seed"
    const val fertilizer = "fertilizer"
    const val water = "water"
    const val light = "light"
    const val temperature = "temperature"
    const val humidity = "humidity"
    const val location = "location"
}

fun main() {
    data class Range(val source: Long, val destination: Long, val length: Long)

    class BaseMapper(private val ranges: MutableList<Range> = mutableListOf()) {
        fun get(i: Long): Long {
            val range = ranges.firstOrNull { i >= it.source && i < it.source + it.length } ?: return i
            return range.destination + (i - range.source)
        }

        fun set(source: Long, destination: Long, length: Long) {
            ranges.add(Range(source, destination, length))
        }
    }

    class Mapper {
        private val maps = mutableMapOf<String, MutableMap<String, BaseMapper>>()
        fun get(source: String, destination: String, sourceValue: Long): Long = maps
            .getOrDefault(source, null)
            ?.getOrDefault(destination, null)
            ?.get(sourceValue)
            ?: sourceValue

        fun add(source: String, destination: String, sourceValue: Long, destinationValue: Long, length: Long) {
            val inner = maps.getOrPut(source, ::mutableMapOf)
            val base = inner.getOrPut(destination, ::BaseMapper)
            base.set(sourceValue, destinationValue, length)
        }
    }

    val map = Mapper()
    val reverse = Mapper()
    val seeds = mutableListOf<Long>()

    var source = ""
    var destination = ""
    Input.get(2023, 5)
        .map(String::trim)
        .filter(String::isNotBlank)
        .forEach { line ->
            if (line.startsWith("seeds:")) {
                line.removePrefix("seeds: ").split(" ").map(String::toLong).forEach(seeds::add)
            } else if (line[0].isDigit()) {
                val split = line.split(" ").map(String::toLong)
                val destinationStart = split[0]
                val sourceStart = split[1]
                val length = split[2]
                map.add(source, destination, sourceStart, destinationStart, length)
                reverse.add(destination, source, destinationStart, sourceStart, length)
            } else {
                val converterInfo = line.split(" ").first().split("-to-")
                source = converterInfo[0]
                destination = converterInfo[1]
            }
        }

    fun traverse(
        at: Long,
        start: String,
        order: List<String>
    ): Long {
        var source = start
        var value = at
        for (destination in order) {
            value = map.get(source, destination, value)
            source = destination
        }
        return value
    }

    fun part1() {
        val order = listOf(
            Type.soil,
            Type.fertilizer,
            Type.water,
            Type.light,
            Type.temperature,
            Type.humidity,
            Type.location
        )
        println(seeds.minOfOrNull { traverse(it, Type.seed, order) })
    }
    part1()

    fun part2() {
        val seedRanges = mutableListOf<Range>()
        for (i in 0..<seeds.size step 2) seedRanges.add(Range(seeds[i], 0, seeds[i + 1]))

        val order = listOf(
            Type.soil,
            Type.fertilizer,
            Type.water,
            Type.light,
            Type.temperature,
            Type.humidity,
            Type.location
        )
        val locations = seedRanges
            // Shhhh, just let it go brrrr, it's not super under optimized
            .parallelStream()
            .map { range ->
                println(range)
                var result = Long.MAX_VALUE
                for (seed in range.source..<range.source + range.length) {
                    var source = Type.seed
                    var value = seed
                    for (destination in order) {
                        value = map.get(source, destination, value)
                        source = destination
                    }
                    result = min(value, result)
                }
                result
            }
            .toList()
        println(locations.min())
    }
    part2()
}