package year2023

import utils.*

fun main() {
    val roundedRock = 'O'
    val cubeRock = '#'
    val emptySpace = '.'
    val lines = Input.get(2023, 14).map { it.toCharArray() }.toTypedArray()

    fun tiltNorth() {
        for (x in lines[0].indices) {
            var currentPos = 0
            for (y in lines.indices) {
                when (lines[y][x]) {
                    roundedRock -> {
                        lines[y][x] = emptySpace
                        lines[currentPos][x] = roundedRock
                        currentPos++
                    }

                    cubeRock -> {
                        currentPos = y + 1
                    }
                }
            }
        }
    }

    fun tiltSouth() {
        for (x in lines[0].indices) {
            var currentPos = lines.lastIndex
            for (y in lines.lastIndex downTo 0) {
                when (lines[y][x]) {
                    roundedRock -> {
                        lines[y][x] = emptySpace
                        lines[currentPos][x] = roundedRock
                        currentPos--
                    }

                    cubeRock -> {
                        currentPos = y - 1
                    }
                }
            }
        }
    }

    fun tiltWest() {
        for (y in lines.indices) {
            var currentPos = 0
            for (x in lines[0].indices) {
                when (lines[y][x]) {
                    roundedRock -> {
                        lines[y][x] = emptySpace
                        lines[y][currentPos] = roundedRock
                        currentPos++
                    }

                    cubeRock -> {
                        currentPos = x + 1
                    }

                }
            }
        }
    }

    fun tiltEast() {
        for (y in lines.indices) {
            var currentPos = lines[0].lastIndex
            for (x in lines[0].lastIndex downTo 0) {
                when (lines[y][x]) {
                    roundedRock -> {
                        lines[y][x] = emptySpace
                        lines[y][currentPos] = roundedRock
                        currentPos--
                    }

                    cubeRock -> {
                        currentPos = x - 1
                    }

                }
            }
        }
    }

    fun runCycle() {
        tiltNorth()
        tiltWest()
        tiltSouth()
        tiltEast()
    }

    fun calculateNorthLoad(): Long {
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
        return sum
    }

    fun calculateLoad(): Long {
        var sum = 0L
        for (x in lines[0].indices) {
            for (y in lines.indices) {
                when (lines[y][x]) {
                    roundedRock -> {
                        sum += lines.size - y
                    }
                }
            }
        }
        return sum
    }

    fun part1() {
        println(calculateNorthLoad())
    }

    fun part2() {
        val prime = 31L
        val seen = HashMap<Long, Long>()
        var i = 0L
        val cycles = 1_000_000_000L
        while (i < cycles) {
            runCycle()
            var hash = 17L
            for (y in lines.indices) {
                for (x in lines[0].indices) {
                    hash = hash * prime + (lines[y][x].code)
                    hash = hash * prime + x
                    hash = hash * prime + y
                }
            }

            if (seen.contains(hash)) {
                val cycleSize = i - seen[hash]!!
                // yarp, this could just be some quick division work, but meh fast enough is fast enough
                while (i + cycleSize < cycles) i += cycleSize
                while (++i < cycles) {
                    runCycle()
                }
                println(calculateLoad())
                return
            } else {
                seen[hash] = i
                i++
            }
        }
    }

    part1()
    part2()
}

