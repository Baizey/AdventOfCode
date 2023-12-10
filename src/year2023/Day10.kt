package year2023

import utils.*
import java.awt.Point
import java.util.Stack
import kotlin.math.abs
import kotlin.math.min

fun main() {
    data class P(
        val x: Int,
        val y: Int,
        val c: Char,
        val canLeft: Boolean,
        val canRight: Boolean,
        val canDown: Boolean,
        val canUp: Boolean,
    ) : Point(x, y)

    val upDown = '|'
    val leftRight = '-'
    val upRight = 'L'
    val upLeft = 'J'
    val downRight = 'F'
    val downLeft = '7'
    val none = '.'
    val startSpot = 'S'

    val chars = Input.get(2023, 10).mapIndexed { y, line ->
        line.toCharArray().mapIndexed { x, it ->
            when (it) {
                startSpot -> P(x, y, startSpot, canLeft = true, canRight = true, canDown = true, canUp = true)
                none -> P(x, y, none, canLeft = false, canRight = false, canDown = false, canUp = false)
                upDown -> P(x, y, upDown, canLeft = false, canRight = false, canDown = true, canUp = true)
                upLeft -> P(x, y, upLeft, canLeft = true, canRight = false, canDown = false, canUp = true)
                upRight -> P(x, y, upRight, canLeft = false, canRight = true, canDown = false, canUp = true)
                downLeft -> P(x, y, downLeft, canLeft = true, canRight = false, canDown = true, canUp = false)
                downRight -> P(x, y, downRight, canLeft = false, canRight = true, canDown = true, canUp = false)
                leftRight -> P(x, y, leftRight, canLeft = true, canRight = true, canDown = false, canUp = false)
                else -> throw Exception()
            }
        }
    }

    fun findStart(): Point {
        for (y in chars.indices) {
            for (x in chars[y].indices) {
                if (chars[y][x].c == startSpot) {
                    return Point(x, y)
                }
            }
        }
        throw Exception()
    }

    fun findLoop(): MutableList<P> {
        val at = findStart()
        val path = mutableListOf<P>()
        val seen = mutableSetOf<P>()
        seen.add(chars[at.y][at.x])
        path.add(chars[at.y][at.x])
        while (true) {
            val curr = chars[at.y][at.x]
            var next = chars[at.y][at.x - 1]
            if (!seen.contains(next) && curr.canLeft && next.canRight) {
                seen.add(next)
                path.add(next)
                at.x--
                continue
            }
            next = chars[at.y][at.x + 1]
            if (!seen.contains(next) && curr.canRight && next.canLeft) {
                seen.add(next)
                path.add(next)
                at.x++
                continue
            }
            next = chars[at.y + 1][at.x]
            if (!seen.contains(next) && curr.canDown && next.canUp) {
                seen.add(next)
                path.add(next)
                at.y++
                continue
            }
            next = chars[at.y - 1][at.x]
            if (!seen.contains(next) && curr.canUp && next.canDown) {
                seen.add(next)
                path.add(next)
                at.y--
                continue
            }

            return path
        }
    }

    fun part1() {
        val loop = findLoop()
        println(loop.size / 2)
    }

    fun part2() {
        /**
         * TL;DR
         * rather than being smart, be lazy,
         * double number of rows and columns, then make all even row/column be marked as "pipe-gap"
         * then put in the pipe we found in prior challenge, (just 2* the cords) and add pipe tile in the connector-pipe-gap between each pair of piping
         * from this we can just do normal bfs, where we start with the frontier on all edge-pieces (cheezed it based on my data, your pipe may touch the edge)
         * Finally just count number of tiles still unknown, ez pz lmn sqz
         */
        val isUnknown = 0
        val isPipe = 1
        val isPipeGap = 8
        val isOutside = 2

        fun toFake(p: Point): Point = Point(p.x * 2 + 1, p.y * 2 + 1)

        val lookup = mutableListOf<MutableList<Int>>()
        for (i in 0..<2 * chars.size) {
            val l = mutableListOf<Int>()
            lookup.add(l)
            for (j in 0..<2 * chars[0].size) l.add(if (i % 2 == 0 || j % 2 == 0) isPipeGap else isUnknown)
        }
        val loop = findLoop()
        loop.forEachIndexed { i, it ->
            val prev = toFake(if (i - 1 < 0) loop.last() else loop[i - 1])
            val curr = toFake(it)
            lookup[prev.y][prev.x] = isPipe
            lookup[curr.y][curr.x] = isPipe
            val gap =
                Point(min(curr.x, prev.x) + abs(curr.x - prev.x) / 2, min(curr.y, prev.y) + abs(curr.y - prev.y) / 2)
            lookup[gap.y][gap.x] = isPipe
        }

        for (i in 0..lookup[0].lastIndex) {
            lookup[0][i] = isOutside
            lookup[lookup.lastIndex][i] = isOutside
        }
        for (i in 0..lookup.lastIndex) {
            lookup[i][0] = isOutside
            lookup[i][lookup[0].lastIndex] = isOutside
        }

        val queue = Stack<Point>()
        for (y in lookup.indices) {
            for (x in lookup[0].indices) {
                if (lookup[y][x] == isOutside)
                    queue.add(Point(x, y))
            }
        }

        val dirs = listOf(
            Point(1, 0),
            Point(1, 1),
            Point(1, -1),
            Point(-1, 0),
            Point(-1, -1),
            Point(-1, 1),
            Point(0, 1),
            Point(0, -1)
        )
        while (queue.isNotEmpty()) {
            val at = queue.pop()
            dirs.forEach { d ->
                val p = Point(at.x + d.x, at.y + d.y)
                if (p.x >= 0 && p.x <= lookup[0].lastIndex && p.y >= 0 && p.y <= lookup.lastIndex) {
                    val point = lookup[p.y][p.x]
                    if (point == isUnknown || point == isPipeGap) {
                        lookup[p.y][p.x] = isOutside
                        queue.add(p)
                    }
                }
            }
        }

        println(lookup.joinToString(separator = "\n") { it.joinToString(separator = "") })
        println( lookup.sumOf { it.count { it == isUnknown } })
    }
    part1()
    part2()
}