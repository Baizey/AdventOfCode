package year2015

import utils.*
import kotlin.math.min

fun main() {
    val lines = Input.get(2015, 2).asLines()

    fun part1() {
        var total = 0L
        lines.forEach { line ->
            val sides = line.split("x").map { it.toLong() }
            val l = sides[0]
            val w = sides[1]
            val h = sides[2]
            val a = l * w
            val b = w * h
            val c = h * l
            total += 2 * (a + b + c) + listOf(a, b, c).min()
        }
        println(total)
    }

    fun part2() {
        var total = 0L
        lines.forEach { line ->
            val sides = line.split("x").map { it.toLong() }
            val bow = sides.reduce { acc, v -> acc * v }
            val sorted = sides.sorted()
            val perimeter = 2 * sorted[0] + 2 * sorted[1]
            total += bow + perimeter
        }
        println(total)
    }

    part1()
    part2()
}