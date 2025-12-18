package year2025

import utils.Input
import utils.grid.Nav
import kotlin.math.abs

fun main() {
    val input = Input.get(2025, 9).asLongLines().map { Nav(it[0], it[1]) }

    fun part1(): Any {
        var max = 0L
        for (i in input.indices) {
            val a = input[i]
            for (j in i + 1..<input.size) {
                val b = input[j]
                max = maxOf(max, (abs(a.x - b.x) + 1) * (abs(a.y - b.y) + 1))
            }
        }
        return max
    }

    fun part2(): Any {
        var max = 0L
        for (i in input.indices) {
            val a = input[i]
            for (j in i + 1..<input.size) {
                val b = input[j]
                val value = (abs(a.x - b.x) + 1) * (abs(a.y - b.y) + 1)
                if (value <= max) continue

                val minX = minOf(a.x, b.x)
                val maxX = maxOf(a.x, b.x)
                val minY = minOf(a.y, b.y)
                val maxY = maxOf(a.y, b.y)
                if (input.any { (it.x in (minX + 1)..<maxX) && (it.y in (minY + 1)..<maxY) }) continue

                var skip = false
                for (k in input.indices) {
                    val p = input[k]
                    val p2 = input[(k + 1) % input.size]
                    if ((p.x in (minX + 1)..<maxX)) {
                        if (p.y >= maxY && p2.y <= minY) skip = true
                        if (p.y <= minY && p2.y >= maxY) skip = true
                    } else if ((p.y in (minY + 1)..<maxY)) {
                        if (p.x >= maxX && p2.x <= minX) skip = true
                        if (p.x <= minX && p2.x >= maxX) skip = true
                    }
                    if (skip) break
                }
                if (skip) continue

                max = value
            }
        }
        return max
    }

    println("Part 1: " + part1())
    println("Part 2: " + part2())
}