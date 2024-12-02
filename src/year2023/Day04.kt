package year2023

import utils.*
import kotlin.math.max
import kotlin.math.min

fun main() {
    data class Card(val id: Int, val winners: List<Int>, val numbers: List<Int>) {
        fun matches(): Int {
            var score = 0
            val temps = numbers.toMutableList()
            winners.forEach {
                if (temps.contains(it)) {
                    score += 1
                    temps[temps.indexOf(it)] = -1
                }
            }
            return score
        }

        fun score(): Int {
            var score = 0
            val temps = numbers.toMutableList()
            winners.forEach {
                if (temps.contains(it)) {
                    score = max(1, score * 2)
                    temps[temps.indexOf(it)] = -1
                }
            }
            return score
        }
    }

    val cards = Input.get(2023, 4).map { line ->
        val id = line.split(":")[0].split("Card ")[1].trim().toInt()
        val winners = line.split(":")[1].split("|")[0].trim().split(Regex(" +")).map { it.toInt() }
        val numbers = line.split(":")[1].split("|")[1].trim().split(Regex(" +")).map { it.toInt() }
        Card(id, winners, numbers)
    }

    fun part1() {
        println(cards.sumOf { it.score() })
    }
    part1()

    fun part2() {
        val copies = cards.map { 1 }.toMutableList()
        for (i in 0..cards.lastIndex) {
            val card = cards[i]
            val matches = card.matches()
            for (j in (i + 1)..min(i + matches, cards.lastIndex)) copies[j] += copies[i]
        }
        println(copies.sum())
    }
    part2()

}