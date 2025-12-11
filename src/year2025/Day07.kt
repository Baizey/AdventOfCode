package year2025

import utils.*
import utils.grid.Direction
import utils.grid.NavBound

fun main() {
    val input = Input.get(2025, 7).asCharGrid()

    fun part1(): Any {
        val start = NavBound.find('S', input).turn(Direction.south)
        val queue = ArrayDeque<NavBound<Char>>()
        queue.add(start)
        val seen = mutableSetOf<Long>()
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            while (next.isInBound() && next.value() != '^') {
                next.moveForward()
            }
            if (!next.isInBound()) continue
            if (seen.add(next.hash())) {
                queue.add(next.clone().moveLeft(1))
                queue.add(next.clone().moveRight(1))
            }
        }
        return seen.size
    }

    data class Node(val nav: NavBound<Char>, val children: MutableList<Node>, var worlds: Long)

    fun search(nav: NavBound<Char>, known: MutableMap<Long, Node>): Node {
        val node = Node(nav, mutableListOf(), 2L)
        if (known.contains(nav.hash())) return known.getValue(nav.hash())
        known[nav.hash()] = node

        val left = nav.clone().moveLeft(1)
        while (left.isInBound() && left.value() != '^') left.moveForward()
        if (left.isInBound()) node.children.add(search(left, known))

        val right = nav.clone().moveRight(1)
        while (right.isInBound() && right.value() != '^') right.moveForward()
        if (right.isInBound()) node.children.add(search(right, known))

        when (node.children.size) {
            1 -> node.worlds = node.children.first().worlds + 1L
            2 -> node.worlds = node.children.sumOf { it.worlds }
        }
        return node
    }

    fun part2(): Any {
        val start = NavBound.find('S', input).turn(Direction.south)
        while (start.isInBound() && start.value() != '^') start.moveForward()
        if (!start.isInBound()) return 0
        return search(start, mutableMapOf()).worlds
    }

    println("Part 1: " + part1())
    println("Part 2: " + part2())
}