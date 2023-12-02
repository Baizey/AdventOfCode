package year2015

import utils.*
import java.awt.Point

fun main() {
    val chars = Input.get(2015, 3).flatMap { it.toCharArray().toList() }

    fun walk(chars: List<Char>): HashMap<Int, HashSet<Int>> {
        val at = Point(0, 0)
        val visited: HashMap<Int, HashSet<Int>> = HashMap()
        visited[0] = HashSet()
        visited.getValue(0).add(0)
        chars.forEach {
            when (it) {
                '^' -> at.y++
                'v' -> at.y--
                '>' -> at.x--
                '<' -> at.x++
            }
            if (!visited.containsKey(at.x)) visited[at.x] = HashSet()
            val b = visited.getValue(at.x)
            b.add(at.y)
        }

        return visited
    }

    fun part1() {
        val visited = walk(chars)
        val count = visited.values.sumOf { it.size }
        println(count)
    }

    fun part2() {
        val walk1 = walk(chars.filterIndexed { i, _ -> i % 2 == 0 })
        val walk2 = walk(chars.filterIndexed { i, _ -> i % 2 == 1 })

        walk1.forEach { (key, value) ->
            if (!walk2.containsKey(key)) walk2[key] = value
            else value.forEach { walk2[key]?.add(it) }
        }

        val count = walk2.values.sumOf { it.size }
        println(count)
    }

    part1()
    part2()

}