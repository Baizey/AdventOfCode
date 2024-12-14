package year2024

import utils.*
import utils.Helpers.println

fun main() {
    val input = Input.get(2024, 14).asLongLines()

    data class Robot(val at: GridNavigator, val velocity: GridNavigator)

    fun display(maxX: Int, maxY: Int, robots: List<Robot>) {
        val grid = MutableList(maxY) { MutableList(maxX) { 0 } }
        robots.forEach { it.at.setValue(it.at.valueOf(grid) + 1, grid) }
        grid[maxY / 2] = MutableList(maxX) { -1 }
        grid.forEach { it[maxX / 2] = -1 }
        grid.joinToString(separator = "\n") {
            it.map {
                when (it) {
                    0 -> '.'
                    -1 -> ' '
                    else -> Integer.toString(it)
                }
            }.joinToString(separator = "")
        }.println()
    }

    fun part1() {
        val robots = input.map { Robot(GridNavigator(it[0], it[1]), GridNavigator(it[2], it[3])) }
        val maxX = 101
        val maxY = 103
        robots.forEach { robot ->
            for (t in 0..<100) {
                robot.at.x += robot.velocity.x
                robot.at.y += robot.velocity.y
            }
            while (robot.at.x < 0) robot.at.x += maxX
            while (robot.at.y < 0) robot.at.y += maxY
            robot.at.x %= maxX
            robot.at.y %= maxY
        }

        val groups = robots.groupBy {
            val x = it.at.x
            val y = it.at.y
            if (x == maxX / 2L || y == maxY / 2L) {
                Direction.unknown
            } else if (x < maxX / 2) {
                if (y < maxY / 2) {
                    Direction.northwest
                } else {
                    Direction.southwest
                }
            } else {
                if (y < maxY / 2) {
                    Direction.northeast
                } else {
                    Direction.southeast
                }
            }
        }.toMutableMap()
        groups.remove(Direction.unknown)
        groups.values.map { it.size }.reduce { a, b -> a * b }.println()

    }

    fun part2() {
        val robots = input.map { Robot(GridNavigator(it[0], it[1]), GridNavigator(it[2], it[3])) }
        val maxX = 101
        val maxY = 103
        var time = 0L
        var timestep = 1L
        while (true) {
            time += timestep
            robots.forEach { robot ->
                for (t in 0..<timestep) {
                    robot.at.x += robot.velocity.x
                    robot.at.y += robot.velocity.y
                }
                while (robot.at.x < 0) robot.at.x += maxX
                while (robot.at.y < 0) robot.at.y += maxY
                robot.at.x %= maxX
                robot.at.y %= maxY
            }
            val lookup = robots.map { it.at.hash() }.toSet()
            if (robots.any { r -> r.at.allDirs().all { lookup.contains(it.hash()) } }) {
                display(maxX, maxY, robots)
                println("Seconds: $time")
                break
            }
        }
    }
    part1()
    part2()
}