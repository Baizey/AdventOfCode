package year2025

import utils.*

fun main() {
    val input = Input.get(2025, 6)
    val args = input.asLines().last().trim().split(Regex("\\s+"))

    fun mul(a: Long, b: Long): Long = a * b
    fun add(a: Long, b: Long): Long = a + b

    fun part1(): Any {
        val nums = input.asLongLines().dropLast(1)
        var sum = 0L
        for (i in 0..<args.size) {
            val symbol = args[i]
            var temp = nums[0][i]
            for (j in 1..<nums.size) {
                val n = nums[j][i]
                when (symbol) {
                    "+" -> temp += n
                    "*" -> temp *= n
                    else -> throw Exception(symbol)
                }
            }
            sum += temp
        }
        return sum
    }

    fun part2(): Any {
        var result = 0L
        val lines = input.asLines().dropLast(1)

        var at = 0

        val max = lines.maxOf { it.length }
        for (arg in 0..<args.size) {
            val symbol = args[arg]

            val nums = mutableListOf<Long>()
            while (true) {
                if (at >= max) break
                val n = lines.map { if (at >= it.length) "" else it[at] }.joinToString(separator = "").trim()
                if (n.isEmpty()) {
                    at++
                    break
                } else {
                    nums.add(n.toLong())
                    at++
                }
            }

            var tmp = nums[0]
            for (j in 1..<nums.size) {
                when (symbol) {
                    "+" -> tmp += nums[j]
                    "*" -> tmp *= nums[j]
                    else -> throw Exception(symbol)
                }
            }
            result += tmp
        }
        return result
    }

    println("Part 1: " + part1())
    println("Part 2: " + part2())
}