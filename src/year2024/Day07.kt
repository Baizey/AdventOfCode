package year2024

import utils.*
import utils.Input.println

fun main() {
    val input = Input.get(2024, 7).asLines().map { it.replace(":", "").split(" ").map { it.toLong() } }

    fun part1() {
        input
            .filter { data ->
                var nums = listOf(data[1])
                data.subList(2, data.size).forEach { num ->
                    val mul = nums.map { it * num }
                    val add = nums.map { it + num }
                    nums = add + mul
                }
                nums.contains(data[0])
            }
            .sumOf { it[0] }
            .println()
    }

    fun part2() {
        input
            .filter { data ->
                var nums = listOf(data[1])
                data.subList(2, data.size).forEach { num ->
                    val mul = nums.map { it * num }
                    val add = nums.map { it + num }
                    val concat = nums.map { (it.toString() + num.toString()).toLong() }
                    nums = add + mul + concat
                }
                nums.contains(data[0])
            }
            .sumOf { it[0] }
            .println()
    }
    part1()
    part2()
}