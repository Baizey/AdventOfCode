package year2024

import utils.Helpers.println
import utils.Input

fun main() {
    val input = Input.get(2024, 11).asLongLines()[0]

    fun calc(
        value: Long,
        depth: Int = 0,
        maxDepth: Int,
        seen: MutableMap<Long, MutableMap<Int, Long>> = mutableMapOf(),
    ): Long {
        return when {
            depth == maxDepth -> 1L
            seen.contains(value) && seen[value]!!.contains(depth) -> return seen[value]!![depth]!!
            value == 0L -> calc(1L, depth + 1, maxDepth, seen)
            value.toString().length % 2 == 0 -> {
                val str = value.toString()
                val a = str.substring(0, str.length / 2).toLong()
                val b = str.substring(str.length / 2).toLong()
                calc(a, depth + 1, maxDepth, seen) + calc(b, depth + 1, maxDepth, seen)
            }

            else -> calc(2024L * value, depth + 1, maxDepth, seen)
        }.also { seen.computeIfAbsent(value) { mutableMapOf() }[depth] = it }
    }

    fun part1() {
        input.sumOf { calc(it, maxDepth = 25) }.println()
    }

    fun part2() {
        input.sumOf { calc(it, maxDepth = 75) }.println()
    }

    part1()
    part2()
}