package year2023

import utils.*
import utils.Direction.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.min

fun main() {
    data class QueueItem(
        val agent: GridNavigator,
        val stepsLeft: Int,
        val cost: Int,
        val path: List<GridNavigator>,
    )

    val lines = Input.get(2023, 17).map { it.toCharArray().map { it.digitToInt() } }
    val width = lines[0].size
    val height = lines.size

    fun part1() {
        val start = GridNavigator(0, 0, east)
        val queue = PriorityQueue<QueueItem>(Comparator.comparing { it.cost })
        queue.add(QueueItem(start, 3, 0, listOf(start)))

        val seen = HashMap<Int, HashMap<Int, HashMap<Int, HashSet<Int>>>>()

        var best = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            //println("${current.agent.x} ${current.agent.y}")

            val xmap = seen.computeIfAbsent(current.agent.x.toInt()) { HashMap() }
            val ymap = xmap.computeIfAbsent(current.agent.y.toInt()) { HashMap() }
            val dmap = ymap.computeIfAbsent(current.agent.direction.ordinal) { HashSet() }
            if (dmap.contains(current.stepsLeft)) continue
            dmap.add(current.stepsLeft)


            if (current.cost >= best) continue

            if (current.agent.x.toInt() == width - 1 && current.agent.y.toInt() == height - 1) {
                best = min(current.cost, best)

                val path = current.path
                println(lines.mapIndexed { y, line ->
                    line.mapIndexed { x, c ->
                        val item = path.firstOrNull { it.x.toInt() == x && it.y.toInt() == y }
                        if (item != null) {
                            "${Console.red_background}${c}${Console.reset_colors}"
                        } else {
                            c.toString()
                        }
                    }.joinToString(separator = "")
                }.joinToString(separator = "\n"))

                continue
            }

            if (current.stepsLeft > 0) {
                val temp = current.agent.clone().moveForward()
                if (temp.isInBound(width, height)) {
                    val cost = current.cost + lines[temp.y.toInt()][temp.x.toInt()]
                    queue.add(QueueItem(temp, current.stepsLeft - 1, cost, current.path + listOf(temp)))
                }
            }
            if (true) {
                val temp = current.agent.clone().turnRight().moveForward()
                if (temp.isInBound(width, height)) {
                    val cost = current.cost + lines[temp.y.toInt()][temp.x.toInt()]
                    queue.add(QueueItem(temp, 2, cost, current.path + listOf(temp)))
                }
            }
            if (true) {
                val temp = current.agent.clone().turnLeft().moveForward()
                if (temp.isInBound(width, height)) {
                    val cost = current.cost + lines[temp.y.toInt()][temp.x.toInt()]
                    queue.add(QueueItem(temp, 2, cost, current.path + listOf(temp)))
                }
            }
        }

        println(best)
    }

    fun part2() {
        val queue = PriorityQueue<QueueItem>(Comparator.comparing { it.cost })
        fun moveNew(current: QueueItem, agent: GridNavigator) {
            var cost = current.cost
            for (i in 0..<4) {
                agent.moveForward()
                if (!agent.isInBound(width, height)) return
                cost += lines[agent.y.toInt()][agent.x.toInt()]
            }
            queue.add(QueueItem(agent, 6, cost, current.path + listOf(agent)))
        }

        queue.add(QueueItem(GridNavigator(0, 0, north), 0, 0, listOf(GridNavigator(0, 0, north))))
        queue.add(QueueItem(GridNavigator(0, 0, west), 0, 0, listOf(GridNavigator(0, 0, west))))

        val seen = HashMap<Int, HashMap<Int, HashMap<Int, HashSet<Int>>>>()

        var best = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            //println("${current.agent.x} ${current.agent.y}")

            val xmap = seen.computeIfAbsent(current.agent.x.toInt()) { HashMap() }
            val ymap = xmap.computeIfAbsent(current.agent.y.toInt()) { HashMap() }
            val dmap = ymap.computeIfAbsent(current.agent.direction.ordinal) { HashSet() }
            if (dmap.contains(current.stepsLeft)) continue
            dmap.add(current.stepsLeft)


            if (current.cost >= best) continue

            if (current.agent.x.toInt() == width - 1 && current.agent.y.toInt() == height - 1) {
                best = min(current.cost, best)

                val path = current.path
                println(lines.mapIndexed { y, line ->
                    line.mapIndexed { x, c ->
                        val item = path.firstOrNull { it.x.toInt() == x && it.y.toInt() == y }
                        if (item != null) {
                            "${Console.red_background}${c}${Console.reset_colors}"
                        } else {
                            c.toString()
                        }
                    }.joinToString(separator = "")
                }.joinToString(separator = "\n"))

                continue
            }

            if (current.stepsLeft > 0) {
                val temp = current.agent.clone().moveForward()
                if (temp.isInBound(width, height)) {
                    val cost = current.cost + lines[temp.y.toInt()][temp.x.toInt()]
                    queue.add(QueueItem(temp, current.stepsLeft - 1, cost, current.path + listOf(temp)))
                }
            }
            moveNew(current, current.agent.clone().turnLeft())
            moveNew(current, current.agent.clone().turnRight())
        }

        println(best)
    }
    part1()
    part2()
}