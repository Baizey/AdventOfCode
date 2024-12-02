package year2024

import utils.*
import kotlin.math.abs

fun main() {
    val rawInput = Input.get(2024, 1)
    val lines = rawInput.map { line -> line.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } }
    val left = lines.map { it[0] }.sorted()
    val right = lines.map { it[1] }.sorted()

    fun part1() {
        var result = 0L
        for (i in 0..left.lastIndex) {
            result += abs(left[i] - right[i])
        }
        println(result)

    }

    fun part2() {
        val lookup = right.groupBy { it }
        val result = left.sumOf { it * lookup.getOrDefault(it, emptyList()).size }
        println(result)
    }

    part1()
    part2()
}