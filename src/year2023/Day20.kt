package year2023

import utils.*
import java.util.*

fun main() {
    val flipFlop = '%'
    val inverse = '&'
    val broadcaster = 'b'

    class Data(val name: String, val to: List<String>, val type: Char)

    val ending = Data("output", listOf(), broadcaster)

    val lines = Input.get(2023, 20)
        .map {
            var (name, going) = it.split(" -> ")
            val type = name.first()
            name = name.removePrefix("$flipFlop").removePrefix("$inverse")
            Data(name, going.split(", "), type)
        }
    val map = mutableMapOf<String, Data>()

    lines.forEach { map[it.name] = it }

    fun part1() {
        data class QueueItem(val receiving: Boolean, val item: Data)

        val states = mutableMapOf<String, Boolean>()
        lines.filter { it.type == flipFlop }.forEach { states[it.name] = false }
        val start = map["broadcaster"]!!
        var high = 0L
        var low = 0L
        for (i in 0..<1000) {
            val queue = Stack<QueueItem>()
            queue.add(QueueItem(false, start))
            low++
            while (queue.isNotEmpty()) {
                val curr = queue.pop()
                when (curr.item.type) {
                    flipFlop -> {
                        if (curr.receiving) continue
                        states[curr.item.name] = !states[curr.item.name]!!
                        val sending = states[curr.item.name]!!
                        curr.item.to.map { map.getOrDefault(it, ending) }.forEach {
                            if (sending) high++ else low++
                            queue.add(QueueItem(sending, it))
                        }
                    }

                    inverse -> curr.item.to
                        .map { map.getOrDefault(it, ending) }
                        .forEach {
                            if (!curr.receiving) high++ else low++
                            queue.add(QueueItem(!curr.receiving, it))
                        }

                    broadcaster -> curr.item.to.map { map.getOrDefault(it, ending) }.forEach {
                        if (curr.receiving) high++ else low++
                        queue.add(QueueItem(curr.receiving, it))
                    }

                    else -> throw Error()
                }
            }
        }
        println("$high * $low = ${low * high}")
    }

    fun part2() {
    }

    part1()
    part2()
}