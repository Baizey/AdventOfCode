package year2025

import utils.Input


fun main() {
    val input = Input.get(2025, 2).asString().trim()
        .split(",")
        .map { it.split("-") }
        .map { (a, b) -> a.toLong()..b.toLong() }

    fun part1(): Any {
        var sum = 0L

        for (range in input) {
            val low = range.first
            val high = range.last

            val minSize = low.toString().length.let { if (it % 2 == 0) it else it + 1 } / 2
            val maxSize = high.toString().length.let { if (it % 2 == 0) it else it - 1 } / 2
            for (i in minSize..maxSize step 2) {
                var halfStart = 1L
                repeat((1..<i).count()) { halfStart *= 10L }
                val halfStartStr = halfStart.toString()

                var at = (halfStartStr + halfStartStr).toLong()
                val step = halfStart * 10L + 1L
                val end = (halfStartStr + "0".repeat(halfStartStr.length)).toLong() * 10L
                while (at < low) at += step
                while (at < end && at <= high) {
                    sum += at
                    at += step
                }
            }
        }

        return sum
    }

    fun part2(): Any {
        var sum = 0L

        val seen = mutableSetOf<Long>()

        for (range in input) {
            val lowLength = range.first.toString().length
            val highLength = range.last.toString().length
            val maxRepeatLength = range.last.toString().length / 2
            for (seqLength in 1..maxRepeatLength) {
                var segmentSequence = "1"
                var stepSequence = 1L
                while (segmentSequence.length < seqLength) {
                    stepSequence *= 10L
                    segmentSequence += "0"
                }
                val jump = stepSequence * 10L

                var startString = segmentSequence
                var step = 1L

                do {
                    startString += segmentSequence
                    step = jump * step + 1L
                } while (startString.length < lowLength)

                while (startString.length <= highLength) {
                    var at = startString.toLong()
                    val end = ("1" + "0".repeat(startString.length)).toLong()
                    while (at < range.first) at += step
                    while (at < end && at <= range.last) {
                        if (seen.add(at)) {
                            sum += at
                        }
                        at += step
                    }

                    startString += segmentSequence
                    step = jump * step + 1L
                }
            }
        }

        return sum
    }

    fun p2(input: List<LongRange>): Any {
        val regex = Regex("$(\\d+)\\1+^")
        return input.sumOf { it.filter { n -> regex.matches(n.toString()) }.sum() }
    }

    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}