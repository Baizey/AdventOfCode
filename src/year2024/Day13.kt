package year2024

import utils.*
import utils.Helpers.println
import utils.grid.Nav

fun main() {
    val input = Input.get(2024, 13).asLongLines()

    data class Data(val a: Nav, val b: Nav, val c: Nav)

    val data = mutableListOf<Data>()
    for (i in 0..input.lastIndex step 4) data.add(
        Data(
            Nav(input[i + 0][0], input[i + 0][1]),
            Nav(input[i + 1][0], input[i + 1][1]),
            Nav(input[i + 2][0], input[i + 2][1])
        )
    )

    fun calc(data: Data): Long {
        val (a, b, c) = data

        val lhs = a.x * b.y - a.y * b.x
        val rhs = c.x * b.y - c.y * b.x
        if (rhs % lhs != 0L) return 0L
        val na = rhs / lhs

        val rhs2 = c.x - na * a.x
        if (rhs2 % b.x != 0L) return 0L
        val nb = rhs2 / b.x

        return na * 3L + nb
    }

    fun part1() {
        data.sumOf(::calc)
            .println()
    }

    fun part2() {
        data.map { it.c.x += 10000000000000L; it.c.y += 10000000000000L; it }
            .sumOf(::calc)
            .println()

    }

    part1()
    part2()
}