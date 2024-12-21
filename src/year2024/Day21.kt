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
            else result.append("v".repeat(abs(to.y - from.y).toInt()))
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

    fun unFold(
        from: Nav,
        path: String,
        grid: List<List<Char>>,
    ): Pair<String, Nav> {
        val result = StringBuilder()
        var at = from.clone()
        for (c in path) {
            val goal = arrows.findExact { it == c }!!
            val arrowPath = goto(at, goal, grid)
            result.append(arrowPath)
            at = goal
        }
        return result.toString() to at
    }

    fun part1() {
        input.map { code ->
            var selfAt = arrows.findExact { it == 'A' }!!
            val bots = MutableList(1) { arrows.findExact { it == 'A' }!! }
            var numAt = nums.findExact { it == 'A' }!!

            val result = code.map { c ->
                val newNum = nums.findExact { it == c }!!
                val numPath = goto(numAt, newNum, nums)
                numAt = newNum

                var lastPath = numPath
                bots.forEachIndexed { i, bot ->
                    val (botPath, newBot) = unFold(bot, lastPath, arrows)
                    lastPath = botPath
                    bots[i] = newBot
                }

                val (selfPath, newSelf) = unFold(selfAt, lastPath, arrows)
                selfAt = newSelf

                selfPath
            }.joinToString(separator = "")

            val length = result.length.toLong()
            val inputCode = InputData(listOf(code)).asLongLines().first().first()
            length to inputCode
        }.sumOf { it.first * it.second }.println()
    }

    fun part2() {
        input.map { code ->
            var selfAt = arrows.findExact { it == 'A' }!!
            val bots = MutableList(25) { arrows.findExact { it == 'A' }!! }
            var numAt = nums.findExact { it == 'A' }!!

            val result = code.map { c ->
                val newNum = nums.findExact { it == c }!!
                val numPath = goto(numAt, newNum, nums)
                numAt = newNum

                var lastPath = numPath
                bots.forEachIndexed { i, bot ->
                    val (botPath, newBot) = unFold(bot, lastPath, arrows)
                    lastPath = botPath
                    bots[i] = newBot
                }

                val (selfPath, newSelf) = unFold(selfAt, lastPath, arrows)
                selfAt = newSelf

                selfPath
            }.joinToString(separator = "")

            val length = result.length.toLong()
            val inputCode = InputData(listOf(code)).asLongLines().first().first()
            length to inputCode
        }.sumOf { it.first * it.second }.println()
    }
    part1()
    part2()
}