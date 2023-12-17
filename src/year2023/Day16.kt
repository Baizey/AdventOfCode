package year2023

import utils.Input
import year2023.Direction.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.max


const val ANSI_BLACK_BACKGROUND = "\u001B[40m"
const val ANSI_RED_BACKGROUND = "\u001B[41m"
const val ANSI_GREEN_BACKGROUND = "\u001B[42m"
const val ANSI_YELLOW_BACKGROUND = "\u001B[43m"
const val ANSI_BLUE_BACKGROUND = "\u001B[44m"
const val ANSI_PURPLE_BACKGROUND = "\u001B[45m"
const val ANSI_CYAN_BACKGROUND = "\u001B[46m"
const val ANSI_WHITE_BACKGROUND = "\u001B[47m"
const val ANSI_RESET = "\u001B[0m"
const val ANSI_BLACK = "\u001B[30m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"
const val ANSI_YELLOW = "\u001B[33m"
const val ANSI_BLUE = "\u001B[34m"
const val ANSI_PURPLE = "\u001B[35m"
const val ANSI_CYAN = "\u001B[36m"
const val ANSI_WHITE = "\u001B[37m"

val emptySpace = '.'
val mirrorLeft = '/'
val mirrorRight = '\\'
val splitterHorizontal = '-'
val splitterVertical = '|'

enum class Direction {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    private fun turnLeft() = when (this) {
        NORTH -> WEST
        WEST -> SOUTH
        SOUTH -> EAST
        EAST -> NORTH
    }

    private fun turnRight() = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }

    fun turn(c: Char): List<Direction> = when (c) {
        emptySpace -> listOf(this)
        splitterHorizontal -> if (this == NORTH || this == SOUTH) listOf(
            turnRight(),
            turnLeft()
        ) else listOf(this)

        splitterVertical -> if (this == EAST || this == WEST) listOf(
            turnRight(),
            turnLeft()
        ) else listOf(this)

        // \
        mirrorRight -> listOf(
            when (this) {
                NORTH -> turnLeft()
                EAST -> turnRight()
                SOUTH -> turnLeft()
                WEST -> turnRight()
            }
        )

        // /
        mirrorLeft -> listOf(
            when (this) {
                NORTH -> turnRight()
                EAST -> turnLeft()
                SOUTH -> turnRight()
                WEST -> turnLeft()
            }
        )

        else -> throw Error()
    }
}

fun main() {
    val lines = Input.get(2023, 16)
    //.map { it.flatMap { listOf(it, '.') }.joinToString(separator = "") }
    //.flatMap { listOf(it, ".".repeat(it.length)) }

    data class Mover(var x: Int, var y: Int, val direction: Direction) {
        fun move() {
            when (direction) {
                NORTH -> y--
                SOUTH -> y++
                WEST -> x--
                EAST -> x++
            }
        }

        fun inBound(maxX: Int, maxY: Int) = x in 0..<maxX && y in 0..<maxY
    }

    fun solve(start: Mover): Long {
        val seen = HashMap<Int, HashMap<Int, HashSet<Direction>>>()
        val energized = HashMap<Int, HashSet<Int>>()
        val width = lines.first().length
        val height = lines.size

        println(lines.mapIndexed { y, line ->
            line.mapIndexed { x, c -> c }.joinToString(separator = "")
        }.joinToString(separator = "\n"))
        println()

        val queue = Stack<Mover>()
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
            mover.move()
            if (!mover.inBound(width, height)) continue
            mover.direction.turn(lines[mover.y][mover.x]).map { Mover(mover.x, mover.y, it) }.forEach { queue.add(it) }
        }

        return energized.values.sumOf { it.size.toLong() } - 1L
    }

    fun part1() {
        println(solve(Mover(-1, 0, EAST)))
    }

    fun part2() {
        var best = 0L
        for (i in lines.indices) best = max(best, solve(Mover(-1, i, EAST)))
        for (i in lines.indices) best = max(best, solve(Mover(lines[0].length, i, WEST)))
        for (i in 0..<lines[0].length) best = max(best, solve(Mover(i, lines.size, NORTH)))
        for (i in 0..<lines[0].length) best = max(best, solve(Mover(i, i - 1, SOUTH)))
        println(best)
    }
    part1()
    part2()
}