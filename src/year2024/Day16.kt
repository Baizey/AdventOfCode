package year2024

import utils.Helpers.findExact
import utils.Helpers.println
import utils.Input
import utils.grid.Direction.east
import utils.grid.Direction.north
import utils.grid.GridNavigator
import java.util.*
import kotlin.collections.ArrayDeque

fun main() {
    val grid = Input.get(2024, 16).asCharGrid()
    val start = grid.findExact { it == 'S' }!!.turn(east)
    val end = grid.findExact { it == 'E' }!!

    fun findBest(): Triple<Long, Set<Long>, GridNavigator> {
        val visited = mutableSetOf<Long>()
        val queue = PriorityQueue<Triple<Long, Set<Long>, GridNavigator>> { a, b -> a.first.compareTo(b.first) }
        queue.add(Triple(0L, setOf(start.hash()), start))
        while (queue.isNotEmpty()) {
            val triple = queue.poll()
            val (cost, path, navigator) = triple
            if (visited.contains(navigator.hashWithDir())) continue
            visited.add(navigator.hashWithDir())

            if (navigator.hash() == end.hash()) return triple

            val c = navigator.clone().moveForward()
            if (c.valueOf(grid) != '#') queue.add(Triple(cost + 1L, path + c.hash(), c))
            val a = navigator.clone().turnLeft()
            queue.add(Triple(cost + 1000L, path + a.hash(), a))
            val b = navigator.clone().turnRight()
            queue.add(Triple(cost + 1000L, path + b.hash(), b))
        }
        throw Error()
    }

    fun part1() {
        findBest().first.println()
    }

    fun fillGrid(maxCost: Long): MutableMap<Long, MutableList<Pair<Long, GridNavigator>>> {
        val visited = mutableMapOf<Long, MutableList<Pair<Long, GridNavigator>>>()
        val queue = PriorityQueue<Pair<Long, GridNavigator>> { a, b -> a.first.compareTo(b.first) }
        queue.add(Pair(0L, start))
        while (queue.isNotEmpty()) {
            val triple = queue.poll()
            val (cost, navigator) = triple
            if (cost > maxCost) continue
            if (visited.contains(navigator.hashWithDir())) {
                val list = visited[navigator.hashWithDir()]!!
                if (list[0].first == cost) list.add(triple)
                continue
            } else visited.computeIfAbsent(navigator.hashWithDir()) { mutableListOf() }.add(triple)

            val a = navigator.clone().turnLeft()
            queue.add(Pair(cost + 1000L, a))
            val b = navigator.clone().turnRight()
            queue.add(Pair(cost + 1000L, b))
            val c = navigator.clone().moveForward()
            if (c.valueOf(grid) != '#') queue.add(Pair(cost + 1L, c))
        }
        return visited
    }

    fun part2() {
        val lowestCost = findBest().first
        val allPaths = fillGrid(lowestCost)
        val queue = ArrayDeque<GridNavigator>()
        queue.add(end.clone().turn(north))
        queue.add(end.clone().turn(east))
        val visited = mutableSetOf<Long>()
        while (queue.isNotEmpty()) {
            val at = queue.removeFirst()

            visited.add(at.hash())
            if (at.hashWithDir() == start.hashWithDir()) continue

            allPaths[at.hashWithDir()]?.forEach {
                val (cost, navigator) = it

                val backCost = cost - 1L
                val backNav = navigator.clone().moveBackward()
                val back = allPaths[backNav.hashWithDir()]
                if (back != null && back[0].first == backCost) queue.addLast(backNav)

                val turnLeftCost = cost - 1000L
                val turnLeftNav = navigator.clone().turnLeft()
                val turnLeft = allPaths[turnLeftNav.hashWithDir()]
                if (turnLeft != null && turnLeft[0].first == turnLeftCost) queue.addLast(turnLeftNav)

                val turnRightCost = cost - 1000L
                val turnRightNav = navigator.clone().turnRight()
                val turnRight = allPaths[turnRightNav.hashWithDir()]
                if (turnRight != null && turnRight[0].first == turnRightCost) queue.addLast(turnRightNav)
            }
        }
        visited.size.println()
    }
    part1()
    part2()
}