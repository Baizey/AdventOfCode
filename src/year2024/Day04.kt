package year2024

import utils.GridNavigator
import utils.Input

fun main() {
    val grid = Input.get(2024, 4).asCharGrid()

    val letters = arrayOf('M', 'A', 'S')
    fun walk(at: GridNavigator): Boolean {
        for (letter in letters) {
            if (at.isNotInBound(grid))
                return false
            if (grid[at.x.toInt()][at.y.toInt()] != letter)
                return false
            at.moveForward()
        }
        return true
    }

    fun part1() {
        var result = 0L
        val maxY = grid.size
        val maxX = grid[0].size
        for (x in 0..<maxX) {
            for (y in 0..<maxY) {
                if (grid[y][x] == 'X')
                    for (d in GridNavigator(x, y).allDirs())
                        if (walk(d))
                            result++
            }
        }
        println(result)
    }

    fun part2() {
        var result = 0L
        val maxY = grid.size
        val maxX = grid[0].size

        for (x in 0..<maxX) {
            for (y in 0..<maxY) {
                val at = GridNavigator(x, y)
                if (at.valueOf(grid) != 'A') continue
                val dirs = at.diagonalDirs()
                if (dirs.any { it.isNotInBound(grid) }) continue
                val chars = dirs.map { it.valueOf(grid) }
                if (chars[0] == chars[2]) continue
                if (chars.count { it == 'M' } != 2) continue
                if (chars.count { it == 'S' } != 2) continue
                result++
            }
        }

        println(result)
    }
    part1()
    part2()
}