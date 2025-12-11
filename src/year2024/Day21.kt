package year2024

import utils.*
import utils.Helpers.findExact
import utils.Helpers.println
import utils.grid.Nav
import kotlin.math.abs

fun main() {
    val input = Input.get(2024, 21).asWordLines().map { it.first() }
    val nums = listOf(
        listOf('7', '8', '9'),
        listOf('4', '5', '6'),
        listOf('1', '2', '3'),
        listOf('#', '0', 'A'),
    )
    val arrows = listOf(
        listOf('#', '^', 'A'),
        listOf('<', 'v', '>'),
    )

    fun goto(from: Nav, to: Nav, grid: List<List<Char>>): String {
        val result = StringBuilder()
        if (from.xyIntersects(to).any { it.valueOrNull(grid) == '#' }) {
            if (to.x > from.x) result.append(">".repeat(abs(to.x - from.x).toInt()))
            if (to.y < from.y) result.append("^".repeat(abs(to.y - from.y).toInt()))
            if (to.y > from.y) result.append("v".repeat(abs(to.y - from.y).toInt()))
            if (to.x < from.x) result.append("<".repeat(abs(to.x - from.x).toInt()))
        } else {
            if (to.x < from.x) result.append("<".repeat(abs(to.x - from.x).toInt()))
            if (to.y > from.y) result.append("v".repeat(abs(to.y - from.y).toInt()))
            if (to.x > from.x) result.append(">".repeat(abs(to.x - from.x).toInt()))
            if (to.y < from.y) result.append("^".repeat(abs(to.y - from.y).toInt()))
        }
        result.append("A")
        return result.toString()
    }

    val cacheData = mutableMapOf<Long, MutableMap<Long, MutableMap<Char, Long>>>()

    fun unfold(
        from: Nav,
        c: Char,
        depth: Long,
    ): Long {
        if (depth <= 0L) return 0L
        val cache = cacheData.computeIfAbsent(depth) { mutableMapOf() }
            .computeIfAbsent(from.hash()) { mutableMapOf() }
        if (cache.containsKey(c)) return cache[c]!!
        return goto(from, arrows.findExact { it == c }!!, arrows)
            .sumOf { unfold(from, it, depth - 1L) }
            .also { cache[c] = it }
    }

    fun part1() {
        input.map { code ->
            val selfAt = arrows.findExact { it == 'A' }!!
            var numAt = nums.findExact { it == 'A' }!!

            val result = code.map { c ->
                val newNum = nums.findExact { it == c }!!
                val numPath = goto(numAt, newNum, nums)
                numAt = newNum
                numPath.map { unfold(selfAt, it, 1L) }
            }.joinToString(separator = "")

            val length = result.length.toLong()
            val inputCode = InputData(listOf(code)).asLongLines().first().first()
            length to inputCode
        }.sumOf { it.first * it.second }.println()
    }

    fun part2() {
        input.map { code ->
            val selfAt = arrows.findExact { it == 'A' }!!

            var numAt = nums.findExact { it == 'A' }!!
            val length = code.map { c ->
                val newNum = nums.findExact { it == c }!!
                val numPath = goto(numAt, newNum, nums)
                numAt = newNum
                numPath.map { unfold(selfAt, it, 25L) }.sum()
            }.sum()

            val inputCode = InputData(listOf(code)).asLongLines().first().first()
            length to inputCode
        }.sumOf { it.first * it.second }.println()
    }
    part1()
    part2()
}