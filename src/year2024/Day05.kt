package year2024

import utils.Input

fun main() {
    val input = Input.get(2024, 5).asLines()

    val rules = mutableListOf<List<Int>>()
    val lists = mutableListOf<List<Int>>()

    for (line in input) {
        if (line.isEmpty()) continue
        else if (line.contains("|")) {
            rules.add(line.split("|").map { it.toInt() })
        } else {
            lists.add(line.split(",").map { it.toInt() })
        }
    }
    val rulesBefore: MutableMap<Int, Set<Int>> = mutableMapOf()
    val rulesAfter: MutableMap<Int, Set<Int>> = mutableMapOf()
    rules.groupBy { it[0] }.forEach { (t, u) -> rulesBefore[t] = u.map { it[1] }.toSet() }
    rules.groupBy { it[1] }.forEach { (t, u) -> rulesAfter[t] = u.map { it[0] }.toSet() }

    fun isValid(list: List<Int>): Boolean {
        val before = mutableSetOf<Int>()
        val after = list.toSet().toMutableSet()
        for (i in list.indices) {
            val element = list[i]
            after.remove(element)

            val isBreakingBefore = rulesBefore[element]!!.any { before.contains(it) }
            val isBreakingAfter = rulesAfter[element]!!.any { after.contains(it) }
            if (isBreakingBefore || isBreakingAfter) return false

            before.add(element)
        }
        return true
    }

    fun part1() {
        val result = lists
            .filter(::isValid)
            .sumOf { list -> list[list.size / 2] }
        println(result)
    }

    fun fix(list: List<Int>): List<Int> {
        val result = mutableListOf<Int>()
        for (round in 0..list.lastIndex) {
            val t = list.size - 1 - round
            for (i in 0..list.lastIndex) {
                val r = rulesBefore[list[i]]!!
                val beforeCount = r.filter { list.contains(it) }.size
                if (beforeCount == t) {
                    result.add(list[i])
                    break
                }
            }
        }
        return result
    }

    fun part2() {
        val result = lists
            .filter { !isValid(it) }
            .map { fix(it) }
            .sumOf { list -> list[list.size / 2] }
        println(result)
    }
    part1()
    part2()
}