package year2024

import utils.Helpers.println
import utils.Helpers.surround
import utils.Input
import utils.grid.Direction.southeast
import utils.grid.Nav
import java.util.*

fun main() {
    val data = Input.get(2024, 18).asLongLines()

    fun solve(
        start: Nav,
        end: Nav,
        walls: Map<Long, Nav>,
    ): Long? {
        val grid = MutableList(end.y.toInt()) { MutableList(end.x.toInt()) { ' ' } }.surround('#')
        walls.values.forEach { it.setValue('#', grid) }
        start.setValue('S', grid)
        end.setValue('E', grid)

        val seen = mutableMapOf<Long, Pair<Long, Nav>>()
        val queue = PriorityQueue<Pair<Long, Nav>> { a, b -> a.first.compareTo(b.first) }
        queue.add(0L to start)
        while (queue.isNotEmpty()) {
            val pair = queue.poll()
            val (dist, pos) = pair

            if (seen.containsKey(pos.hash())) continue
            seen[pos.hash()] = pair

            pos.xyDirs()
                .filter { it.valueOrNull(grid) != '#' }
                .forEach { queue.add(dist + 1 to it) }

            if (pos.hash() == end.hash()) {
                var backward = end.clone()
                while (backward.hash() != start.hash()) {
                    backward.setValue('O', grid)
                    backward = backward.xyDirs()
                        .filter { seen.containsKey(it.hash()) }
                        .first { seen[it.hash()]!!.first + 1 == seen[backward.hash()]!!.first }
                }
                //grid.joinToString(separator = "\n") { it.joinToString(separator = "") }.println()
                return seen[end.hash()]!!.first
            }
        }
        return null
    }

    fun part1() {
        val corner = if (data.size < 100) 6 else 70
        val fallen = if (data.size < 100) 12 else 1024
        val start = Nav(0, 0) + southeast
        val end = Nav(corner, corner) + southeast
        val allWalls = data.map { Nav(it[0], it[1]) + southeast }
        val walls = allWalls.take(fallen).associateBy { it.hash() }
        solve(start, end, walls)?.println()
    }

    fun part2() {
        val corner = if (data.size < 100) 6 else 70
        val fallen = if (data.size < 100) 12 else 1024

        val start = Nav(0, 0) + southeast
        val end = Nav(corner, corner) + southeast
        val allWalls = data.map { Nav(it[0], it[1]) + southeast }

        for (i in fallen..allWalls.lastIndex) {
            val walls = allWalls.take(i).associateBy { it.hash() }
            val unsolvable = solve(start, end, walls) == null
            if (unsolvable) {
                listOf(allWalls[i - 1] - southeast).map { "${it.x},${it.y}" }.first().println()
                return
            }
        }

    }

    part1()
    part2()
}