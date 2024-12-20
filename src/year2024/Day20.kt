package year2024

import utils.Helpers.findExact
import utils.Helpers.findMatches
import utils.Helpers.println
import utils.Input
import utils.grid.Nav
import java.util.*
import kotlin.math.abs

fun main() {
    val grid = Input.get(2024, 20).asCharGrid()
    val start = grid.findExact { it == 'S' }!!
    val end = grid.findExact { it == 'E' }!!
    val walls = grid.findMatches { it == '#' }.associateBy { it.hash() }
    val opens = grid.findMatches { it != '#' }

    fun distanceLookup(from: Nav): Map<Long, Long> {
        val visited = mutableMapOf<Long, Long>()
        val queue = PriorityQueue<Pair<Long, Nav>> { a, b -> a.first.compareTo(b.first) }
        queue.add(Pair(0L, from.clone()))
        while (queue.isNotEmpty()) {
            val next = queue.poll()
            val (cost, navigator) = next

            if (visited.containsKey(navigator.hash())) continue
            visited[navigator.hash()] = cost

            navigator.xyDirs()
                .filter { it.hash() !in walls && it.isInBound(grid) }
                .forEach { queue.add(Pair(cost + 1L, it)) }
        }
        return visited
    }

    val fromStart = distanceLookup(start)
    val fromGoal = distanceLookup(end)
    val normal = fromStart[end.hash()]!!

    fun part1() {
        opens.flatMap { at ->
            at.xyDirs(2L)
                .filter { it.isInBound(grid) && it.hash() !in walls }
                .map { fromStart[at.hash()]!! + abs(at.x - it.x) + abs(at.y - it.y) + fromGoal[it.hash()]!! }
                .map { normal - it }
        }
            .count { it >= 100L }
            .println()
    }

    fun part2() {
        opens.flatMap { at ->
            at.xyDirs(20L)
                .filter { it.isInBound(grid) && it.hash() !in walls }
                .map { fromStart[at.hash()]!! + abs(at.x - it.x) + abs(at.y - it.y) + fromGoal[it.hash()]!! }
                .map { normal - it }
        }
            .count { it >= 100L }
            .println()
    }
    part1()
    part2()
}