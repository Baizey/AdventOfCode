package year2023

import utils.*

fun main() {
    val fiveOfAKind = "five"
    val fourOfAKind = "four"
    val fullHouse = "full_house"
    val threeOfAKind = "three"
    val twoPair = "two_pair"
    val pair = "pair"
    val high = "high"
    val handOrder = listOf(high, pair, twoPair, threeOfAKind, fullHouse, fourOfAKind, fiveOfAKind)

    val lines = Input.get(2023, 7)

    fun part1() {
        val cardOrder = "AKQJT98765432".reversed()

        class Hand(val raw: String) : Comparable<Hand> {
            val type: String

            init {
                val chars = raw.toCharArray()
                val map = mutableMapOf<Char, Int>()
                chars.forEach { map.compute(it) { _, v -> v?.plus(1) ?: 1 } }
                val entries = map.entries
                type = if (entries.size == 1) {
                    fiveOfAKind
                } else if (entries.any { it.value == 4 }) {
                    fourOfAKind
                } else if (entries.any { it.value == 3 } && entries.any { it.value == 2 }) {
                    fullHouse
                } else if (entries.any { it.value == 3 }) {
                    threeOfAKind
                } else if (entries.count { it.value == 2 } == 2) {
                    twoPair
                } else if (entries.count { it.value == 2 } == 1) {
                    pair
                } else {
                    high
                }
            }

            override fun compareTo(other: Hand): Int {
                if (other.type == this.type) {
                    for (i in 0..<5) {
                        val r = cardOrder.indexOf(raw[i]) - cardOrder.indexOf(other.raw[i])
                        if (r != 0) return r
                    }
                    return 0
                } else {
                    return handOrder.indexOf(type) - handOrder.indexOf(other.type)
                }
            }
        }

        data class Play(val hand: Hand, val bid: Long) : Comparable<Play> {
            override fun compareTo(other: Play) = hand.compareTo(other.hand)
        }

        val plays = lines.map { Play(Hand(it.split(" ").first()), it.split(" ").last().toLong()) }
        val sorted = plays.sorted()
        val result = sorted
            .mapIndexed { i, v -> v.bid * (i + 1) }
            .sum()
        println(result)
    }

    fun part2() {
        val cardOrder = "AKQT98765432J".reversed()

        class Hand(val raw: String) : Comparable<Hand> {
            val type: String

            init {
                val map = mutableMapOf<Char, Int>()
                raw.forEach { map.compute(it) { _, v -> v?.plus(1) ?: 1 } }
                val jokers = map['J'] ?: 0
                val entries = map.entries.filter { it.key != 'J' }.map { it.value }
                type =
                    if (entries.isEmpty() || entries.any { it + jokers == 5 }) fiveOfAKind
                    else if (entries.any { it + jokers == 4 }) fourOfAKind
                    else if (
                        (entries.any { it == 3 } && entries.any { it == 2 })
                        || (entries.count { it == 2 } == 2 && jokers == 1)
                    ) fullHouse
                    else if (entries.any { it + jokers == 3 }) threeOfAKind
                    else if (entries.count { it == 2 } == 2) twoPair
                    else if (entries.any { it + jokers == 2 }) pair
                    else high
            }

            override fun compareTo(other: Hand): Int {
                if (other.type == this.type) {
                    for (i in 0..<5) {
                        val r = cardOrder.indexOf(raw[i]) - cardOrder.indexOf(other.raw[i])
                        if (r != 0) return r
                    }
                    return 0
                } else {
                    return handOrder.indexOf(type) - handOrder.indexOf(other.type)
                }
            }

            override fun toString() = raw
        }

        data class Play(val hand: Hand, val bid: Long) : Comparable<Play> {
            override fun compareTo(other: Play) = hand.compareTo(other.hand)
        }

        val plays = lines.map { Play(Hand(it.split(" ").first()), it.split(" ").last().toLong()) }
        val sorted = plays.sorted()
        val result = sorted
            .mapIndexed { i, v -> v.bid * (i + 1) }
            .sum()
        println(result)

    }

    fun part3() {

    }
    part1()
    part2()
    part3()
}