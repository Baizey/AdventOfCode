package year2025

import utils.*
import kotlin.math.absoluteValue

fun main() {
    val input = Input.get(2025, 1).asLines().mapNotNull {
        when (it.first()) {
            'L' -> -it.substring(1).toInt()
            'R' -> it.substring(1).toInt()
            else -> null
        }
    }

    fun part1(): Any {
        var at = 50
        var counter = 0
        input.forEach {
            at += it
            if (at < 0) at += (1 + at.absoluteValue / 100) * 100
            if (at >= 100) at %= 100
            if (at == 0) counter++
        }
        return counter
    }

    fun part2(): Any {
        var at = 50
        var counter = 0
        input.forEach {
            val dir = if (it < 0) -1 else 1
            counter += (it.absoluteValue / 100)
            val move = dir * (it.absoluteValue % 100)
            if (move == 0) return@forEach
            val prev = at
            at += move
            if (prev != 0 && at !in 1..<100) counter++
            if (at < 0) at += (1 + at.absoluteValue / 100) * 100
            if (at >= 100) at %= 100
        }
        return counter
    }

    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}