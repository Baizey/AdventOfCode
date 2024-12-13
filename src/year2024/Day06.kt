package year2024

import utils.Direction.north
import utils.GridNavigator
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
            grid[at.y.toInt()][at.x.toInt()] = 'X'
            val c = at.clone().moveForward()
            if (c.isNotInBound(grid)) break
            if (c.valueOf(grid) == '#') at.turnRight()
            at.moveForward()
        }
        grid.flatten().count { it == 'X' }.also(::println)
    }

    fun isLoop(start: GridNavigator, grid: List<List<Char>>): Boolean {
        val seen = mutableSetOf<Long>()
        val at = start.clone()
        while (true) {
            val c = at.clone().moveForward()
            if (c.isNotInBound(grid)) break
            if (c.valueOf(grid) == '#') at.turnRight()
            else {
                at.moveForward()
                if (seen.contains(at.hashWithDir())) return true
                seen.add(at.hashWithDir())
            }
        }
        return false
    }

    fun part2() {
        val at = start.clone()
        val grid = originalGrid.clone()
        var count = 0
        while (true) {
            val c = at.setValue('X', grid).clone().moveForward()
            when {
                c.isNotInBound(grid) -> break
                c.valueOf(grid) == '#' -> at.turnRight()
                c.valueOf(grid) != 'X' -> {
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