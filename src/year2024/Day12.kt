package year2024

import utils.grid.Nav
import utils.Helpers.findMatches
import utils.Helpers.println
import utils.Helpers.surround
import utils.Input

fun main() {
    val grid = Input.get(2024, 12).asCharGrid().surround('.')

    fun part1() {
        val seen = mutableSetOf<Long>()
        grid.findMatches { it != '.' }
            .sumOf { start ->
                val type = start.value(grid)
                if (seen.contains(start.hash())) 0L
                else {
                    var area = 0L
                    var perimeter = 0L

                    val queue = ArrayDeque<Nav>()
                    queue.add(start)
                    while (queue.isNotEmpty()) {
                        val at = queue.removeFirst()
                        if (seen.contains(at.hash())) continue
                        seen.add(at.hash())
                        area++
                        at.xyDirs().forEach {
                            if (it.isNotInBound(grid) || it.value(grid) != type) perimeter++
                            else queue.addLast(it)
                        }
                    }
                    area * perimeter
                }
            }
            .println()
    }

    fun part2() {
        val seenArea = mutableSetOf<Long>()
        grid.findMatches { it != '.' }
            .sumOf { start ->
                val type = start.value(grid)
                if (seenArea.contains(start.hash())) 0L
                else {
                    var area = 0L
                    val perimeter = mutableMapOf<Long, Nav>()
                    val queue = ArrayDeque<Nav>()
                    queue.add(start)
                    while (queue.isNotEmpty()) {
                        val at = queue.removeFirst()
                        if (seenArea.contains(at.hash())) continue
                        seenArea.add(at.hash())
                        area++
                        at.xyDirs().forEach { if (it.valueOrNull(grid) != type) perimeter[it.hashWithDir()] = it else queue.addLast(it) }
                    }

                    val sides = perimeter.values.toList().count { edge ->
                        if (!perimeter.containsKey(edge.hashWithDir())) false
                        else {
                            var t = edge.clone().turnLeft().moveForward().turnRight()
                            while (perimeter.containsKey(t.hashWithDir())) {
                                perimeter.remove(t.hashWithDir())
                                t.turnLeft().moveForward().turnRight()
                            }
                            t = edge.clone()
                            while (perimeter.containsKey(t.hashWithDir())) {
                                perimeter.remove(t.hashWithDir())
                                t.turnRight().moveForward().turnLeft()
                            }
                            true
                        }
                    }

                    area * sides
                }
            }
            .println()
    }
    part1()
    part2()
}