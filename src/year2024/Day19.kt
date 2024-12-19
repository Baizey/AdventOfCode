package year2024

import utils.Helpers.println
import utils.Input

fun main() {
    val input = Input.get(2024, 19).asWordLines()
    val patterns = input.first().toList()
    val words = input.subList(2, input.size).map { it.first() }

    fun isPossible(word: String): Boolean {
        val lookup = (word.map { false } + listOf(true)).toMutableList()
        for (at in word.lastIndex downTo 0) {
            for (p in patterns) {
                if (p.length + at > word.length) continue
                if (p.indices.any { p[it] != word[at + it] }) continue
                if (lookup[at + p.length]) {
                    lookup[at] = true
                    break
                }
            }
        }
        return lookup.first()
    }

    fun part1() {
        words.count { isPossible(it) }.println()
    }

    fun mutations(word: String): Long {
        val lookup = (word.map { 0L } + listOf(1L)).toMutableList()
        for (at in word.lastIndex downTo 0) {
            for (p in patterns) {
                if (p.length + at > word.length) continue
                if (p.indices.any { p[it] != word[at + it] }) continue
                lookup[at] += lookup[at + p.length]
            }
        }
        return lookup.first()
    }

    fun part2() {
        words.sumOf { mutations(it) }.println()
    }
    part1()
    part2()
}