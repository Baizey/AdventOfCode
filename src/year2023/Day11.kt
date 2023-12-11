package year2023

import utils.Input
import java.awt.Point
import kotlin.math.abs

fun main() {
    val input = Input.get(2023, 11)


    fun part1() {
        fun dist(a: Point, b: Point) = abs(a.x - b.x) + abs(a.y - b.y)

        val extendedY = input.flatMap { line -> if (line.contains('#')) listOf(line) else listOf(line, line) }

        val extend = mutableListOf<Int>()
        for (column in extendedY[0].indices) {
            var hasStar = false
            for (row in extendedY.indices) {
                if (extendedY[row][column] == '#') {
                    hasStar = true
                    break
                }
            }
            if (!hasStar) extend.add(column)
        }

        val extendedXY = extendedY.map { it.mapIndexed { i, c -> if (i in extend) listOf(c, c) else listOf(c) }.flatten() }

        val galaxies = mutableListOf<Point>()
        extendedXY.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') galaxies.add(Point(x, y))
            }
        }

        var sum = 0L
        for (i in 0..<galaxies.size) {
            for (j in i + 1..<galaxies.size) {
                sum += dist(galaxies[i], galaxies[j])
            }
        }

        println(sum)
    }


    fun part2() {
        val extendedY = mutableSetOf<Int>()
        input.forEachIndexed { y, line -> if (!line.contains('#')) extendedY.add(y) }

        val extendedX = mutableSetOf<Int>()
        for (column in input[0].indices) {
            var hasStar = false
            for (row in input.indices) {
                if (input[row][column] == '#') {
                    hasStar = true
                    break
                }
            }
            if (!hasStar) extendedX.add(column)
        }


        val galaxies = mutableListOf<Point>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') galaxies.add(Point(x, y))
            }
        }

        // This is horrible... but its less horrible than brute forcing the true size of the universe... kind of
        fun dist(a: Point, b: Point): Long {
            val at = Point(a.x, a.y)
            var sum = 0L

            val diffX = if (b.x > at.x) 1 else -1
            while (at.x != b.x) {
                at.x += diffX
                if (at.x in extendedX) sum += 1_000_000L
                else sum += 1
            }

            val diffY = if (b.y > at.y) 1 else -1
            while (at.y != b.y) {
                at.y += diffY
                if (at.y in extendedY) sum += 1_000_000L
                else sum += 1
            }
            return sum
        }

        var sum = 0L
        for (i in 0..<galaxies.size) {
            for (j in i + 1..<galaxies.size) {
                sum += dist(galaxies[i], galaxies[j])
            }
        }

        println(sum)
    }

    part1()
    part2()
}