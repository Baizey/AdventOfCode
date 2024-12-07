package year2024

import utils.*
import utils.Input.println

fun main() {
    val input = Input.get(2024, 7).asLines().map { it.replace(":", "").split(" ").map { it.toLong() } }

    fun part1() {
        input
            .filter { data ->
                val result = data[0]
                val nums = data.subList(2, data.size)
                var currentNums = listOf(data[1])
                nums.forEach { num ->
                    val mul = currentNums.map { it * num }
                    val add = currentNums.map { it + num }
                    currentNums = add + mul
                }
                currentNums.contains(result)
            }
            .sumOf { it[0] }
            .println()
    }

    fun part2() {
        input
            .filter { data ->
                val result = data[0]
                val nums = data.subList(2, data.size)
                var currentNums = listOf(data[1])
                nums.forEach { num ->
                    val mul = currentNums.map { it * num }
                    val add = currentNums.map { it + num }
                    val concat = currentNums.map { (it.toString() + num.toString()).toLong() }
                    currentNums = add + mul + concat
                }
                currentNums.contains(result)
            }
            .sumOf { it[0] }
            .println()
    }
    part1()
    part2()
}