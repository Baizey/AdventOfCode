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
            for (j in i + 1 until input.size) {
                val b = input[j]
                max = maxOf(max, (abs(a.x - b.x) + 1) * (abs(a.y - b.y) + 1))
            }
        }
        return max
    }

    fun orient(a: Nav, b: Nav, c: Nav): Long =
        (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)

    fun onSeg(a: Nav, b: Nav, c: Nav): Boolean =
        minOf(a.x, b.x) <= c.x && c.x <= maxOf(a.x, b.x) &&
                minOf(a.y, b.y) <= c.y && c.y <= maxOf(a.y, b.y)

    fun segInter(a: Nav, b: Nav, c: Nav, d: Nav): Boolean {
        val o1 = orient(a, b, c)
        val o2 = orient(a, b, d)
        val o3 = orient(c, d, a)
        val o4 = orient(c, d, b)

        if (o1 * o2 < 0 && o3 * o4 < 0) return true
        if (o1 == 0L && onSeg(a, b, c)) return true
        if (o2 == 0L && onSeg(a, b, d)) return true
        if (o3 == 0L && onSeg(c, d, a)) return true
        if (o4 == 0L && onSeg(c, d, b)) return true
        return false
    }

    fun lineIntersectsAxisAlignedSquare(c1: Nav, c2: Nav, p1: Nav, p2: Nav): Boolean {
        val minX = minOf(c1.x, c2.x)
        val maxX = maxOf(c1.x, c2.x)
        val minY = minOf(c1.y, c2.y)
        val maxY = maxOf(c1.y, c2.y)

        val A = Nav(minX + 1, minY + 1)
        val B = Nav(maxX - 0, minY + 1)
        val C = Nav(maxX - 0, maxY - 0)
        val D = Nav(minX + 1, maxY - 0)

        return segInter(p1, p2, A, B) ||
                segInter(p1, p2, B, C) ||
                segInter(p1, p2, C, D) ||
                segInter(p1, p2, D, A)
    }

    fun part2(): Any {
        var max = 0L
        for (i in input.indices) {
            val a = input[i]
            for (j in i + 1 until input.size) {
                val b = input[j]

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

                max = maxOf(max, (abs(a.x - b.x) + 1) * (abs(a.y - b.y) + 1))
            }
        }
        return max
    }

    println("Part 1: " + part1())
    println("Part 2: " + part2())
}