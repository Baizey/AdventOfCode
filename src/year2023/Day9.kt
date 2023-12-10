package year2023

import utils.*

fun main() {
    val lines = Input.get(2023, 9).map { line -> line.split(" ").map { it.toLong() } }

    fun part1() {
        fun calcForwardNext(value: List<Long>): Long {
            val diffs = value.drop(1).mapIndexed { i, it -> it - value[i] }
            return if (diffs.all { it == 0L }) {
                value.last()
            } else {
                value.last() + calcForwardNext(diffs)
            }
        }

        println(lines.sumOf(::calcForwardNext))

    }

    fun part2() {
        fun calcBackwardNext(value: List<Long>): Long {
            val diffs = value.drop(1).mapIndexed { i, it -> it - value[i] }
            return if (diffs.all { it == 0L }) {
                value.first()
            } else {
                value.first() - calcBackwardNext(diffs)
            }
        }

        println(lines.sumOf(::calcBackwardNext))
    }
    part1()
    part2()
}