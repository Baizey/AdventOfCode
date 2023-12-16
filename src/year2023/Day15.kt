package year2023

import utils.*

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

    }
    part1()
    part2()
}