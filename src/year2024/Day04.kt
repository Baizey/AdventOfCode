package year2024

import utils.GridNavigator
import utils.Input

fun main() {
    val grid = Input.get(2024, 4).map { it.toCharArray() }

    val letters = arrayOf('M', 'A', 'S')
    fun walk(at: GridNavigator, maxX: Int, maxY: Int): Boolean {
        for (letter in letters) {
            if (at.isNotInBound(maxX, maxY))
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
        for (y in 0..<maxY) {
            for (x in 0..<maxX) {
                if (grid[y][x] == 'X')
                    for (d in GridNavigator(y, x).allDirs())
                        if (walk(d, maxX, maxY))
                            result++
            }
        }
        println(result)
    }

    fun part2() {
        var result = 0L
        val maxY = grid.size
        val maxX = grid[0].size

        for (y in 0..<maxY) {
            for (x in 0..<maxX) {
                val at = GridNavigator(y, x)
                if (at.valueOf(grid) != 'A') continue
                val dirs = at.diagonalDirs()
                if (dirs.any { it.isNotInBound(maxX, maxY) }) continue
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