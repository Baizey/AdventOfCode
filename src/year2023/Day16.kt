package year2023

import utils.GridNavigator
import utils.Direction
import utils.Direction.*
import utils.Input
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.max

const val emptySpace = '.'
const val mirrorLeft = '/'
const val mirrorRight = '\\'
const val splitterHorizontal = '-'
const val splitterVertical = '|'


fun main() {
    fun Direction.turn(c: Char): List<Direction> = when (c) {
        emptySpace -> listOf(this)
        splitterHorizontal -> if (this == north || this == south) listOf(
            turnRight(),
            turnLeft()
        ) else listOf(this)

        splitterVertical -> if (this == west || this == east) listOf(
            turnRight(),
            turnLeft()
        ) else listOf(this)

        // \
        mirrorRight -> listOf(
            when (this) {
                north -> turnLeft()
                east -> turnRight()
                south -> turnLeft()
                west -> turnRight()
                unknown -> throw Error()
            }
        )

        // /
        mirrorLeft -> listOf(
            when (this) {
                north -> turnRight()
                east -> turnLeft()
                south -> turnRight()
                west -> turnLeft()
                unknown -> throw Error()
            }
        )

        else -> throw Error()
    }

    val lines = Input.get(2023, 16)

    fun solve(start: GridNavigator): Long {
        val seen = HashMap<Int, HashMap<Int, HashSet<Direction>>>()
        val energized = HashMap<Int, HashSet<Int>>()
        val width = lines.first().length
        val height = lines.size

        println(lines.mapIndexed { y, line ->
            line.mapIndexed { x, c -> c }.joinToString(separator = "")
        }.joinToString(separator = "\n"))
        println()

        val queue = Stack<GridNavigator>()
        queue.add(start)

        while (queue.isNotEmpty()) {
            val mover = queue.pop()

            val mapX = seen.computeIfAbsent(mover.x) { HashMap() }
            val mapY = mapX.computeIfAbsent(mover.y) { HashSet() }
            if (mapY.contains(mover.direction)) continue
            mapY.add(mover.direction)

            energized.compute(mover.x) { _, v ->
                val set = v ?: HashSet()
                set.add(mover.y)
                set
            }
            mover.moveForward()
            if (!mover.isInBound(width, height)) continue
            mover.direction.turn(lines[mover.y][mover.x]).map { GridNavigator(mover.x, mover.y, it) }.forEach { queue.add(it) }
        }

        return energized.values.sumOf { it.size.toLong() } - 1L
    }

    fun part1() {
        println(solve(GridNavigator(-1, 0, east)))
    }

    fun part2() {
        var best = 0L
        for (i in lines.indices) best = max(best, solve(GridNavigator(-1, i, east)))
        for (i in lines.indices) best = max(best, solve(GridNavigator(lines[0].length, i, west)))
        for (i in 0..<lines[0].length) best = max(best, solve(GridNavigator(i, lines.size, north)))
        for (i in 0..<lines[0].length) best = max(best, solve(GridNavigator(i, i - 1, south)))
        println(best)
    }
    part1()
    part2()
}