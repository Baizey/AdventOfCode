package year2023

import utils.*
import java.util.stream.IntStream

fun main() {
    val words = Input.get(2023, 15).first().split(",")

    fun hash(str: String): Int {
        var result = 0
        str.forEach {
            result += it.code
            result *= 17
            result %= 256
        }
        return result
    }

    fun part1() {
        println(words.sumOf { hash(it) })
    }

    fun part2() {
        data class Wrap(val key: String, var value: Int)

        val boxes = IntStream.range(0, 256).mapToObj { mutableListOf<Wrap>() }.toList()
        words.forEach { word ->
            val parts = word.split(Regex("[-=]"))
            val key = parts.first()
            val box = boxes[hash(key)]
            if (word.contains("=")) {
                val value = parts.last().toInt()
                if (box.any { it.key == key }) {
                    box.first { it.key == key }.value = value
                } else box.add(Wrap(key, value))
            } else if (word.contains("-")) {
                box.removeIf { it.key == key }
            }
        }

        println(boxes.mapIndexed { i, box ->
            box.mapIndexed { j, wrap -> (i + 1) * (j + 1) * wrap.value }.sum()
        }.sum())

    }
    part1()
    part2()
}