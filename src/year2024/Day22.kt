package year2024

import utils.*
import utils.Helpers.println
import kotlin.math.roundToLong

fun main() {
    val nums = Input.get(2024, 22).asLongLines().map { it.first() }

    fun Long.mix(other: Long): Long = this xor other
    fun Long.prune(): Long = this % 16777216L

    fun nextSecret(step0: Long): Long {
        1 to 4
        val step1 = step0.mix(step0 * 64L).prune()
        val step2 = step1.mix(step1 / 32L).prune()
        val step3 = step2.mix(step2 * 2048L).prune()
        return step3
    }

    fun sim(rounds: Long, seed: Long): Long {
        var res = seed
        for (i in 0..rounds) {
            res = nextSecret(res)
        }
        return res
    }

    fun part1() {
        nums.sumOf { sim(2000L, it) }.println()
    }

    fun part2() {

    }
    part1()
    part2()
}