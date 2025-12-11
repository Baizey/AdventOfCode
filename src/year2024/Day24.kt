package year2024

import utils.*
import utils.Helpers.println

fun main() {
    data class Gate(
        val a: String,
        val b: String,
        val type: String,
        val out: String,
    ) {
        fun calc(
            gates: Map<String, Gate>,
            registers: MutableMap<String, Boolean>,
        ): Boolean {
            val a = registers[a] ?: gates[a]!!.calc(gates, registers)
            val b = registers[b] ?: gates[b]!!.calc(gates, registers)
            val out = calc(a, b)
            registers[this.out] = out
            return out
        }

        fun calc(a: Boolean, b: Boolean): Boolean {
            return when (type) {
                "OR" -> a || b
                "AND" -> a && b
                "XOR" -> a xor b
                else -> throw Error()
            }
        }
    }

    val input = Input.get(2024, 24).asWordLines()
    val registers = mutableMapOf<String, Boolean>()
    val gates = mutableListOf<Gate>()
    input.forEach { line ->
        if (line.size == 2) {
            registers[line[0]] = line[1] == "1"
        } else if (line.size == 4) {
            gates.add(Gate(line[0], line[2], line[1], line[3]))
        }
    }
    val gateLookup = gates.associateBy { it.out }

    fun solve(registers: MutableMap<String, Boolean>): String {
        val result = StringBuilder()
        for (i in 0..<46) {
            val out = "z${String.format("%02d", i)}"
            val r = gateLookup[out]?.calc(gateLookup, registers) ?: break
            result.append(if (r) "1" else "0")
        }
        return result.toString().reversed()
    }

    fun part1() {
        solve(registers).toLong(2).println()
    }

    fun toRegister(
        x: String,
        y: String,
    ): MutableMap<String, Boolean> {
        val map = mutableMapOf<String, Boolean>()
        x.padStart(100, '0').reversed().forEachIndexed { index, c -> map["x${String.format("%02d", index)}"] = c == '1' }
        y.padStart(100, '0').reversed().forEachIndexed { index, c -> map["y${String.format("%02d", index)}"] = c == '1' }
        return map
    }

    fun part2() {
        var i = "1"
        var round = 0L
        while (i.length < 46) {
            val reg = toRegister(i, i)
            round.println()
            val expected = (i + "0")
            val actual = solve(reg).trimStart { it == '0' }
            expected.println()
            actual.println()
            println()
            i += "0"
            round++
        }


    }
    part1()
    part2()
}