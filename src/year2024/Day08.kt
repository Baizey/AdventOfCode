package year2024

import utils.grid.Nav
import utils.Helpers.groupMatches
import utils.Helpers.println
import utils.Input

fun main() {
    val input = Input.get(2024, 8).asCharGrid()
    val lookup = input.groupMatches { it != '.' }

    fun part1() {
        lookup.values
            .map { list ->
                val nodes = mutableSetOf<Long>()
                for (i in 0..list.lastIndex) {
                    val a = list[i]
                    for (j in (i + 1)..list.lastIndex) {
                        val b = list[j]
                        val deltaX = a.x - b.x
                        val deltaY = a.y - b.y

                        val dead1 = Nav(b.x - deltaX, b.y - deltaY)
                        val dead2 = Nav(b.x + 2 * deltaX, b.y + 2 * deltaY)
                        if (dead1.isInBound(input)) nodes.add(dead1.hash())
                        if (dead2.isInBound(input)) nodes.add(dead2.hash())
                    }
                }
                nodes
            }
            .flatten()
            .distinct()
            .count()
            .println()
    }

    fun part2() {
        lookup.values
            .map { list ->
                val nodes = mutableSetOf<Long>()
                for (i in 0..list.lastIndex) {
                    val a = list[i]
                    for (j in (i + 1)..list.lastIndex) {
                        val b = list[j]
                        val deltaX = a.x - b.x
                        val deltaY = a.y - b.y

                        val at = b.clone()
                        while (at.isInBound(input)) {
                            at.x -= deltaX
                            at.y -= deltaY
                        }
                        at.x += deltaX
                        at.y += deltaY
                        while (at.isInBound(input)) {
                            nodes.add(at.hash())
                            at.x += deltaX
                            at.y += deltaY
                        }

                    }
                }
                nodes
            }
            .flatten()
            .distinct()
            .count()
            .println()
    }
    part1()
    part2()
}