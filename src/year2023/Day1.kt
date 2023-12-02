package year2023

import utils.Input

fun main() {
    val lines = Input.day(2023, 1)

    fun part1(): Long {
        var sum = 0L
        lines.forEach { line ->
            val first = line.firstOrNull { it.isDigit() } ?: return@forEach
            val last = line.last { it.isDigit() }
            sum += "$first$last".toLong()
        }
        return sum
    }

    fun part2(): Long {
        var sum = 0L
        val digits = listOf("__zero__", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        lines.forEach { line ->
            var first: Char? = null
            var firstIndex = Int.MAX_VALUE
            digits.forEachIndexed { i, v ->
                if (line.contains(v)) {
                    val index = line.indexOf(v)
                    if (index < firstIndex) {
                        firstIndex = index
                        first = ("$i")[0]
                    }
                }
            }
            val firstDigit = line.firstOrNull { it.isDigit() }
            if (firstDigit != null) {
                val index = line.indexOf(firstDigit)
                if (index < firstIndex) {
                    firstIndex = index
                    first = firstDigit
                }
            }
            var last: Char? = null
            var lastIndex = Int.MIN_VALUE
            digits.forEachIndexed { i, v ->
                if (line.contains(v)) {
                    val index = line.lastIndexOf(v)
                    if (index > lastIndex) {
                        lastIndex = index
                        last = ("$i")[0]
                    }
                }
            }
            val lastDigit = line.lastOrNull { it.isDigit() }
            if (lastDigit != null) {
                val index = line.lastIndexOf(lastDigit)
                if (index > lastIndex) {
                    lastIndex = index
                    last = lastDigit
                }
            }
            if (first == null || last == null) return@forEach
            sum += "$first$last".toLong()
        }
        return sum
    }

    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}