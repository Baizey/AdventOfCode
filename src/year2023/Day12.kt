package year2023

import utils.*
import kotlin.math.max

fun main() {
    val outside = '_'
    val operational = '.'
    val damaged = '#'
    val unknown = '?'

    data class Line(val str: String, val nums: List<Int>)

    val lines = Input.get(2023, 12)
        .map { line ->
            val str = line.split(" ").first()
            val nums = line.split(" ").last().split(",").map { it.toInt() }
            Line(str, nums)
        }

    fun get(str: String, at: Int) = if (at in str.indices) str[at] else operational

    fun valid(str: String, from: Int, to: Int): Boolean {
        if (get(str, from - 1) == damaged) return false
        if (get(str, to) == damaged) return false
        for (i in from..<to) if (get(str, i) == operational) return false
        return true
    }

    fun solve(
        num: Int,
        damaged: List<Boolean>,
        currentValid: List<Boolean>,
        previousValid: List<Boolean>,
        currentMap: MutableList<Long>,
        previousMap: List<Long>,
    ) {
        var count = 0
        for (end in currentMap.size downTo 0) {
            val start = end - num
            if (start < 0) break
            if (damaged[start]) count++
            if (start > 0 && damaged[start - 1]) continue
            if (end < damaged.size && damaged[end]) {
                count--
                continue
            }

            var adding = 0L
            for (i in end + 1..<previousValid.size) {
                if (previousValid[i]) adding += previousMap[i]
                if (damaged[i]) break
            }

            val value = if (currentValid[start]) adding else 0L
            currentMap[start] = value
        }
    }

    fun solve(line: Line): Long {
        val wrap = "$operational".repeat(2)
        val str = wrap + line.str + wrap
        val nums = line.nums
        val dmg = str.map { it == damaged }

        val lastResult = str.map { 1L }
        val lastValid = str.map { false }.toMutableList()
        lastValid[lastValid.lastIndex] = true

        val firstResult = str.map { 0L }.toMutableList()
        val firstValid = str.map { false }.toMutableList()
        firstValid[0] = true

        val validTemp = nums.mapIndexed { _, num -> str.mapIndexed { j, _ -> valid(str, j, j + num) } }
        val valid = (listOf(firstValid) + validTemp + listOf(lastValid)).map { it.toMutableList() }

        val resultTemp = nums.map { str.map { 0L } }
        val result = (listOf(firstResult) + resultTemp + listOf(lastResult)).map { it.toMutableList() }

        val tempNums = listOf(1) + nums + listOf(1)
        for (i in tempNums.lastIndex - 1 downTo 0) {
            solve(
                tempNums[i], dmg,
                valid[i], valid[i + 1],
                result[i], result[i + 1],
            )
        }
        return result.first().first()
    }

    fun part1() {
        println(lines.sumOf(::solve))
    }

    fun part2() {
        println(lines
            .map {
                Line(
                    listOf(it.str, it.str, it.str, it.str, it.str).joinToString(separator = "$unknown"),
                    it.nums + it.nums + it.nums + it.nums + it.nums
                )
            }
            .sumOf(::solve))
    }
    part1()
    part2()
}