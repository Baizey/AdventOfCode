package year2023

import utils.*
import java.math.BigInteger

fun main() {
    data class Instruction(val id: String, val left: String, val right: String)
    data class Temp(val instruction: String, val index: Int)
    data class Looper(val steps: List<Long>, val restartFrom: Int)

    val lines = Input.get(2023, 8).asLines()
    val firstLine = lines.first().toCharArray()

    val instructions = lines.drop(2).map {
        val id = it.split(" = (").first()
        val part = it.split(" = (")[1].trim().removeSurrounding("", ")").split(", ")
        Instruction(id, part[0], part[1])
    }.associateBy { it.id }

    fun part1() {
        var at = instructions["AAA"]
        var count = 0
        while (at?.id != "ZZZ") {
            for (i in firstLine) {
                if (at?.id === "ZZZ") break
                count++
                val moving = if (i == 'L') at?.left else at?.right
                at = instructions[moving]
            }
        }
        println(count)
    }

    fun walk(from: String): Looper {
        var at = instructions[from]!!
        val path = mutableListOf<Temp>()
        val seen = mutableSetOf<Temp>()
        val steps = mutableListOf<Long>()
        var s = 0L
        println("---")
        while (true) {
            for (i in firstLine.indices) {
                val moving = if (firstLine[i] == 'L') at.left else at.right
                at = instructions[moving]!!
                s++
                val step = Temp(at.id, i)
                if (at.id.endsWith('Z')) {
                    if (!seen.contains(step)) {
                        steps.add(s)
                        s = 0
                        seen.add(step)
                        path.add(step)
                    } else {
                        steps.add(s)
                        val loopFrom = path.indexOf(step)
                        return Looper(steps, loopFrom + 1)
                    }
                }
            }
        }
    }

    fun part2() {
        val nodes = instructions.values.filter { it.id.endsWith('A') }
        val results = nodes.map { walk(it.id) }.map { it.steps.first() }

        fun gcd(a: Long, b: Long): Long {
            return if (b == 0L) a else gcd(b, a % b)
        }

        fun lcm(a: Long, b: Long): Long {
            return a * (b / gcd(a, b))
        }

        fun findSyncTime(cycles: List<Long>): Long {
            return cycles.reduce { a, b -> lcm(a, b) }
        }

        println(findSyncTime(results))
    }

    part1()
    part2()
}