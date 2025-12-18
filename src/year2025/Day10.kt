@file:Suppress("ArrayInDataClass")

package year2025

import utils.*
import java.util.PriorityQueue

fun main() {
    val on = '#'
    val off = '.'

    data class Key(val value: IntArray) {
        private val h = value.contentHashCode()
        override fun hashCode() = h
        override fun equals(other: Any?) = other is Key && value.contentEquals(other.value)
    }

    data class PatternItem(val state: BooleanArray, val count: Long)
    data class JoltItem(val state: IntArray, val count: Int)
    data class Machine(
        val pattern: BooleanArray,
        val buttons: Array<IntArray>,
        val jolt: IntArray,
    )

    val input = Input.get(2025, 10)
        .asLines()
        .map { line ->
            val pattern = line.substring(line.indexOf('[') + 1, line.indexOf(']')).toCharArray().map { it == on }.toBooleanArray()
            val jolt = line.substring(line.indexOf('{') + 1, line.indexOf('}')).split(",").map { it.toInt() }.toIntArray()
            val buttons =
                line.substring(line.indexOf(']') + 1, line.indexOf('{')).trim().split(" ").map { b -> b.substring(1, b.length - 1).split(",").map { it.toInt() }.toIntArray() }
                    .toTypedArray()
            Machine(pattern, buttons, jolt)
        }

    fun part1(): Any {
        return input.parallelStream().map { machine ->
            val seen = mutableSetOf<BooleanArray>()
            val queue = ArrayDeque<PatternItem>()
            queue.add(PatternItem(BooleanArray(machine.pattern.size) { false }, 0))
            seen.add(queue.first().state)
            while (queue.isNotEmpty()) {
                val currentWrap = queue.removeFirst()
                val current = currentWrap.state
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
        return input.map { machine ->
            val buttons = machine.buttons.sortedBy { it.size }.toTypedArray()
            val queue = ArrayDeque<JoltItem>()
            val seen = mutableSetOf<Key>()
            queue.add(JoltItem(machine.jolt, 0))
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                val max = current.state.max()
                if (max == 0) {
                    return@map current.count
                }
                val idx = current.state.indexOf(max)
                buttons
                    .filter { it.contains(idx) }
                    .forEach { button ->
                        val next = current.state.clone()
                        button.forEach { next[it]-- }
                        if (!next.any { it < 0 }) {
                            if (!seen.add(Key(next))) return@forEach
                            queue.addFirst(JoltItem(next, current.count + 1))
                        }
                    }
            }
            throw Error()
        }.toList().sum()
    }

    //println("Part 1: " + part1())
    println("Part 2: " + part2())
}