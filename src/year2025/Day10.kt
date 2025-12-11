@file:Suppress("ArrayInDataClass")

package year2025

import utils.*

fun main() {
    val on = '#'
    val off = '.'

    data class PatternItem(val pattern: BooleanArray, val count: Long)
    data class JoltItem(val pattern: LongArray, val count: Long)
    data class Machine(
        val pattern: BooleanArray,
        val buttons: List<List<Int>>,
        val jolt: LongArray,
    )

    val input = Input.get(2025, 10)
        .asLines()
        .map { line ->
            val pattern = line.substring(line.indexOf('[') + 1, line.indexOf(']')).toCharArray().map { it == on }.toBooleanArray()
            val jolt = line.substring(line.indexOf('{') + 1, line.indexOf('}')).split(",").map { it.toLong() }.toLongArray()
            val buttons = line.substring(line.indexOf(']') + 1, line.indexOf('{')).trim().split(" ").map { b -> b.substring(1, b.length - 1).split(",").map { it.toInt() } }
            Machine(pattern, buttons, jolt)
        }

    fun part1(): Any {
        return input.parallelStream().map { machine ->
            val seen = mutableSetOf<BooleanArray>()
            val queue = ArrayDeque<PatternItem>()
            queue.add(PatternItem(BooleanArray(machine.pattern.size) { false }, 0))
            seen.add(queue.first().pattern)
            while (queue.isNotEmpty()) {
                val currentWrap = queue.removeFirst()
                val current = currentWrap.pattern
                if (current.contentEquals(machine.pattern)) return@map currentWrap.count
                machine.buttons.forEach { button ->
                    val next = current.clone()
                    button.forEach { next[it] = !next[it] }
                    if (seen.add(next)) queue.addLast(PatternItem(next, currentWrap.count + 1))
                }
            }
            throw Error()
        }.toList().sum()
    }

    fun part2(): Any {
        val i = IntArray(0) { 0 }
        return input.parallelStream().map { machine ->
            val seen = mutableSetOf<LongArray>()
            val queue = ArrayDeque<JoltItem>()
            queue.add(JoltItem(machine.jolt, 0))
            seen.add(machine.jolt)
            while (queue.isNotEmpty()) {
                val currentWrap = queue.removeFirst()
                val current = currentWrap.pattern
                if (current.all { it == 0L }) {
                    println("${i[0]++}")
                    return@map currentWrap.count
                }
                machine.buttons.forEach { button ->
                    val next = current.clone()
                    button.forEach { next[it]-- }
                    if (next.any { it < 0 }) return@forEach
                    if (seen.add(next)) queue.addLast(JoltItem(next, currentWrap.count + 1))
                }
            }
            throw Error()
        }.toList().sum()
    }

    println("Part 1: " + part1())
    println("Part 2: " + part2())
}