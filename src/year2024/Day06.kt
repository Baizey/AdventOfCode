package year2024

import utils.Direction.north
import utils.GridNavigator
import utils.Input

fun main() {
    val originalGrid = Input.get(2024, 6).asCharGrid()
    val start = GridNavigator.find('^', originalGrid)
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

    fun isLoop(start: GridNavigator): Boolean {
        val seen = mutableSetOf<Int>()
        val at = start.clone()
        while (true) {
            val c = at.clone().moveForward()
            if (c.isNotInBound(originalGrid)) break
            if (c.valueOf(originalGrid) == '#') at.turnRight()
            else {
                at.moveForward()
                if (seen.contains(at.hashCode())) return true
                seen.add(at.hashCode())
            }
        }
        return false
    }

    fun part2() {
        val at = start.clone()
        var count = 0
        while (true) {
            val c = at.setValue('X', originalGrid).clone().moveForward()
            when {
                c.isNotInBound(originalGrid) -> break
                c.valueOf(originalGrid) == '#' -> at.turnRight()
                c.valueOf(originalGrid) != 'X' -> {
                    c.setValue('#', originalGrid)
                    if (isLoop(at)) count++
                    c.setValue(' ', originalGrid)
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