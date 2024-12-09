package year2024

import utils.Helpers.println
import utils.Input
import java.lang.Character.digit
import kotlin.math.min

fun main() {
    val input = Input.get(2024, 9).asString().toCharArray()

    fun part1() {
        val list = mutableListOf<Long>()
        var last = if (input.lastIndex % 2 == 1) input.lastIndex - 1 else input.lastIndex
        var lastRemaining = digit(input[last], 10)
        for (i in 0..input.lastIndex) {
            if (last <= i) {
                if (lastRemaining > 0 && i == last) {
                    val id = last / 2
                    for (j in 0..<lastRemaining) list.add(id.toLong())
                }
                break
            }
            val d = digit(input[i], 10)
            if (i % 2 == 0) {
                val id = i / 2
                for (j in 0..<d) list.add(id.toLong())
            } else {
                var tmp = d
                while (tmp > 0) {
                    if (lastRemaining == 0) {
                        last -= 2
                        lastRemaining = digit(input[last], 10)
                    }
                    if (last <= i) break
                    val id = last / 2
                    val removing = min(lastRemaining, tmp)
                    for (j in 0..<removing) list.add(id.toLong())
                    lastRemaining -= removing
                    tmp -= removing
                }
            }
        }
        list.mapIndexed { index, value -> index * value }.sum().println()
    }

    data class Section(
        val id: Int,
        var valid: Boolean,
        var length: Long,
        var index: Long,
        val subsections: MutableList<Section> = mutableListOf(),
    ) {
        fun calc(): Long =
            if (valid) {
                val base = length * index * id
                val extra = (length * (length - 1) / 2) * id
                base + extra
            } else {
                subsections.sumOf { it.calc() }
            }

        override fun toString(): String {
            return if (valid)
                "$id".repeat(length.toInt())
            else
                subsections.joinToString("") { it.toString() } + ".".repeat(length.toInt())
        }
    }

    fun part2() {
        var index = 0L
        val sections = input.mapIndexed { i, c ->
            val id = i / 2
            val digit = digit(c, 10).toLong()
            val tmp = Section(id, i % 2 == 0, digit, index)
            index += digit
            tmp
        }
        val lastRound = if (sections.lastIndex % 2 == 0) sections.lastIndex else sections.lastIndex - 1
        for (i in lastRound downTo 0 step 2) {
            val moving = sections[i]
            for (j in 1..<i step 2) {
                val empty = sections[j]
                if (empty.length >= moving.length) {
                    val using = Section(moving.id, true, moving.length, empty.index)
                    moving.valid = false
                    empty.subsections.add(using)
                    empty.length -= moving.length
                    empty.index += moving.length
                    break
                }
            }
        }
        sections.sumOf { it.calc() }.println()
    }

    part1()
    part2()
}