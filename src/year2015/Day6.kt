package year2015

import utils.*
import java.awt.Point
import java.util.HashMap
import kotlin.math.max


fun main() {
    val on = "turn on "
    val off = "turn off "
    val toggle = "toggle "

    data class Action(val type: String, val from: Point, val to: Point)

    val actions = Input.get(2015, 6).map { line ->
        val type = if (line.startsWith(on)) on else if (line.startsWith(off)) off else toggle
        val from = line.removePrefix(type).split("through").first().trim().split(",").map { it.toInt() }
        val to = line.removePrefix(type).split("through").last().trim().split(",").map { it.toInt() }
        Action(type, Point(from[0], from[1]), Point(to[0], to[1]))
    }

    fun part1() {
        val lights = HashMap<Int, HashMap<Int, Boolean>>()
        actions.forEach { action ->
            val xRange = action.from.x..action.to.x
            val yRange = action.from.y..action.to.y
            for (x in xRange) {
                for (y in yRange) {
                    val row = lights.getOrPut(x) { HashMap() }
                    when (action.type) {
                        on -> row[y] = true
                        off -> row[y] = false
                        toggle -> row[y] = !row.getOrDefault(y, false)
                    }
                }
            }
        }

        val result = lights.values.flatMap { it.values }.count { it }
        println(result)
    }

    fun part2() {
        val lights = HashMap<Int, HashMap<Int, Int>>()
        actions.forEach { action ->
            val xRange = action.from.x..action.to.x
            val yRange = action.from.y..action.to.y
            for (x in xRange) {
                for (y in yRange) {
                    val row = lights.getOrPut(x) { HashMap() }
                    when (action.type) {
                        on -> row[y] = row.getOrDefault(y, 0) + 1
                        off -> row[y] = max(0, row.getOrDefault(y, 0) - 1)
                        toggle -> row[y] = row.getOrDefault(y, 0) + 2
                    }
                }
            }
        }

        val result = lights.values.flatMap { it.values }.sum()
        println(result)
    }

    part1()
    part2()

}