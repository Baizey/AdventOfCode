package year2023

import utils.*

fun main() {
    val lines = Input.get(2023, 21)
    val startY = lines.indexOfFirst { it.contains("S") }
    val startX = lines.first { it.contains("S") }.indexOf('S')

    fun part1() {
        val totalSteps = 64

        data class QueueItem(val steps: Int, val agent: GridNavigator)

        val start = GridNavigator(startX, startY)
        val seen = HashMap<Int, HashSet<Int>>()
        val queue = ArrayDeque<QueueItem>()
        queue.addLast(QueueItem(0, start))
        seen.computeIfAbsent(startX) { HashSet() }.add(startY)

        var sum = 0L
        while (queue.isNotEmpty()) {
            val curr = queue.removeFirst()
            if (curr.steps > totalSteps) break
            if (curr.steps % 2 == 0) sum++
            curr.agent.fourDir()
                .filter { it.isInBound(lines[0].length, lines.size) }
                .filter { lines[it.y.toInt()][it.x.toInt()] == '.' }
                .filter { !seen.computeIfAbsent(it.x.toInt()) { HashSet() }.contains(it.y.toInt()) }
                .forEach {
                    seen.computeIfAbsent(it.x.toInt()) { HashSet() }.add(it.y.toInt())
                    queue.addLast(QueueItem(curr.steps + 1, it))
                }
        }

        println(sum)
    }

    fun part2() {
        val totalSteps = 26501365L

        data class QueueItem(val steps: Int, val agent: GridNavigator, val world: GridNavigator)

        val start = GridNavigator(startX, startY)
        val seen = HashMap<Long, HashMap<Long, HashMap<Long, HashSet<Long>>>>()
        val queue = ArrayDeque<QueueItem>()
        val width = lines[0].length
        val height = lines.size

        queue.addLast(QueueItem(0, start, GridNavigator(0, 0)))
        val startItem = queue.first()
        seen.computeIfAbsent(startItem.world.x) { HashMap() }
            .computeIfAbsent(startItem.world.y) { HashMap() }
            .computeIfAbsent(startItem.agent.x) { HashSet() }
            .add(startItem.agent.y)

        var sum = 0L
        while (queue.isNotEmpty()) {
            val curr = queue.removeFirst()
            if (curr.steps > totalSteps) break
            if (curr.steps % 2 == 0) sum++
            val nextStep = curr.steps + 1
            curr.agent.fourDir().forEach {
                val world =
                    if (it.x < 0) GridNavigator(curr.world.x - 1, curr.world.y)
                    else if (it.x >= width) GridNavigator(curr.world.x + 1, curr.world.y)
                    else if (it.y < 0) GridNavigator(curr.world.x, curr.world.y - 1)
                    else if (it.y >= height) GridNavigator(curr.world.x, curr.world.y + 1)
                    else curr.world
                val real = GridNavigator(
                    if (it.x < 0) it.x + width else if (it.x >= width) it.x % width else it.x,
                    if (it.y < 0) it.y + height else if (it.y >= height) it.y % height else it.y,
                )
                if (lines[real.y.toInt()][real.x.toInt()] == '#') {
                    return@forEach
                } else if (seen.computeIfAbsent(world.x) { HashMap() }
                        .computeIfAbsent(world.y) { HashMap() }
                        .computeIfAbsent(real.x) { HashSet() }
                        .contains(real.y)) {
                    return@forEach
                } else {
                    seen.computeIfAbsent(world.x) { HashMap() }
                        .computeIfAbsent(world.y) { HashMap() }
                        .computeIfAbsent(real.x) { HashSet() }
                        .add(real.y)
                    queue.addLast(QueueItem(nextStep, real, world))
                }
            }
        }

        println(sum)

    }
    part1()
    part2()
}