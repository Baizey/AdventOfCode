package year2024

import utils.grid.Nav
import utils.Helpers.findMatches
import utils.Helpers.println
import utils.Input

fun main() {
    val grid = Input.get(2024, 10).asDigitGrid()

    fun part1() {
        grid.findMatches { it == 0 }
            .map { start ->
                val queue = ArrayDeque<Nav>()
                queue.addLast(start)
                val tops = mutableSetOf<Long>()
                while (queue.isNotEmpty()) {
                    val current = queue.removeFirst()
                    if (current.value(grid) == 9) {
                        tops.add(current.hash())
                        continue
                    }
                    val next = current.value(grid) + 1
                    current.xyDirs()
                        .filter { it.isInBound(grid) }
                        .filter { it.value(grid) == next }
                        .forEach { queue.addLast(it) }
                }
                tops.size
            }
            .sumOf { it }
            .println()
    }

    fun part2() {
        grid.findMatches { it == 0 }
            .map { start ->
                val queue = ArrayDeque<Nav>()
                queue.addLast(start)
                var tops = 0L
                while (queue.isNotEmpty()) {
                    val current = queue.removeFirst()
                    if (current.value(grid) == 9) {
                        tops++
                        continue
                    }
                    val next = current.value(grid) + 1
                    current.xyDirs()
                        .filter { it.isInBound(grid) }
                        .filter { it.value(grid) == next }
                        .forEach { queue.addLast(it) }
                }
                tops
            }
            .sumOf { it }
            .println()
    }
    part1()
    part2()
}