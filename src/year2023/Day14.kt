package year2023

import utils.*

fun main() {
    val roundedRock = 'O'
    val cubeRock = '#'
    val emptySpace = '.'
    val lines = Input.get(2023, 14)

    fun part1() {
        var sum = 0L
        for (x in lines[0].indices) {
            var currentLoad = lines.size
            for (y in lines.indices) {
                when (lines[y][x]) {
                    roundedRock -> {
                        sum += currentLoad
                        currentLoad--
                    }

                    cubeRock -> {
                        currentLoad = lines.size - y - 1
                    }
                }
            }
        }
        println(sum)
    }

    fun part2() {

    }

    part1()
    part2()
}

