package year2025

import utils.*

fun main() {
    val input = Input.get(2025, 3)
        .asLines()
        .map { it.toCharArray().map { c -> c - '0' } }

    fun part1(): Any {
        return input.sumOf { line ->
            var max = 0
            for (i in 0..line.lastIndex)
                for (j in (i + 1)..line.lastIndex) {
                    val tmp = line[i] * 10 + line[j]
                    max = maxOf(max, tmp)
                }
            max
        }
    }

    fun part2(): Any {
        return input.sumOf { line ->
            var num = 0L
            var index = -1
            for (i in 0..<12) {
                val hasToKeep = 11 - i
                val sublist = line.subList(index + 1, line.size - hasToKeep)
                val maxValue = sublist.max()
                index += 1 + sublist.indexOf(maxValue)
                num = num * 10 + maxValue
            }
            println(num)
            num
        }
    }

    println("Part 1: " + part1())
    println("Part 2: " + part2())
}