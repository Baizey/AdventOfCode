package year2025

import utils.Input
import utils.grid.NavBound
import utils.grid.NavBound.Companion.navBoundEach

fun main() {
    val input = Input.get(2025, 4).asCharGrid()
    val paper = '@'
    val empty = '.'
    val taken = 'x'

    fun part1(): Any = input.navBoundEach()
        .filter { it.value() == paper }
        .count { at -> at.allDirs().filter { it.isInBound() }.count { it.value() == paper }.let { it < 4 } }

    fun part2(): Any {
        val frontier = ArrayDeque<NavBound<Char>>()

        input.navBoundEach().forEach { at ->
            val count = at.allDirs().filter { it.isInBound() }.count { it.value() == paper }
            if (count < 4) frontier.add(at)
        }

        var result = 0
        while (frontier.isNotEmpty()) {
            val current = frontier.removeFirst()
            if (current.value() != paper) continue
            current.setValue(taken)
            result++
            current.allDirs().filter { it.isInBound() }.filter { it.value() == paper }.forEach { at ->
                val count = at.allDirs().filter { it.isInBound() }.count { it.value() == paper }
                if (count < 4) frontier.add(at)
            }
        }
        return result
    }

    println("Part 1: " + part1())
    println("Part 2: " + part2())
}