package year2024

import utils.Input

fun main() {
    val rawInput = Input.get(2024, 3).joinToString(separator = " ")

    fun part1() {
        val regex = Regex("""mul\((\d+),(\d+)\)""")
        val result = regex.findAll(rawInput)
            .map {
                val a = it.groups[1]!!.value.toInt()
                val b = it.groups[2]!!.value.toInt()
                a * b
            }
            .sumOf { it }
        println(result)
    }

    fun part2() {
        val regex = Regex("""do\(\)|don't\(\)|mul\((\d+),(\d+)\)""")
        var isEnabled = true
        val result = regex.findAll(rawInput)
            .map {
                if (it.value == "do()") {
                    isEnabled = true
                    0
                } else if (it.value == "don't()") {
                    isEnabled = false
                    0
                } else if (isEnabled) {
                    val a = it.groups[1]!!.value.toInt()
                    val b = it.groups[2]!!.value.toInt()
                    a * b
                } else 0
            }
            .sumOf { it }
        println(result)
    }
    part1()
    part2()
}