package year2023

import utils.*
import kotlin.math.max
import kotlin.math.min

fun main() {
    val rejected = "R"
    val accepted = "A"

    data class Item(val x: Int, val m: Int, val a: Int, val s: Int)
    data class Rule(
        val out: String, val variable: String, val barrier: Int, val needsGreaterThan: Boolean,
        val test: (item: Item) -> Boolean,
    )

    data class Ruleset(val name: String, val rules: List<Rule>)

    val rules = mutableListOf<Ruleset>()
    val items = mutableListOf<Item>()

    Input.get(2023, 19).forEach {
        if (it.startsWith('{')) {
            val (x, m, a, s) = it.substring(1, it.length - 1).split(',')
            items.add(
                Item(
                    x = x.split("=").last().toInt(),
                    m = m.split("=").last().toInt(),
                    a = a.split("=").last().toInt(),
                    s = s.split("=").last().toInt(),
                )
            )
        } else if (it.isNotBlank()) {
            val (name, rest) = it.split("{")
            val r = rest.substring(0, rest.length - 1).split(",").map { args ->
                if (!args.contains(":")) {
                    Rule(args, "x", 0, true) { true }
                } else {
                    val (data, out) = args.split(":")
                    val (variable, barrier) = data.split(Regex("[<>]"))
                    val isGreaterThan = data.contains(">")
                    val expected = barrier.toInt()
                    Rule(out, variable, expected, isGreaterThan) {
                        val value = when (variable) {
                            "x" -> it.x
                            "m" -> it.m
                            "a" -> it.a
                            "s" -> it.s
                            else -> throw Error()
                        }
                        if (isGreaterThan) value > expected else value < expected
                    }
                }
            }
            rules.add(Ruleset(name, r))
        }
    }

    val lookup = mutableMapOf<String, Ruleset>()
    rules.forEach { lookup[it.name] = it }
    val start = lookup["in"]!!
    fun part1() {
        var sum = 0L
        items.forEach { item ->
            var at = start.name
            while (at != accepted && at != rejected) {
                val curr = lookup[at]!!
                val jump = curr.rules.first { it.test(item) }
                at = jump.out
            }
            if (at == accepted) {
                sum += item.m + item.s + item.x + item.a
            }
        }
        println(sum)
    }

    fun part2() {
        data class Range(var min: Int = 1, var max: Int = 4000) {
            fun total() = (max - min + 1L)
            fun isOpen() = max >= min
            fun isClosed() = !isOpen()
        }

        data class Test(val x: Range, val m: Range, val a: Range, val s: Range) {
            fun sum() = x.total() * m.total() * a.total() * s.total()
            val ranges = listOf(x, m, a, s)
            fun get(variable: String) = when (variable) {
                "x" -> x
                "m" -> m
                "a" -> a
                "s" -> s
                else -> throw Error()
            }

            fun clone() = Test(Range(x.min, x.max), Range(m.min, m.max), Range(a.min, a.max), Range(s.min, s.max))
        }

        val initial = Test(Range(), Range(), Range(), Range())

        fun rec(curr: Test, set: Ruleset): List<Test> {
            return set.rules.flatMap {
                val rejectRange = curr.get(it.variable)
                val accepting = curr.clone()
                val acceptingRange = accepting.get(it.variable)
                val limit = it.barrier
                val needsGreater = it.needsGreaterThan
                if (needsGreater) {
                    rejectRange.max = min(rejectRange.max, limit)
                    acceptingRange.min = max(acceptingRange.min, limit + 1)
                } else {
                    rejectRange.min = max(rejectRange.min, limit)
                    acceptingRange.max = min(acceptingRange.max, limit - 1)
                }
                if (accepting.ranges.all(Range::isOpen))
                    when (it.out) {
                        accepted -> listOf(accepting)
                        rejected -> listOf()
                        else -> rec(accepting, lookup[it.out]!!)
                    }
                else listOf()
            }
        }

        val ranges = rec(initial, start)
        val result = ranges.sumOf { it.sum() }
        println(result)

    }
    part1()
    part2()
}