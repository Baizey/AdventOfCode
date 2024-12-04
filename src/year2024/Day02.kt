package year2024

import utils.*
import java.util.stream.IntStream.range
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
    val lines = Input.get(2024, 2).asIntLines()

    fun part1() {
        println(lines.count(::isSafe))
    }

    fun part2() {
        val count = lines.count { line ->
            if (isSafe(line)) true
            else range(0, line.size)
                .mapToObj { i -> line.filterIndexed { index, _ -> index != i } }
                .anyMatch { isSafe(it) }
        }
        println(count)
    }
    part1()
    part2()
}
