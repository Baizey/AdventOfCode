package year2025

import utils.*

fun main() {
    val input = Input.get(2025, 5).asLines()
    val splitLine = input.indexOf("")
    val ranges = input.subList(0, splitLine)
        .map { it.split("-") }
        .map { (a, b) -> a.toLong()..b.toLong() }
    val ids = input.subList(splitLine + 1, input.size)
        .map { it.toLong() }

    infix fun LongRange.overlaps(other: LongRange): Boolean = this.first <= other.last && other.first <= this.last
    infix fun LongRange.join(other: LongRange): LongRange = minOf(this.first, other.first)..maxOf(this.last, other.last)

    fun part1(): Any = ids.count { id -> ranges.any { it.contains(id) } }

    fun part2(): Any {
        val remaining = ranges.toMutableSet()
        val resulting = ArrayList<LongRange>()

        while (remaining.isNotEmpty()) {
            var current = remaining.first()
            remaining.remove(current)
            var rerun = true
            while (rerun) {
                rerun = false
                for (other in remaining.toList()) {
                    if (current.overlaps(other)) {
                        current = current.join(other)
                        remaining.remove(other)
                        rerun = true
                    }
                }
            }
            resulting.add(current)
        }

        return resulting.sumOf { it.last - it.first + 1 }
    }

    println("Part 1: " + part1())
    println("Part 2: " + part2())
}