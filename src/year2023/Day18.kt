package year2023

import utils.*
import utils.Direction.*
import java.util.*

fun main() {
    data class Data(val direction: Direction, val distance: Long)

    val lines = Input.get(2023, 18)

    fun solve(commands: List<Data>) {
        val agent = GridNavigator(0, 0)
        val corners = mutableListOf<GridNavigator>()
        commands.forEach {
            agent.move(it.direction, it.distance)
            corners.add(agent.clone())
        }

        val poly = Polygon(corners)
        println(poly.border() + poly.area())
    }

    fun part1() {
        val commands = lines
            .map {
                val (a, b, _) = it.split(" ")
                val dir = when (a.first()) {
                    'U' -> north
                    'D' -> south
                    'L' -> west
                    'R' -> east
                    else -> throw Error()
                }
                Data(dir, b.toLong())
            }
        solve(commands)
    }

    fun part2() {
        val commands = lines
            .map {
                val (_, _, c) = it.split(" ")
                c.substring(2, c.length - 1)
            }
            .map {
                val hex = it.take(5).toLong(16)
                val dir = when (it.last()) {
                    '0' -> east
                    '1' -> south
                    '2' -> west
                    '3' -> north
                    else -> throw Error()
                }
                Data(dir, hex)
            }
        solve(commands)
    }
    part1()
    part2()
}