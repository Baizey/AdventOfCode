package year2015

import utils.*

fun main() {
    val lines = Input.get(2015, 1).asLines().joinToString(separator = "")

    fun part1() {
        var at = 0
        lines.toCharArray().forEach {
            if (it == '(') at++
            else if (it == ')') at--
        }
        println(at)
    }

    fun part2() {
        var at = 0
        lines.toCharArray().forEachIndexed { i, it ->
            if (it == '(') at++
            else if (it == ')') at--
            if (at < 0) {
                println(i + 1)
                return
            }
        }
    }

    part1()
    part2()

}