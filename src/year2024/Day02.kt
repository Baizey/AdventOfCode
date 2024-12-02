package year2024

import utils.*
import kotlin.math.abs

fun isSafe(line: List<Int>): Boolean {
    if (line.size <= 1) return true
    val isInc = line[1] > line[0]
    for (i in 1..line.lastIndex) {
        val a = line[i - 1]
        val b = line[i]
        if (abs(a - b) > 3) return false
        else if (isInc && a >= b) return false
        else if (!isInc && a <= b) return false
    }
    return true
}

fun main() {
    val lines = Input.get(2024, 2).map { line -> line.split(" ").map { it.toInt() } }

    fun part1() {
        println(lines.count(::isSafe))
    }

    fun part2() {
        var count = 0L
        lines.forEach { line ->
            if (isSafe(line)) count++
            else {
                for (i in 0..line.lastIndex) {
                    val tmp = line.filterIndexed { index, _ -> index != i }
                    if (isSafe(tmp)) {
                        count++
                        break
                    }
                }
            }
        }
        println(count)
    }
    part1()
    part2()
}
