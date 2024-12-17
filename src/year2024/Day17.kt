package year2024

import utils.Helpers.println
import utils.Input
import kotlin.math.pow

fun main() {
    val input = Input.get(2024, 17).asLongLines()
    val program = input[4]

    fun run(aStart: Long): MutableList<Long> {
        var a = aStart
        var b = 0L
        var c = 0L
        fun combo(i: Long): Long = when (i) {
            0L, 1L, 2L, 3L -> i
            4L -> a
            5L -> b
            6L -> c
            else -> throw Error()
        }

        var i = 0
        val output = mutableListOf<Long>()
        while (i < program.lastIndex) {
            val opcode = program[i]
            when (opcode) {
                0L -> {
                    val combo = 2.0.pow(combo(program[i + 1]).toDouble()).toLong()
                    a = (a / combo)
                    i++
                }

                1L -> {
                    val literal = program[i + 1]
                    b = b.xor(literal)
                    i++
                }

                2L -> {
                    val combo = combo(program[i + 1]) % 8
                    b = combo
                    i++
                }

                3L -> {
                    if (a != 0L) {
                        i = program[i + 1].toInt() - 1
                    }
                }

                4L -> {
                    b = b.xor(c)
                    i++
                }

                5L -> {
                    val combo = combo(program[i + 1]) % 8L
                    output.add(combo)
                    i++
                }

                6L -> {
                    val combo = 2.0.pow(combo(program[i + 1]).toDouble()).toLong()
                    b = (a / combo)
                    i++
                }

                7L -> {
                    val combo = 2.0.pow(combo(program[i + 1]).toDouble()).toLong()
                    c = (a / combo)
                    i++
                }
            }
            i++
        }
        return output
    }


    fun part1() {
        run(input.first().first()).println()
    }

    fun test(aSoFar: Long, startIndex: Int): Long? {
        if (startIndex == -1) return aSoFar
        val shouldMatch = program.subList(startIndex, program.size).toList()
        for (i in 0L..<8L) {
            val a = aSoFar * 8L + i
            val result = run(a)
            if (result == shouldMatch) {
                val r = test(a, startIndex - 1)
                if (r != null) return r
            }
        }
        return null
    }

    fun part2() {
        test(0L, program.lastIndex)!!.println()
    }

    part1()
    part2()
}