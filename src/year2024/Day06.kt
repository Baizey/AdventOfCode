package year2024

import utils.grid.Direction.north
import utils.grid.Nav
import utils.Helpers.clone
import utils.Helpers.findExact
import utils.Input

fun main() {
    val originalGrid = Input.get(2024, 6).asCharGrid()
    val start = originalGrid.findExact { it == '^' }!!.turn(north)
    start.turn(north)

    fun part1() {
        val grid = originalGrid.map { line -> line.map { it }.toMutableList() }
        val at = start.clone()
        while (true) {
            val c = at.setValue('X', grid).valueOrNull(grid, steps = 1)
            when (c) {
                null -> break
                '#' -> at.turnRight()
                else -> at.moveForward()
            }
        }
        grid.flatten().count { it == 'X' }.also(::println)
    }

    fun isLoop(start: Nav, grid: List<List<Char>>): Boolean {
        val seen = mutableSetOf<Long>()
        val at = start.clone()
        while (true) {
            when (at.valueOrNull(grid, steps = 1)) {
                null -> return false
                '#' -> at.turnRight()
                else -> at.moveForward()
            }
            if (seen.contains(at.hashWithDir())) return true
            seen.add(at.hashWithDir())
        }
    }

    fun part2() {
        val at = start.clone()
        val grid = originalGrid.clone()
        var count = 0
        while (true) {
            val c = at.setValue('X', grid).clone().moveForward()
            when {
                c.isNotInBound(grid) -> break
                c.value(grid) == '#' -> at.turnRight()
                c.value(grid) != 'X' -> {
                    c.setValue('#', grid)
                    if (isLoop(at, grid)) count++
                    c.setValue(' ', grid)
                    at.moveForward()
                }

                else -> at.moveForward()
            }
        }
        println(count)
    }

    part1()
    part2()
}